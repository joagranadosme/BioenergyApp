package com.procesosyoperaciones.bioenergy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private String idUser = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usernameEditText = (EditText) findViewById(R.id.input_username);
        passwordEditText = (EditText) findViewById(R.id.input_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("IdUser",idUser);
        editor.apply();
    }

    public void setIdUser(String idUser){
        this.idUser = idUser;
    }

    public void loginClick(View view){

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String hash = null;

        try {
            hash = Cipher.hash(password);
        }catch (Exception e){
            Log.e("Login", e.toString());
        }

        new LoginTask(this).execute(username, hash);

    }

    private class LoginTask extends AsyncTask<String, Void, String>{

        private ProgressDialog progressDialog;
        private Login activity;

        public LoginTask(Login activity){
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;

            try{
                String mUrl = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=login";
                String username = "document";
                String password = "password";

                Uri buildUri = Uri.parse(mUrl).buildUpon()
                        .appendQueryParameter(username, params[0])
                        .appendQueryParameter(password, params[1])
                        .build();

                URL url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                jsonString = stringBuffer.toString();

            }catch (Exception e){

                Log.e("Login", e.toString());
                Toast.makeText(activity.getApplicationContext(), "Error iniciando sesión", Toast.LENGTH_LONG).show();

            }finally {

                if(urlConnection != null)
                    urlConnection.disconnect();

                if(reader != null)
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e("Reader", e.toString());
                    }

                return jsonString;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject = new JSONObject(s);
                activity.setIdUser(jsonObject.getString("idUser"));

                Intent intent = new Intent(activity.getApplicationContext(), RolActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idUser", jsonObject.getString("idUser"));
                intent.putExtra("idRol", jsonObject.getString("idRol"));
                intent.putExtra("name", jsonObject.getString("name"));
                activity.getApplicationContext().startActivity(intent);

            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), "Tu usuario y contraseña no coinciden", Toast.LENGTH_LONG).show();

            }finally {

                progressDialog.dismiss();

            }

        }

    }

}

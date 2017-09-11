package com.procesosyoperaciones.bioenergy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        //Item selected in menu.
        switch (item.getItemId()){
            case R.id.back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ingresarClick(View view){

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

}

class LoginTask extends AsyncTask<String, Void, String>{

    private ProgressDialog progressDialog;
    private Context context;
    private int idUser;

    public LoginTask(Login activity){

        context = activity.getApplicationContext();
        progressDialog = new ProgressDialog(activity);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
            Toast.makeText(context, "Error iniciando sesión", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Tu usuario y contraseña no coinciden", Toast.LENGTH_LONG).show();
        }finally {
            progressDialog.dismiss();
        }
    }
}

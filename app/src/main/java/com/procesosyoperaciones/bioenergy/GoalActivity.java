package com.procesosyoperaciones.bioenergy;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.procesosyoperaciones.bioenergy.data_objects.Goal;
import com.procesosyoperaciones.bioenergy.data_objects.Period;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoalActivity extends AppCompatActivity {

    private int type = -1;
    private String idUser;
    private GoalFragment goalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");

        goalFragment = (GoalFragment) getSupportFragmentManager().findFragmentById(R.id.goal_container);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUser = preferences.getString("IdUser", "-1");

        if(goalFragment == null){
            goalFragment = GoalFragment.newInstance(type);
            getSupportFragmentManager().beginTransaction().add(R.id.goal_container, goalFragment).commit();
        }
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

    public void sendClick(View view){

        if(goalFragment.sendClick()){
            Goal[] goals = goalFragment.getGoals();
            ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setMessage("Guardando objetivos...");
            progressDialog.show();
            new DeleteGoalsTask().execute();
            new SaveGoalsTask().execute(goals);
            progressDialog.dismiss();
            Toast.makeText(view.getContext(), "¡Objetivos creados correctamente!", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(view.getContext(), "¡La suma de tus objetivos debe ser igual a 100%!", Toast.LENGTH_LONG).show();
        }

    }

    public void addClick(View view){
        goalFragment.addClick();
    }

    private class DeleteGoalsTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;

            try{

                String mUrl = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=delete_goal";

                Uri buildUri = Uri.parse(mUrl).buildUpon()
                        .appendQueryParameter("idUser", idUser)
                        .appendQueryParameter("type", type+"")
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

                Log.e("BossActivity", e.toString());

            }finally {

                if(urlConnection != null)
                    urlConnection.disconnect();

                if(reader != null)
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e("Reader", e.toString());
                    }

                return null;

            }
        }
    }

    private class SaveGoalsTask extends AsyncTask<Goal,Void,String>{

        @Override
        protected String doInBackground(Goal... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try{

                String mUrl = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=add_goal";

                for(int j=0; j<params.length; j++) {

                    Uri buildUri = Uri.parse(mUrl).buildUpon()
                            .appendQueryParameter("idUser", idUser)
                            .appendQueryParameter("perspective", params[j].getPerspective())
                            .appendQueryParameter("description", params[j].getDescription())
                            .appendQueryParameter("general", params[j].getGeneral())
                            .appendQueryParameter("specific", params[j].getSpecific())
                            .appendQueryParameter("formula", params[j].getFormula())
                            .appendQueryParameter("unit", params[j].getUnity())
                            .appendQueryParameter("weight", params[j].getWeight() + "")
                            .appendQueryParameter("type", type+"")
                            .appendQueryParameter("period", params[j].getPeriod())
                            .build();

                    URL url = new URL(buildUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }

                    JSONObject response = new JSONObject(stringBuffer.toString());

                    Period[] aux = params[j].getPeriods();
                    for (int i = 0; i < aux.length; i++) {
                        HttpURLConnection urlConnection2;
                        BufferedReader reader2;
                        String mUrl2 = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=add_period";
                        //TODO: Choose correct date.
                        Uri buildUri2 = Uri.parse(mUrl2).buildUpon()
                                .appendQueryParameter("idPeriod", response.getString("idPeriod"))
                                .appendQueryParameter("num", i + "")
                                .appendQueryParameter("date", "2017-08-09")
                                .appendQueryParameter("proposed", aux[i].getProposed() + "")
                                .appendQueryParameter("reached", aux[i].getReached() + "")
                                .appendQueryParameter("compromise", aux[i].getCompromise())
                                .build();

                        URL url2 = new URL(buildUri2.toString());
                        urlConnection2 = (HttpURLConnection) url2.openConnection();
                        urlConnection2.setRequestMethod("GET");
                        urlConnection2.connect();

                        InputStream inputStream2 = urlConnection2.getInputStream();
                        StringBuffer stringBuffer2 = new StringBuffer();
                        reader2 = new BufferedReader(new InputStreamReader(inputStream2));

                        String line2;
                        while ((line2 = reader2.readLine()) != null) {
                            stringBuffer2.append(line2 + "\n");
                        }

                        JSONObject response2 = new JSONObject(stringBuffer2.toString());

                        if (urlConnection2 != null)
                            urlConnection2.disconnect();
                        reader2.close();
                    }
                }

            }catch (Exception e){

                Log.e("BossActivity", e.toString());

            }finally {

                if(urlConnection != null)
                    urlConnection.disconnect();

                if(reader != null)
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e("Reader", e.toString());
                    }

                return null;

            }
        }
    }

}

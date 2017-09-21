package com.procesosyoperaciones.bioenergy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.procesosyoperaciones.bioenergy.data_objects.Boss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BossActivity extends AppCompatActivity {

    private String idUser;
    private BossFragment bossFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);

        bossFragment = (BossFragment) getSupportFragmentManager().findFragmentById(R.id.boss_container);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUser = preferences.getString("IdUser", "-1");

        if(bossFragment == null){
            bossFragment = BossFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.boss_container, bossFragment).commit();
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

        final Boss boss = bossFragment.getBoss();
        if(boss == null){
            Toast.makeText(view.getContext(), "¡Selecciona tu jefe!", Toast.LENGTH_LONG).show();
        }else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Selección de jefe")
                    .setMessage("¿Estás seguro de elegir a " + boss.getName() + " como tu jefe?\n")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new SetBossTask().execute(idUser, boss.getId());
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    private class SetBossTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;

            try{

                String mUrl = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=set_boss";

                Uri buildUri = Uri.parse(mUrl).buildUpon()
                        .appendQueryParameter("idUser", params[0])
                        .appendQueryParameter("idBoss", params[1])
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

}


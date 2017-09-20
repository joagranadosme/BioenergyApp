package com.procesosyoperaciones.bioenergy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.procesosyoperaciones.bioenergy.data_objects.Boss;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BossFragment extends Fragment {

    private String idUser;
    private Boss currentBoss;
    private ListView bossList;
    private BossAdapter bossAdapter;

    public BossFragment() {}

    public static BossFragment newInstance(){
        BossFragment fragment = new BossFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_boss, container, false);
        bossList = (ListView) root.findViewById(R.id.boss_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(root.getContext());
        idUser = preferences.getString("IdUser", "-1");

        new BossTask(getActivity()).execute(idUser);

        bossList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                currentBoss = bossAdapter.getItem(position);
            }
        });

        return root;
    }

    public Boss getBoss(){
        return currentBoss;
    }

    private class BossTask extends AsyncTask<String, Void, List<Boss>> {

        private ProgressDialog progressDialog;
        private Context context;

        public BossTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Cargando jefes...");
            progressDialog.show();
        }

        @Override
        protected List<Boss> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;
            List<Boss> list = new ArrayList<>();

            try{

                String mUrl = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=bosses";

                Uri buildUri = Uri.parse(mUrl).buildUpon()
                        .appendQueryParameter("idUser", params[0])
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
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String departament = jsonObject.getString("departament");
                    String area = jsonObject.getString("area");
                    list.add(new Boss(id, name, departament, area, 0));
                }

            }catch (Exception e){

                Log.e("BossFragment", e.toString());

            }finally {

                if(urlConnection != null)
                    urlConnection.disconnect();

                if(reader != null)
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e("Reader", e.toString());
                    }

                return list;

            }

        }

        @Override
        protected void onPostExecute(List<Boss> bosses) {
            super.onPostExecute(bosses);
            progressDialog.dismiss();
            bossAdapter = new BossAdapter(getActivity(), bosses);
            bossList.setAdapter(bossAdapter);
        }

    }

}



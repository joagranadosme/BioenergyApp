package com.procesosyoperaciones.bioenergy;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.procesosyoperaciones.bioenergy.data_objects.Boss;
import com.procesosyoperaciones.bioenergy.data_objects.Goal;
import com.procesosyoperaciones.bioenergy.data_objects.Period;

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

public class GoalFragment extends Fragment {

    private Goal current;
    private String idUser;
    private static int type;
    private ListView goalList;
    private TextView weightTextView;
    private GoalAdapter goalAdapter;

    private final int GOAL_RESULT = 1;

    public GoalFragment() {}

    public static GoalFragment newInstance(int typeO){
        GoalFragment fragment = new GoalFragment();
        type = typeO;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_goal, container, false);
        goalList = (ListView) root.findViewById(R.id.goal_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(root.getContext());
        idUser = preferences.getString("IdUser", "-1");

        ProgressDialog progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setMessage("Cargando objetivos...");
        progressDialog.show();
        new GoalTask().execute(idUser, type+"", (type+2)+"");
        progressDialog.dismiss();

        weightTextView = (TextView) root.findViewById(R.id.weightText);

        goalList.setLongClickable(true);
        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current = goalAdapter.getItem(position);
                current.setType(type);
                Intent intent = new Intent(getActivity(), FormActivity.class);
                intent.putExtra("goal", current);
                startActivityForResult(intent, GOAL_RESULT);
            }
        });

        goalList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                current = goalAdapter.getItem(position);
                goalAdapter.remove(current);
                return true;
            }
        });

        return root;
    }

    public boolean sendClick(){
        return getTotalWeight() != 100 ? false : true;
    }

    public void addClick(){
        if(type == 0)
            goalAdapter.add(new Goal(0));
        if(type == 1)
            goalAdapter.add(new Goal("Personal", 0));
        goalAdapter.notifyDataSetChanged();
    }

    public int getTotalWeight(){
        if(goalAdapter != null) {
            int total = 0;
            for (int i = 0; i < goalAdapter.getCount(); i++)
                total += goalAdapter.getItem(i).getWeight();
            return total;
        }
        return 0;
    }

    public Goal[] getGoals(){
        Goal[] goals = new Goal[goalAdapter.getCount()];
        for(int i=0; i<goals.length; i++)
            goals[i] = goalAdapter.getItem(i);
        return goals;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GOAL_RESULT && resultCode == Activity.RESULT_OK){
            goalAdapter.remove(current);
            current = (Goal) data.getSerializableExtra("goal");
            goalAdapter.add(current);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        weightTextView.setText(getTotalWeight() + "%");
    }

    private class GoalTask extends AsyncTask<String, Void, List<Goal>>{

        @Override
        protected List<Goal> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;
            List<Goal> list = new ArrayList<>();

            try{

                String mUrl = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=get_goal";

                Uri buildUri = Uri.parse(mUrl).buildUpon()
                        .appendQueryParameter("idUser", params[0])
                        .appendQueryParameter("type1", params[1])
                        .appendQueryParameter("type2", params[2])
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
                    int idPeriod = Integer.parseInt(jsonObject.getString("idPeriod"));
                    String perspective = jsonObject.getString("perspective");
                    String description = jsonObject.getString("description");
                    String general = jsonObject.getString("generalInd");
                    String specific = jsonObject.getString("specificInd");
                    String formula = jsonObject.getString("formula");
                    String unit = jsonObject.getString("unit");
                    int weight = Integer.parseInt(jsonObject.getString("weight"));
                    int type = Integer.parseInt(jsonObject.getString("type"));
                    String period = jsonObject.getString("measure");

                    list.add(new Goal(idPeriod,perspective,description,general,specific,formula,unit,weight,type,period,null));
                }

                for(int i=0; i<list.size(); i++){

                    Goal aux = list.get(i);
                    HttpURLConnection urlConnection2 = null;
                    BufferedReader reader2 = null;
                    String jsonString2 = null;

                    String mUrl2 = "http://www.procesosyoperaciones.com/BioenergyApi.php?q=get_period";

                    Uri buildUri2 = Uri.parse(mUrl2).buildUpon()
                            .appendQueryParameter("idPeriod", aux.getId()+"")
                            .build();

                    Log.e("URL", buildUri2.toString());

                    URL url2 = new URL(buildUri2.toString());
                    urlConnection2 = (HttpURLConnection) url2.openConnection();
                    urlConnection2.setRequestMethod("GET");
                    urlConnection2.connect();

                    InputStream inputStream2 = urlConnection2.getInputStream();
                    StringBuffer stringBuffer2 = new StringBuffer();
                    reader2 = new BufferedReader(new InputStreamReader(inputStream2));

                    String line2;
                    while((line2 = reader2.readLine()) != null){
                        stringBuffer2.append(line2 + "\n");
                    }
                    jsonString2 = stringBuffer2.toString();
                    JSONArray jsonArray2 = new JSONArray(jsonString2);

                    Period[] periods = new Period[jsonArray2.length()];
                    for(int j=0; j<jsonArray2.length(); j++){

                        JSONObject jsonObject2 = (JSONObject) jsonArray2.get(i);
                        int period = Integer.parseInt(jsonObject2.getString("num"));
                        int proposed = Integer.parseInt(jsonObject2.getString("proposed"));
                        int reached = Integer.parseInt(jsonObject2.getString("reached"));
                        String compromise = jsonObject2.getString("compromise");

                        periods[i] = new Period(period,proposed,reached,compromise);
                    }
                    Goal auxGoal = list.get(i);
                    auxGoal.setPeriods(periods);
                    list.set(i,auxGoal);
                }

            }catch (Exception e){

                Log.e("GoalFragment", e.toString());

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
        protected void onPostExecute(List<Goal> goals) {
            super.onPostExecute(goals);
            goalAdapter = new GoalAdapter(getActivity(), goals);
            goalList.setAdapter(goalAdapter);
        }

    }

}

package com.procesosyoperaciones.bioenergy;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GoalFragment extends Fragment {

    private Goal current;
    private ListView goalList;
    private TextView weightTextView;
    private GoalAdapter goalAdapter;

    private final int GOAL_RESULT = 1;

    public GoalFragment() {}

    public static GoalFragment newInstance(){
        GoalFragment fragment = new GoalFragment();
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

        weightTextView = (TextView) root.findViewById(R.id.weightText);
        new GoalTask(getActivity()).execute("1");

        goalList.setLongClickable(true);
        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current = goalAdapter.getItem(position);
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
        goalAdapter.add(new Goal());
        goalAdapter.notifyDataSetChanged();
    }

    public int getTotalWeight(){
        if(goalAdapter != null) {
            int total = 0;
            for (int i = 0; i < goalAdapter.getCount(); i++)
                total += goalAdapter.getItem(i).getLocalWeight();
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

        private ProgressDialog progressDialog;
        private Context context;

        public GoalTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Cargando objetivos...");
            progressDialog.show();
        }

        @Override
        protected List<Goal> doInBackground(String... params) {
            List<Goal> goals = new ArrayList<>();
            return goals;
        }

        @Override
        protected void onPostExecute(List<Goal> goals) {
            super.onPostExecute(goals);
            progressDialog.dismiss();
            goalAdapter = new GoalAdapter(getActivity(), goals);
            goalList.setAdapter(goalAdapter);
        }
    }

}

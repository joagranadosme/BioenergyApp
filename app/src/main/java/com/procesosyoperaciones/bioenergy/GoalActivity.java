package com.procesosyoperaciones.bioenergy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GoalActivity extends AppCompatActivity {

    private GoalFragment goalFragment;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");

        goalFragment = (GoalFragment) getSupportFragmentManager().findFragmentById(R.id.goal_container);

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
            /*
            try {
                FileManager.writeGoal(goalFragment.getGoals(), view.getContext());
            }catch (Exception e ){
                Log.e("Goal", e.toString());
            }
            setResult(Activity.RESULT_OK);
            */
            finish();
        }else{
            Toast.makeText(view.getContext(), "¡La suma de tus objetivos debe ser igual a 100%!", Toast.LENGTH_LONG).show();
        }

    }

    public void addClick(View view){
        goalFragment.addClick();
    }

}

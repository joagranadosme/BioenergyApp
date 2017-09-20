package com.procesosyoperaciones.bioenergy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.procesosyoperaciones.bioenergy.data_objects.Goal;
import com.procesosyoperaciones.bioenergy.data_objects.Period;

public class FormActivity extends AppCompatActivity {

    private int size;
    private Goal goal;

    private static String[] perspectiveString = {"Financiera", "Interna", "Aprendizaje"};
    private static String[] unitString = {"Dias", "Unidades"};
    private static String[] periodString = {"Periodo", "Mensual", "Bimestral", "Trimestral", "Semestral", "Anual"};

    private Spinner perspectiveSpinner;
    private Spinner unitSpinner;
    private Spinner periodSpinner;

    private ArrayAdapter<String> perspectiveAdapter;
    private ArrayAdapter<String> unitAdapter;
    private ArrayAdapter<String> periodAdapter;

    private EditText responsabilityEditText;
    private EditText generalEditText;
    private EditText specificEditText;
    private EditText formulaEditText;
    private EditText weightEditText;

    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout goalsLinearLayout;

    private TextView[] goalsTextView;
    private EditText[] goalsEditText;
    private LinearLayout[] goalLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        goal = (Goal) intent.getSerializableExtra("goal");

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

        perspectiveSpinner = (Spinner) findViewById(R.id.perspectiveSpinner);
        perspectiveAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, perspectiveString);
        perspectiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        perspectiveSpinner.setAdapter(perspectiveAdapter);

        unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        unitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitString);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
        periodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periodString);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(periodAdapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(goal.getPeriods() != null){
                    add(goal.getPeriods().length, goal.getPeriod(), goal.getPeriods());
                    goal.setPeriods(null);
                }else{
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem.equals(periodString[1]))
                        add(12, "Mes", new Period[12]);
                    else if (selectedItem.equals(periodString[2]))
                        add(6, "Bimestre", new Period[6]);
                    else if (selectedItem.equals(periodString[3]))
                        add(4, "Trimestre", new Period[4]);
                    else if (selectedItem.equals(periodString[4]))
                        add(2, "Semestre", new Period[2]);
                    else if (selectedItem.equals(periodString[5]))
                        add(1, "Año", new Period[1]);
                    else
                        add(1, "Periodo", new Period[1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        responsabilityEditText = (EditText) findViewById(R.id.respEditText);
        generalEditText = (EditText) findViewById(R.id.generalEditText);
        specificEditText = (EditText) findViewById(R.id.specificEditText);
        formulaEditText = (EditText) findViewById(R.id.formulaEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);

        goalsLinearLayout = (LinearLayout) findViewById(R.id.goalsLinearLayout);

        if(!goal.getPerspective().equals(""))
            perspectiveSpinner.setSelection(perspectiveAdapter.getPosition(goal.getPerspective()));

        if(goal.getUnity() != null)
            unitSpinner.setSelection(unitAdapter.getPosition(goal.getUnity()));

        if(goal.getPeriod() != null)
            periodSpinner.setSelection(periodAdapter.getPosition(goal.getPeriod()));

        if(goal.getDescription() != null)
            responsabilityEditText.setText(goal.getDescription());

        if(goal.getGeneral() != null)
            generalEditText.setText(goal.getGeneral());

        if(goal.getSpecific() != null)
            specificEditText.setText(goal.getSpecific());

        if(goal.getFormula() != null)
            formulaEditText.setText(goal.getFormula());

        weightEditText.setText(goal.getWeight() + "");
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

    public void add(int n, String s, Period[] p){
        size = n;
        goalsLinearLayout.removeAllViews();

        goalLinearLayout = new LinearLayout[n];
        goalsTextView = new TextView[n];
        goalsEditText = new EditText[n];

        for(int i=0; i<n; i++) {
            goalLinearLayout[i] = new LinearLayout(this);
            goalLinearLayout[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            goalLinearLayout[i].setOrientation(LinearLayout.HORIZONTAL);

            goalsTextView[i] = new TextView(this);
            goalsTextView[i].setText(s + " #" + (i+1) + ": ");
            goalsTextView[i].setGravity(Gravity.RIGHT);
            goalsTextView[i].setLayoutParams(layoutParams);
            goalLinearLayout[i].addView(goalsTextView[i]);

            goalsEditText[i] = new EditText(this);
            if(p[i] == null)
                p[i] = new Period();
            goalsEditText[i].setText(p[i].getProposed() + "");
            goalsEditText[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            goalsEditText[i].setLayoutParams(layoutParams);
            goalLinearLayout[i].addView(goalsEditText[i]);

            goalsLinearLayout.addView(goalLinearLayout[i]);
        }
    }

    public void sendClick(View view){

        boolean flag = true;

        String perspective = perspectiveSpinner.getSelectedItem().toString();
        goal.setPerspective(perspective);

        String unit = unitSpinner.getSelectedItem().toString();
        goal.setUnity(unit);

        String period = periodSpinner.getSelectedItem().toString();
        goal.setPeriod(period);

        String description = responsabilityEditText.getText().toString();
        if(description.equals("")){
            Toast.makeText(view.getContext(), "¡La responsabilidad de tu objetivo no puede estar vacía!", Toast.LENGTH_LONG).show();
            return;
        }else{
            goal.setDescription(description);
        }

        String general = generalEditText.getText().toString();
        if(general.equals("")){
            Toast.makeText(view.getContext(), "¡El indicador general de tu objetivo no puede estar vacía!", Toast.LENGTH_LONG).show();
            return;
        }else{
            goal.setGeneral(general);
        }

        String specific = specificEditText.getText().toString();
        if(specific.equals("")){
            Toast.makeText(view.getContext(), "¡El indicador específico de tu objetivo no puede estar vacío!", Toast.LENGTH_LONG).show();
            return;
        }else{
            goal.setSpecific(specific);
        }

        String formula = formulaEditText.getText().toString();
        if(formula.equals("")){
            Toast.makeText(view.getContext(), "¡La formula de tu objetivo no puede estar vacío!", Toast.LENGTH_LONG).show();
            return;
        }else{
            goal.setFormula(formula);
        }

        try{
            int weight;
            weight = Integer.parseInt(weightEditText.getText().toString());
            goal.setWeight(weight);
        }catch (Exception e){
            Toast.makeText(view.getContext(), "¡Debes asignar un porcentaje a tu objetivo!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Period[] periods = new Period[size];
            for(int i=0; i<size; i++) {
                periods[i] = new Period(Integer.parseInt(goalsEditText[i].getText().toString()));
            }
            goal.setPeriods(periods);
        }catch (Exception e){
            Toast.makeText(view.getContext(), "¡Debes asignar una meta para cada periodo!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("goal", goal);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}

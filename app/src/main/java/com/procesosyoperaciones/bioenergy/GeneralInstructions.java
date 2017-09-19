package com.procesosyoperaciones.bioenergy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;

public class GeneralInstructions extends AppCompatActivity {

    private static final int SELECT_BOSS = 0;
    private static final int CREATE_GOAL = 1;
    private static final int TRACING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_instructions);

        DocumentView content = (DocumentView) findViewById(R.id.instruccionesDocumentView);
        content.setText(getResources().getText(R.string.general_instructions_text));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void unoClick(View view) {
        Intent intent = new Intent(this, Instructions.class);
        intent.putExtra("id", 0);
        startActivityForResult(intent, SELECT_BOSS);
    }

    public void dosClick(View view) {
        Intent intent = new Intent(this, Instructions.class);
        intent.putExtra("id", 1);
        startActivityForResult(intent, CREATE_GOAL);
    }

    public void tresClick(View view) {
        Intent intent = new Intent(this, Instructions.class);
        intent.putExtra("id", 2);
        startActivity(intent);
    }

    public void cuatroClick(View view) {
        Intent intent = new Intent(this, Instructions.class);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_BOSS && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "¡Jefe seleecionado correctamente!", Toast.LENGTH_LONG).show();
        }
        if (requestCode == CREATE_GOAL && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "¡Objetivos creados correctamente!", Toast.LENGTH_LONG).show();
        }
        if (requestCode == TRACING && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "¡Seguimiento realizado correctamente!", Toast.LENGTH_LONG).show();
        }
    }

}

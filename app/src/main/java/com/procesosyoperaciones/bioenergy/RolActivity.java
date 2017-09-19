package com.procesosyoperaciones.bioenergy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.JustifiedSpan;
import com.bluejamesbond.text.util.ArticleBuilder;

public class RolActivity extends AppCompatActivity {

    ArticleBuilder articleBuilder;
    DocumentView documentView;
    Button evaluatedButton;
    Button evaluatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol);

        //Get info.
        Bundle bundle = getIntent().getExtras();
        String idUser = bundle.getString("idUser");
        String idRol = bundle.getString("idRol");
        String name = bundle.getString("name");

        evaluatedButton = (Button) findViewById(R.id.evaluatedButton);
        evaluatorButton = (Button) findViewById(R.id.evaluatorButton);
        documentView = (DocumentView) findViewById(R.id.instruccionesDocumentView);

        if(idRol.equals("1"))
            evaluatedButton.setVisibility(View.GONE);
        if(idRol.equals("2"))
            evaluatorButton.setVisibility(View.GONE);

        documentView.setText("Apreciado " + name + ", bienvenido al sistema de gestión de desempeño." + getResources().getText(R.string.restrictions));

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

    public void evaluatorClick(View view){

    }

    public void evaluatedClick(View view){
        Intent intent = new Intent(this, GeneralInstructions.class);
        startActivity(intent);
    }

}

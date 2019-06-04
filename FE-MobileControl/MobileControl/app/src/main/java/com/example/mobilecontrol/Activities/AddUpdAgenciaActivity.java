package com.example.mobilecontrol.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilecontrol.LogicaNegocio.Agencia;
import com.example.mobilecontrol.R;

public class AddUpdAgenciaActivity extends AppCompatActivity {
    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText nomFld;
    private EditText codFld;
    private EditText emailFld;
    private EditText telFld;
    private EditText ubiFld;
    private EditText horFld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_agencia);
        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdAgenciaBtn);

        //cleaning stuff
        codFld = findViewById(R.id.codigoAddUpdAgencia);
        emailFld = findViewById(R.id.emailAddUpdAgencia);
        telFld=findViewById(R.id.telefonoAddUpdAgencia);
        nomFld=findViewById(R.id.nombreAddUpdAgencia);
        horFld=findViewById(R.id.horarioAddUpdAgencia);
        ubiFld=findViewById(R.id.ubicacionAddUpdAgencia);
        nomFld.setText("");
        codFld.setText("");
        emailFld.setText("");
        telFld.setText("");
        horFld.setText("");
        ubiFld.setText("");

        //receiving data from admAlumnoActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Agencia aux = (Agencia) getIntent().getSerializableExtra("agencia");
                codFld.setText(aux.getCodigo());
                codFld.setEnabled(false);
                nomFld.setText(aux.getNombre());
                emailFld.setText(aux.getEmail());
                telFld.setText(aux.getTelefono());
                ubiFld.setText(aux.getUbicacion());
                horFld.setText(aux.getHorario());
                //edit action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editAgencia();
                    }
                });
            } else {         // is adding new Carrera object
                //add new action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addAgencia();
                    }
                });
            }
        }
    }

    public void addAgencia() {
        if (validateForm()) {
            //do something
            Agencia agencia = new Agencia(codFld.getText().toString(), nomFld.getText().toString(),
                    telFld.getText().toString(),
                    emailFld.getText().toString(),ubiFld.getText().toString(),horFld.getText().toString());
            Intent intent = new Intent(getBaseContext(), AdmAgenciaActivity.class);
            //sending Alumno data
            intent.putExtra("addAgencia", agencia);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editAgencia() {
        if (validateForm()) {
            Agencia agencia = new Agencia(codFld.getText().toString(), nomFld.getText().toString(),
                    telFld.getText().toString(),
                    emailFld.getText().toString(),ubiFld.getText().toString(),horFld.getText().toString());
            Intent intent = new Intent(getBaseContext(), AdmAgenciaActivity.class);
            //sending Alumno data
            intent.putExtra("editAgencia", agencia);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.nomFld.getText())) {
            nomFld.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.codFld.getText())) {
            codFld.setError("Codigo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.emailFld.getText())) {
            emailFld.setError("Email requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.telFld.getText())) {
            telFld.setError("Telefono requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.ubiFld.getText())) {
            telFld.setError("Ubicacion requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.horFld.getText())) {
            telFld.setError("Horario requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

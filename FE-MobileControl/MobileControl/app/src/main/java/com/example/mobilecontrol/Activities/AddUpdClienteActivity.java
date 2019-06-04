package com.example.mobilecontrol.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilecontrol.LogicaNegocio.Cliente;
import com.example.mobilecontrol.R;

public class AddUpdClienteActivity extends AppCompatActivity {
    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText nomFld;
    private EditText cedFld;
    private EditText emailFld;
    private EditText telFld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_cliente);
        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdClienteBtn);

        //cleaning stuff
        cedFld = findViewById(R.id.cedulaAddUpdCliente);
        emailFld = findViewById(R.id.emailAddUpdCliente);
        telFld=findViewById(R.id.telefonoAddUpdCliente);
        nomFld=findViewById(R.id.nombreAddUpdCliente);
        nomFld.setText("");
        cedFld.setText("");
        emailFld.setText("");
        telFld.setText("");

        //receiving data from admAlumnoActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Cliente aux = (Cliente) getIntent().getSerializableExtra("cliente");
                cedFld.setText(aux.getCedula());
                cedFld.setEnabled(false);
                nomFld.setText(aux.getNombre());
                emailFld.setText(aux.getEmail());
                telFld.setText(aux.getTelefono());
                //edit action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editCliente();
                    }
                });
            } else {         // is adding new Carrera object
                //add new action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCliente();
                    }
                });
            }
        }
    }

    public void addCliente() {
        if (validateForm()) {
            //do something
            Cliente cliente = new Cliente(cedFld.getText().toString(), nomFld.getText().toString(),
                    telFld.getText().toString(),
                    emailFld.getText().toString());
            Intent intent = new Intent(getBaseContext(), AdmClienteActivity.class);
            //sending Alumno data
            intent.putExtra("addCliente", cliente);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editCliente() {
        if (validateForm()) {
            Cliente cliente = new Cliente(cedFld.getText().toString(), nomFld.getText().toString(),
                    telFld.getText().toString(),
                    emailFld.getText().toString());
            Intent intent = new Intent(getBaseContext(), AdmClienteActivity.class);
            //sending Alumno data
            intent.putExtra("editCliente", cliente);
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
        if (TextUtils.isEmpty(this.cedFld.getText())) {
            cedFld.setError("Cedula requerida");
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
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

package com.example.mobilecontrol.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobilecontrol.LogicaNegocio.Agencia;
import com.example.mobilecontrol.LogicaNegocio.Cliente;
import com.example.mobilecontrol.LogicaNegocio.Reserva;
import com.example.mobilecontrol.LogicaNegocio.Vehiculo;
import com.example.mobilecontrol.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddUpdReservaActivity extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText codFld, fechainicioFld, fechafinalFld;
    private Spinner agencias, clientes, vehiculos;
    private List<Agencia> agenciaList;
    private List<Cliente> clienteList;
    private List<Vehiculo> vehiculoList;

    String apiUrl = "http://10.20.106.109:8080/BE-LabConnection/ServiceReserva?";
    String apiUrlTemporal = "";
    String apiUrlTemporal2 = "";
    String apiUrlTemporal3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_reserva);
        editable=true;
        fBtn = findViewById(R.id.addUpdReservaBtn);

        codFld = findViewById(R.id.codigoAddUpdReserva);
        fechainicioFld = findViewById(R.id.fechainicioTextDate);
        fechafinalFld = findViewById(R.id.fechafinalTextDate);
        codFld.setText("");
        fechainicioFld.setText("");
        fechafinalFld.setText("");
        agencias = (Spinner)findViewById(R.id.sp_agencias);
        clientes = (Spinner)findViewById(R.id.sp_clientes);
        vehiculos = (Spinner)findViewById(R.id.sp_vehiculos);
        //agencias
        apiUrlTemporal=apiUrl+"opc=5";
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();
        //clientes
        apiUrlTemporal2=apiUrl+"opc=6";
        MyAsyncTasks2 myAsyncTasks2 = new MyAsyncTasks2();
        myAsyncTasks2.execute();
        //vehiculos
        apiUrlTemporal3=apiUrl+"opc=7";
        MyAsyncTasks3 myAsyncTasks3 = new MyAsyncTasks3();
        myAsyncTasks3.execute();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            editable = extras.getBoolean("editable");
            if(editable){
                Reserva aux = (Reserva) getIntent().getSerializableExtra("reserva");
                codFld.setText(aux.getCodigo());
                codFld.setEnabled(false);
                fechainicioFld.setText(aux.getFecha_inicio());
                fechafinalFld.setText(aux.getFecha_final());
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editReserva();
                    }
                });
            }else{
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addReserva();
                    }
                });
            }
        }
    }

    public void addReserva() {
        if (validateForm()) {
            //do something
            Agencia age = buscarAgencia(agenciaList, (Agencia) agencias.getSelectedItem());
            Cliente cli = buscarCliente(clienteList, (Cliente) clientes.getSelectedItem());
            Vehiculo veh = buscarVehiculo(vehiculoList, (Vehiculo)vehiculos.getSelectedItem());
            Reserva reserva = new Reserva(codFld.getText().toString(), age, cli, veh, fechainicioFld.getText().toString(), fechafinalFld.getText().toString());
            Intent intent = new Intent(getBaseContext(), AdmReservaActivity.class);
            //sending curso data
            intent.putExtra("addReserva", reserva);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editReserva() {
        if (validateForm()) {
            Agencia age = buscarAgencia(agenciaList, (Agencia) agencias.getSelectedItem());
            Cliente cli = buscarCliente(clienteList, (Cliente) clientes.getSelectedItem());
            Vehiculo veh = buscarVehiculo(vehiculoList, (Vehiculo)vehiculos.getSelectedItem());
            Reserva reserva = new Reserva(codFld.getText().toString(), age, cli, veh, fechainicioFld.getText().toString(), fechafinalFld.getText().toString());
            Intent intent = new Intent(getBaseContext(), AdmReservaActivity.class);
            //sending curso data
            intent.putExtra("editReserva", reserva);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.codFld.getText())) {
            codFld.setError("Codigo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.fechainicioFld.getText())) {
            fechainicioFld.setError("Fecha de Inicio requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.fechafinalFld.getText())) {
            fechafinalFld.setError("Fecha de Final requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public Agencia buscarAgencia(List<Agencia> agenciaList, Agencia agencia){
        for (Agencia c : agenciaList) {
            if (c.getCodigo().equals(agencia.getCodigo())) {
                return c;
            }
        }
        return null;
    }

    public Cliente buscarCliente(List<Cliente> clienteList, Cliente cliente){
        for (Cliente c : clienteList) {
            if (c.getCedula().equals(cliente.getCedula())) {
                return c;
            }
        }
        return null;
    }

    public Vehiculo buscarVehiculo(List<Vehiculo> vehiculoList, Vehiculo vehiculo){
        for (Vehiculo c : vehiculoList) {
            if (c.getCodigo().equals(vehiculo.getCodigo())) {
                return c;
            }
        }
        return null;
    }

    private void loadAgencias(List agenciaList) {
        // im not sure about this
        ArrayAdapter<Agencia> adapter = new ArrayAdapter<Agencia>(this, R.layout.support_simple_spinner_dropdown_item, agenciaList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        agencias.setAdapter(adapter);
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {

            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemporal);
                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }
                    // return the data to onPostExecute method
                    Log.w("JSON", current);
                    return current;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String s) {
            //S tiene la lista Actualizada que recibe del web service
            //Se actualiza el recycler view
            try {
                Gson gson = new Gson();
                ArrayList<Agencia> AgenciaList= (ArrayList<Agencia>) gson.fromJson(s,
                        new TypeToken<ArrayList<Agencia>>() {
                        }.getType());
                agenciaList = AgenciaList;
                loadAgencias(agenciaList);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadClientes(List clienteList) {
        // im not sure about this
        ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(this, R.layout.support_simple_spinner_dropdown_item, clienteList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        clientes.setAdapter(adapter);
    }

    public class MyAsyncTasks2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {

            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemporal);
                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }
                    // return the data to onPostExecute method
                    Log.w("JSON", current);
                    return current;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String s) {
            //S tiene la lista Actualizada que recibe del web service
            //Se actualiza el recycler view
            try {
                Gson gson = new Gson();
                ArrayList<Cliente> ClienteList= (ArrayList<Cliente>) gson.fromJson(s,
                        new TypeToken<ArrayList<Cliente>>() {
                        }.getType());
                clienteList = ClienteList;
                loadClientes(clienteList);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadVehiculos(List vehiculoList) {
        // im not sure about this
        ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<Vehiculo>(this, R.layout.support_simple_spinner_dropdown_item, vehiculoList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        vehiculos.setAdapter(adapter);
    }

    public class MyAsyncTasks3 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {

            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemporal);
                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }
                    // return the data to onPostExecute method
                    Log.w("JSON", current);
                    return current;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String s) {
            //S tiene la lista Actualizada que recibe del web service
            //Se actualiza el recycler view
            try {
                Gson gson = new Gson();
                ArrayList<Vehiculo> VehiculoList= (ArrayList<Vehiculo>) gson.fromJson(s,
                        new TypeToken<ArrayList<Vehiculo>>() {
                        }.getType());
                vehiculoList = VehiculoList;
                loadVehiculos(vehiculoList);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

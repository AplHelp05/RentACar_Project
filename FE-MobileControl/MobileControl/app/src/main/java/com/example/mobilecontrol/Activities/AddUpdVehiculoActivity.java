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

import com.example.mobilecontrol.LogicaNegocio.Clasificacion;
import com.example.mobilecontrol.LogicaNegocio.Marca;
import com.example.mobilecontrol.LogicaNegocio.Transmision;
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


public class AddUpdVehiculoActivity extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private boolean editable = true;

    private EditText codFld;
    private EditText capacidadFld;
    private EditText placaFld;
    private EditText descripcionFld;
    private EditText precioFld;
    private Spinner clasificaciones;
    private Spinner transmisiones;
    private Spinner marcas;

    private List<Clasificacion> clasificacionList;
    private List<Transmision> transmisionList;
    private List<Marca> marcaList;

    String apiUrl = "http://192.168.1.5:9090/BE-LabConnection/ServiceVehiculo?";
    String apiUrlTemporal = "";
    String apiUrlTemporal2 = "";
    String apiUrlTemporal3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_vehiculo);
        editable = true;

        fBtn = findViewById(R.id.addUpdVehiculoBtn);

        codFld = findViewById(R.id.codigoAddUpdVehiculo);

        clasificaciones = (Spinner) findViewById(R.id.sp_clasificacion);
        transmisiones = (Spinner)findViewById(R.id.sp_transmision);
        marcas = (Spinner)findViewById(R.id.sp_marca);

        capacidadFld = findViewById(R.id.capacidadAddUpdVehiculo);
        placaFld = findViewById(R.id.placaAddUpdVehiculo);
        descripcionFld = findViewById(R.id.descripcionAddUpdVehiculo);
        precioFld = findViewById(R.id.precioAddUpdVehiculo);

        codFld.setText("");
        capacidadFld.setText("");
        placaFld.setText("");
        descripcionFld.setText("");
        precioFld.setText("");
//Clasificaciones
        apiUrlTemporal = apiUrl+"opc=5";
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();
//transmisiones
        apiUrlTemporal2 = apiUrl+"opc=6";
        MyAsyncTasks2 myAsyncTasks2 = new MyAsyncTasks2();
        myAsyncTasks2.execute();
//marcas
        apiUrlTemporal3 = apiUrl+"opc=7";
        MyAsyncTasks3 myAsyncTasks3 = new MyAsyncTasks3();
        myAsyncTasks3.execute();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            editable = extras.getBoolean("editable");
            if(editable){
                Vehiculo aux = (Vehiculo)getIntent().getSerializableExtra("vehiculo");
                codFld.setText(aux.getCodigo());
                codFld.setEnabled(false);
                capacidadFld.setText(aux.getCapacidad());
                placaFld.setText(aux.getPlaca());
                descripcionFld.setText(aux.getDescripcion());
                precioFld.setText(aux.getPrecio());
                fBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        editVehiculo();
                    }
                });
            }else{
                fBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        addVehiculo();
                    }
                });
            }
        }
    }

    public void addVehiculo() {
        if (validateForm()) {
            //do something
            Clasificacion cla = buscarClasificacion(clasificacionList, (Clasificacion)clasificaciones.getSelectedItem());
            Transmision tra = buscarTransmision(transmisionList, (Transmision)transmisiones.getSelectedItem());
            Marca mar = buscarMarca(marcaList, (Marca)marcas.getSelectedItem());
            int cap = Integer.parseInt(capacidadFld.getText().toString());
            int pre = Integer.parseInt(precioFld.getText().toString());
            Vehiculo veh = new Vehiculo(codFld.getText().toString(), cla, tra, mar, cap, placaFld.getText().toString(), descripcionFld.getText().toString(), pre);
            Intent intent = new Intent(getBaseContext(), AdmVehiculoActivity.class);
            //sending curso data
            intent.putExtra("addVehiculo", veh);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editVehiculo() {
        if (validateForm()) {
            //do something
            Clasificacion cla = buscarClasificacion(clasificacionList, (Clasificacion)clasificaciones.getSelectedItem());
            Transmision tra = buscarTransmision(transmisionList, (Transmision)transmisiones.getSelectedItem());
            Marca mar = buscarMarca(marcaList, (Marca)marcas.getSelectedItem());
            int cap = Integer.parseInt(capacidadFld.getText().toString());
            int pre = Integer.parseInt(precioFld.getText().toString());
            Vehiculo veh = new Vehiculo(codFld.getText().toString(), cla, tra, mar, cap, placaFld.getText().toString(), descripcionFld.getText().toString(), pre);
            Intent intent = new Intent(getBaseContext(), AdmVehiculoActivity.class);
            //sending curso data
            intent.putExtra("editVehiculo", veh);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public Clasificacion buscarClasificacion(List<Clasificacion> clasificacionList, Clasificacion clasificacion){
        for (Clasificacion c : clasificacionList) {
            if (c.getCodigo().equals(clasificacion.getCodigo())) {
                return c;
            }
        }
        return null;
    }

    public Transmision buscarTransmision(List<Transmision> transmisionList, Transmision transmision){
        for (Transmision t : transmisionList) {
            if (t.getCodigo().equals(transmision.getCodigo())) {
                return t;
            }
        }
        return null;
    }

    public Marca buscarMarca(List<Marca> marcaList, Marca marca){
        for (Marca m : marcaList) {
            if (m.getCodigo().equals(marca.getCodigo())) {
                return m;
            }
        }
        return null;
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.codFld.getText())) {
            codFld.setError("Codigo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.capacidadFld.getText())) {
            capacidadFld.setError("Debe ingresar la Capacidad del vehículo");
            error++;
        }
        if (TextUtils.isEmpty(this.placaFld.getText())) {
            placaFld.setError("Debe ingresar la Placa del vehículo");
            error++;
        }
        if (TextUtils.isEmpty(this.descripcionFld.getText())) {
            descripcionFld.setError("Debe ingresar la Descripción del vehículo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.precioFld.getText())) {
            precioFld.setError("Debe ingresar el Precio de la renta");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Existen algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void loadClasificaciones(List clasificacionList) {
        // im not sure about this
        ArrayAdapter<Clasificacion> adapter = new ArrayAdapter<Clasificacion>(this, R.layout.support_simple_spinner_dropdown_item, clasificacionList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        clasificaciones.setAdapter(adapter);
    }

    private void loadTransmisiones(List transmisionList) {
        // im not sure about this
        ArrayAdapter<Transmision> adapter = new ArrayAdapter<Transmision>(this, R.layout.support_simple_spinner_dropdown_item, transmisionList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        transmisiones.setAdapter(adapter);
    }

    private void loadMarcas(List marcaList) {
        // im not sure about this
        ArrayAdapter<Marca> adapter = new ArrayAdapter<Marca>(this, R.layout.support_simple_spinner_dropdown_item, marcaList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        marcas.setAdapter(adapter);
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
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
                ArrayList<Clasificacion> ClasificacionList= (ArrayList<Clasificacion>) gson.fromJson(s,
                        new TypeToken<ArrayList<Clasificacion>>() {
                        }.getType());

                clasificacionList = ClasificacionList;
                loadClasificaciones(clasificacionList);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyAsyncTasks2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
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
                ArrayList<Transmision> TransmisionList= (ArrayList<Transmision>) gson.fromJson(s,
                        new TypeToken<ArrayList<Transmision>>() {
                        }.getType());


                transmisionList = TransmisionList;
                loadTransmisiones(transmisionList);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyAsyncTasks3 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
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
                ArrayList<Marca> MarcaList= (ArrayList<Marca>) gson.fromJson(s,
                        new TypeToken<ArrayList<Marca>>() {
                        }.getType());


                marcaList = MarcaList;
                loadMarcas(marcaList);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

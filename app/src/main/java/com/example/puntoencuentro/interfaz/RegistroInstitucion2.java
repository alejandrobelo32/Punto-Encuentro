package com.example.puntoencuentro.interfaz;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.puntoencuentro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroInstitucion2 extends AppCompatActivity {

    Bundle bundle;
    Button btnAgregarInst;
    Spinner spinnerPaises, spinnerProvincias, spinnerPartidos, spinnerLocalidades;
    String mail;
    EditText edtCalle, edtAltura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_institucion2);

        btnAgregarInst = findViewById(R.id.btnAgregarInst);
        spinnerPaises = findViewById(R.id.spinnerPaises);
        spinnerProvincias = findViewById(R.id.spinnerProvincias);
        spinnerPartidos = findViewById(R.id.spinnerPartidos);
        spinnerLocalidades = findViewById(R.id.spinnerLocalidades);
        edtCalle = findViewById(R.id.edtCalle);
        edtAltura = findViewById(R.id.edtAltura);

        bundle = getIntent().getExtras();
        mail = bundle.getString("usuarioInstitucion");

        traerDatos("http://"+getString(R.string.ip)+"/puntoencuentro/institucion/buscar_paises.php", spinnerPaises);

        btnAgregarInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrar( "http://"+getString(R.string.ip)+"/puntoencuentro/institucion/updatearIngreso.php");

            }
        });

        spinnerPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                //int index = arg0.getSelectedItemPosition();

                traerDatos("http://"+getString(R.string.ip)+"/puntoencuentro/institucion/buscar_provincias.php?nombrePais="+spinnerPaises.getSelectedItem()+"", spinnerProvincias);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                Toast.makeText(getApplicationContext(),"No selection",Toast.LENGTH_LONG).show();

            }
        });

        spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                traerDatos("http://"+getString(R.string.ip)+"/puntoencuentro/institucion/buscar_partidos.php?nombreProvincia="+spinnerProvincias.getSelectedItem()+"", spinnerPartidos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                Toast.makeText(getApplicationContext(),"No selection",Toast.LENGTH_LONG).show();

            }
        });

        spinnerPartidos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                traerDatos("http://"+getString(R.string.ip)+"/puntoencuentro/institucion/buscar_localidades.php?nombrePartido="+spinnerPartidos.getSelectedItem()+"", spinnerLocalidades);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                Toast.makeText(getApplicationContext(),"No selection",Toast.LENGTH_LONG).show();

            }
        });

          }

    private void traerDatos(String URL, final Spinner spinner) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                List<String> listaSpinner = new ArrayList<String>();

                for (int i = 0;i <response.length();i++) {

                    try {

                        jsonObject = response.getJSONObject(i);

                        listaSpinner.add(jsonObject.getString("nombre"));
                        //id_pais= jsonObject.getString("id_pais");

                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaSpinner);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION"+error.getMessage(),Toast.LENGTH_SHORT).show();
                spinner.setAdapter(null);

            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void registrar(String URL ){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA"+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the request has either time out or there is no connection
                    Toast.makeText(getApplicationContext(), "SERVIDOR NO DISPONIBLE"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    //Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getApplicationContext(), "ERROR DE CONEXION 2"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getApplicationContext(), "ERROR DE CONEXION 3"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getApplicationContext(), "ERROR DE CONEXION 4"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getApplicationContext(), "USUARIO O CONTRASEÑA INCORRECTOS"+error.getMessage(),Toast.LENGTH_SHORT).show();

                }
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                parametros.put("pais", spinnerPaises.getSelectedItem().toString());
                parametros.put("provincia",spinnerProvincias.getSelectedItem().toString());
                parametros.put("partido",spinnerPartidos.getSelectedItem().toString());
                parametros.put("localidad",spinnerLocalidades.getSelectedItem().toString());
                parametros.put("calle",edtCalle.getText().toString());
                parametros.put("altura",edtAltura.getText().toString());
                parametros.put("usuario",mail);
                return parametros;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}



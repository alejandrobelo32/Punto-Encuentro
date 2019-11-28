package com.example.puntoencuentro.interfaz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.example.puntoencuentro.R;
import com.example.puntoencuentro.entidad.Institucion;
import com.example.puntoencuentro.entidad.Usuario;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public Usuario usuario = null;
    private Institucion institucion= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i= new Intent(MainActivity.this, Ingresar.class);
        startActivityForResult(i,1);

     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == Activity.RESULT_OK){

                usuario = (Usuario) data.getExtras().getSerializable("usuario");

                Toast.makeText(getApplicationContext(),"Bienvenido "+usuario.getMail(), Toast.LENGTH_SHORT).show();

                Intent i= new Intent(MainActivity.this, MapsActivity.class);

                Bundle bundle= new Bundle();

                bundle.putSerializable("usuario", usuario);

                i.putExtras(bundle);

                startActivityForResult(i,2);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {

            if(resultCode == Activity.RESULT_OK){

                Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivityForResult(i, 3);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 3) {
            if (data != null) {

                String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

                buscarInstitucionPorQR("http://"+getString(R.string.ip)+"/puntoencuentro/institucion/buscar_qr.php?codigo_qr="+lectura);



            }
        }

    }//onActivityResult

    private void buscarInstitucionPorQR(String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0;i <response.length();i++) {

                    try {

                        jsonObject = response.getJSONObject(i);

                        institucion = new Institucion(jsonObject.getInt("id_institucion"), jsonObject.getString("nombre_inst"), jsonObject.getString("cuit"), new LatLng(-34.670044, -58.362761));

                        confirmarInstitucion(new View(getApplicationContext()));

                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
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
                    Toast.makeText(getApplicationContext(), "El codigo QR no corresponde a una institución"+error.getMessage(),Toast.LENGTH_SHORT).show();

                    Intent i= new Intent(MainActivity.this, MapsActivity.class);

                    Bundle bundle= new Bundle();

                    bundle.putSerializable("usuario", usuario);

                    i.putExtras(bundle);

                    startActivityForResult(i,2);

                }

            }
        }
        );

        RequestQueue requestQueue= Volley.newRequestQueue( this);
        requestQueue.add(jsonArrayRequest);

    }

    private void agregarUsuarioInstitucion(String URL){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "El usuario se ha agregado con éxito"+response, Toast.LENGTH_SHORT).show();


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
                parametros.put("id_institucion", Integer.toString(institucion.getId()) );
                parametros.put("id_usuario",Integer.toString(usuario.getId()));
                return parametros;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue( this);
        requestQueue.add(stringRequest);

    }

    public void confirmarInstitucion(View view){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("¿Desea unirse a esta institución?");

        alertDialogBuilder.setMessage(institucion.getNombre() + "\n" + institucion.getCuit());

        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface arg0, int arg1){

                //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                agregarUsuarioInstitucion("http://" + getString(R.string.ip) + "/puntoencuentro/institucion/agregar_usuario.php");

            }

        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
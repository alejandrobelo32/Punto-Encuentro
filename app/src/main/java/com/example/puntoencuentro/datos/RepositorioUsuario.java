package com.example.puntoencuentro.datos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
import com.example.puntoencuentro.entidad.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RepositorioUsuario {

    Context context;
    
    private Usuario ingresar(String URL,Context context2)throws VolleyError{

        this.context= context2;

        Usuario usuario;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0;i <response.length();i++) {

                    try {

                        jsonObject = response.getJSONObject(i);

                        Usuario usuario =  new Usuario(1,jsonObject.getString("mail"), jsonObject.getString("nombre"), jsonObject.getString("apellido"), jsonObject.getString("contrasena"), jsonObject.getInt("habilitado")==1 );



                    } catch (JSONException e) {

                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the request has either time out or there is no connection
                    Toast.makeText(context, "SERVIDOR NO DISPONIBLE"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    //Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(context, "ERROR DE CONEXION 2"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(context, "ERROR DE CONEXION 3"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(context, "ERROR DE CONEXION 4"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(context, "USUARIO O CONTRASEÑA INCORRECTOS"+error.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        }
        );

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        return null;

    }

    private void registrar(String URL ){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "OPERACION EXITOSA"+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the request has either time out or there is no connection
                    Toast.makeText(context, "SERVIDOR NO DISPONIBLE"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    //Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(context, "ERROR DE CONEXION 2"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(context, "ERROR DE CONEXION 3"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(context, "ERROR DE CONEXION 4"+error.getMessage(),Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(context, "USUARIO O CONTRASEÑA INCORRECTOS"+error.getMessage(),Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                /*parametros.put("usuario", edtUsuario.getText().toString());
                parametros.put("password",edtPass.getText().toString());
                parametros.put("dni",edtDNI.getText().toString());
                parametros.put("nombre",edtNombre.getText().toString());
                parametros.put("apellido",edtApellido.getText().toString());*/
                return parametros;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue( context);
        requestQueue.add(stringRequest);
    }

}

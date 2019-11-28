package com.example.puntoencuentro.interfaz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.puntoencuentro.R;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity {

    EditText edtUsuario, edtNombre, edtPass, edtApellido, edtDNI;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtNombre = findViewById(R.id.edtNombre);
        edtPass = findViewById(R.id.edtPass);
        edtApellido = findViewById(R.id.edtApellido);
        edtDNI = findViewById(R.id.edtDNI);
        btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrar( "http://"+getString(R.string.ip)+"/puntoencuentro/usuario/insertar.php");

            }
        });

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
                parametros.put("usuario", edtUsuario.getText().toString());
                parametros.put("password",edtPass.getText().toString());
                parametros.put("dni",edtDNI.getText().toString());
                parametros.put("nombre",edtNombre.getText().toString());
                parametros.put("apellido",edtApellido.getText().toString());
                return parametros;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue( this);
        requestQueue.add(stringRequest);
    }


}

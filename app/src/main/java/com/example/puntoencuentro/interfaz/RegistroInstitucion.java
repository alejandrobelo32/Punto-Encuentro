package com.example.puntoencuentro.interfaz;

import android.content.Intent;
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


public class RegistroInstitucion extends AppCompatActivity {

    EditText edtUsuarioInst, edtNombreInst, edtPassInst, edtCuit;
    Button btnAgregarInst, btnSiguiente;

    String mailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_institucion);

        edtUsuarioInst = findViewById(R.id.edtUsuarioInst);
        edtNombreInst = findViewById(R.id.edtNombreInst);
        edtPassInst = findViewById(R.id.edtPassInst);
        edtCuit = findViewById(R.id.edtCuit);
        btnAgregarInst = findViewById(R.id.btnAgregarInst);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        btnAgregarInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrar( "http://"+getString(R.string.ip)+"/puntoencuentro/institucion/insertar.php");

            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i= new Intent(RegistroInstitucion.this, RegistroInstitucion2.class);
               i.putExtra("usuarioInstitucion",edtUsuarioInst.getText().toString());
               startActivity(i);

            }
        });

    }

    private void registrar(String URL ){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA"+response, Toast.LENGTH_SHORT).show();
                mailUsuario = edtUsuarioInst.getText().toString();
                Intent i = new Intent(RegistroInstitucion.this,RegistroInstitucion2.class);
                i.putExtra("bundle",mailUsuario);
                startActivity(i);
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
                    Toast.makeText(getApplicationContext(), "USUARIO O CONTRASEÑA INCORRECTOS" + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                parametros.put("usuario", edtUsuarioInst.getText().toString());
                parametros.put("password",edtPassInst.getText().toString());
                parametros.put("cuit",edtCuit.getText().toString());
                parametros.put("nombre",edtNombreInst.getText().toString());
                return parametros;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

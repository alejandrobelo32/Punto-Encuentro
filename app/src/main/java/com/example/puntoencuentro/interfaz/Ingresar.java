package com.example.puntoencuentro.interfaz;

import android.app.Activity;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.puntoencuentro.R;
import com.example.puntoencuentro.entidad.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ingresar extends AppCompatActivity {

    EditText edtUsuario, edtPass;
    Button btnIngresar, btnRegistrar, btnRegistrarInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);

        getSupportActionBar().hide();

        edtPass = findViewById(R.id.edtPass);
        edtUsuario = findViewById(R.id.edtUsuario);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrarInst = findViewById(R.id.btnRegistrarInst);

        btnIngresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ingresar("http://"+getString(R.string.ip)+"/puntoencuentro/usuario/buscar.php?usuario="+edtUsuario.getText()+"&contrasena="+edtPass.getText()+"");

            }

        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(Ingresar.this, RegistroUsuario.class);
                startActivity(i);

            }
        });

        btnRegistrarInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(Ingresar.this, RegistroInstitucion.class);
                startActivity(i);

            }
        });

    }

    private void ingresar(String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

            JSONObject jsonObject = null;

            for (int i = 0;i <response.length();i++) {

                try {

                    jsonObject = response.getJSONObject(i);

                    Intent returnIntent = new Intent();

                    Bundle bundle= new Bundle();

                    Usuario usuario = new Usuario(jsonObject.getInt("id_usuario"),jsonObject.getString("mail"), jsonObject.getString("nombre"), jsonObject.getString("apellido"), jsonObject.getString("contrasena"), !(jsonObject.getInt("habilitado")==0) );

                    bundle.putSerializable("usuario", usuario);

                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                    //Intent inte= new Intent(Ingresar.this, MapsActivity.class);
                    //startActivity(inte);

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
                    Toast.makeText(getApplicationContext(), "USUARIO O CONTRASEÑA INCORRECTOS"+error.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        }
        );

        RequestQueue requestQueue= Volley.newRequestQueue( this);
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }

}

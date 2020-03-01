package com.example.puntoencuentro.interfaz;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.puntoencuentro.R;
import com.example.puntoencuentro.entidad.Institucion;
import com.example.puntoencuentro.entidad.Ruta;
import com.example.puntoencuentro.entidad.Usuario;
import com.example.puntoencuentro.mapsnearbyplaces.DataParser;
import com.example.puntoencuentro.mapsnearbyplaces.GetDirectionsData;
import com.example.puntoencuentro.mapsnearbyplaces.GetDirectionsData.AsyncResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.PolyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerDragListener, AdapterView.OnItemClickListener {

  private GoogleMap mMap;
  private Button btnLectorQR;
  private Button btnGuardarRuta;
  private Button btnUnir;
  private Button btnCancelar;

  private List<Institucion> instituciones;

  private ListView listViewRutas;
  List<String> listaIformacionRutas;
  ArrayAdapter ADP;

  private BottomSheetBehavior bsbInformacionInstitucion, bsbCrearRuta;
  private View bsInformacionInstitucion;

  GoogleApiClient mGoogleApiClient;
  Location mLastLocation;
  Marker mCurrLocationMarker;
  LocationRequest mLocationRequest;
  int PROXIMITY_RADIUS = 10000;

  public Polyline polylineRuta;
  public Marker markerDestino;

  private Institucion institucionSeleccionada;


  private Usuario usuario;
  private Ruta rutaActual;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    instituciones = new ArrayList<>();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    bsInformacionInstitucion = findViewById(R.id.bottom_sheet_informacion_institucion);
    bsbInformacionInstitucion = BottomSheetBehavior.from(bsInformacionInstitucion);
    bsbInformacionInstitucion.setPeekHeight(200);
    bsbInformacionInstitucion.setHideable(true);
    bsbInformacionInstitucion.setState(BottomSheetBehavior.STATE_HIDDEN);

    View bsCrearRuta = findViewById(R.id.bottom_sheet_crear_ruta);
    bsbCrearRuta = BottomSheetBehavior.from(bsCrearRuta);
    bsbCrearRuta.setPeekHeight(200);
    bsbCrearRuta.setHideable(true);
    bsbCrearRuta.setState(BottomSheetBehavior.STATE_HIDDEN);

    listViewRutas = findViewById(R.id.listViewRutas);
    listaIformacionRutas = new ArrayList<>();

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      checkLocationPermission();
    }

    //Check if Google Play Services Available or not
    if (!CheckGooglePlayServices()) {
      Log.d("onCreate", "Finishing test case since Google Play Services are not available");
      finish();
    } else {
      Log.d("onCreate", "Google Play Services available.");
    }

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    btnLectorQR = findViewById(R.id.btnLectorQR);
    btnUnir = findViewById(R.id.btn_Unir);

    usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");

    Institucion institucion = new Institucion(1, "Instituto técnologico Beltrán", "12345678", new LatLng(-34.669845, -58.362907));

    instituciones.add(institucion);

    Institucion institucion2 = new Institucion(2, "El coso de musica", "12345678", new LatLng(-34.670778, -58.363532));

    instituciones.add(institucion2);

    btnLectorQR.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

      }
    });


  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */


  @Override
  public void onMapReady(GoogleMap googleMap) {

    mMap = googleMap;

    for (Institucion institucion : instituciones) {

      mMap.addMarker(new MarkerOptions().position(institucion.getUbicacion()).title(institucion.getNombre()))
              .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

    }

    /*Marker markerInstitucion;

    markerInstitucion = mMap.addMarker(new MarkerOptions().position(institucion.getUbicacion()).title(institucion.getNombre()));
    markerInstitucion.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(institucion.getUbicacion()));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(5));*/

    //verificacion usuario habilitado
    if (!usuario.isHabilidato()) {

      mMap.getUiSettings().setAllGesturesEnabled(false);
      btnLectorQR.setVisibility(View.VISIBLE);
      Toast.makeText(getApplicationContext(), "Debe unirse a una institucion para usar la app", Toast.LENGTH_SHORT).show();
      mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

          Toast.makeText(getApplicationContext(), "Debe unirse a una institucion para usar la app", Toast.LENGTH_SHORT).show();

          return false;
        }
      });

      //si esta habilitado activa el mapa y los marcadores
    } else {

      btnLectorQR.setVisibility(View.INVISIBLE);
      mMap.getUiSettings().setAllGesturesEnabled(true);

      mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

          mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
          mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

          if (markerDestino != null && polylineRuta != null) {

            markerDestino.remove();
            polylineRuta.remove();

          }

          for (Institucion institucion : instituciones) {

            if (institucion.getUbicacion().longitude == marker.getPosition().longitude && institucion.getUbicacion().latitude == marker.getPosition().latitude) {

              institucionSeleccionada = institucion;

            }

          }

          TextView tvNombreInstitucion = bsInformacionInstitucion.findViewById(R.id.detail_name);
          tvNombreInstitucion.setText(institucionSeleccionada.getNombre());

          btnUnir.setEnabled(false);
          bsbInformacionInstitucion.setState(BottomSheetBehavior.STATE_EXPANDED);

          listaIformacionRutas.clear();

          for (Ruta ruta : institucionSeleccionada.getRutas()) {

            listaIformacionRutas.add((ruta.getUsuarios().size()) + " Asistentes, Horario: " + new SimpleDateFormat("HH:mm").format(ruta.getHoraPartida()));

          }

          ADP = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listaIformacionRutas);

          listViewRutas.setAdapter(ADP);

          ADP.notifyDataSetChanged();

          return false;
        }
      });

    }


    LocationServices.getFusedLocationProviderClient(getBaseContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
      @Override
      public void onSuccess(Location location) {
        mMap.setMyLocationEnabled(true);
      }
    });

    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng latLng) {
        bsbInformacionInstitucion.setState(BottomSheetBehavior.STATE_HIDDEN);
      }
    });

    //Initialize Google Play Services
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this,
              Manifest.permission.ACCESS_FINE_LOCATION)
              == PackageManager.PERMISSION_GRANTED) {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
      }
    } else {
      buildGoogleApiClient();
      mMap.setMyLocationEnabled(true);
    }

    Button btnCrearRuta = findViewById(R.id.btn_crear_ruta);
    btnGuardarRuta = findViewById(R.id.btn_guardar_ruta);
    btnCancelar = findViewById(R.id.btn_cancelar_ruta);

    mMap.setOnMarkerDragListener(this);

    btnCrearRuta.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        bsbInformacionInstitucion.setState(BottomSheetBehavior.STATE_HIDDEN);
        btnGuardarRuta.setEnabled(false);
        btnCancelar.setEnabled(false);

        if (markerDestino != null && polylineRuta != null) {

          markerDestino.remove();
          polylineRuta.remove();

        }

        Toast.makeText(MapsActivity.this, "Seleccione su punto de destino", Toast.LENGTH_LONG).show();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

          @Override
          public void onMapClick(LatLng point) {

            markerDestino = mMap.addMarker(new MarkerOptions().position(
                    new LatLng(point.latitude, point.longitude)).title("Destino").draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            bsbCrearRuta.setState(BottomSheetBehavior.STATE_EXPANDED);

            generarRuta(institucionSeleccionada.getUbicacion(), markerDestino.getPosition());

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
              @Override
              public void onMapClick(LatLng latLng) {
                bsbInformacionInstitucion.setState(BottomSheetBehavior.STATE_HIDDEN);
              }
            });

          }
        });

      }

    });

    btnGuardarRuta.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        bsbCrearRuta.setState(BottomSheetBehavior.STATE_HIDDEN);

        markerDestino.remove();
        polylineRuta.remove();

        institucionSeleccionada.agregarRuta(new Ruta(institucionSeleccionada.getUbicacion(),
                markerDestino.getPosition(),
                Arrays.asList(usuario),
                new Date()));

        listaIformacionRutas.clear();

        for (Ruta ruta : institucionSeleccionada.getRutas()) {

          listaIformacionRutas.add((ruta.getUsuarios().size()) + " Asistentes, Horario: " + new SimpleDateFormat("HH:mm").format(ruta.getHoraPartida()));

        }

      }

    });

    btnCancelar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (polylineRuta != null && markerDestino != null) {

          polylineRuta.remove();
          markerDestino.remove();
          bsbCrearRuta.setState(BottomSheetBehavior.STATE_HIDDEN);

        }

      }
    });

    listViewRutas.setOnItemClickListener(this);

    btnUnir.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        rutaActual.agregarUsuario(usuario);

        listaIformacionRutas.clear();

        for (Ruta ruta : institucionSeleccionada.getRutas()) {

          listaIformacionRutas.add((ruta.getUsuarios().size()) + " Asistentes, Horario: " + new SimpleDateFormat("HH:mm").format(ruta.getHoraPartida()));

        }

        ADP = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listaIformacionRutas);

        listViewRutas.setAdapter(ADP);

        ADP.notifyDataSetChanged();

      }


    });

  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    mGoogleApiClient.connect();
  }


  @Override
  public void onConnected(@Nullable Bundle bundle) {

    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(1000);
    mLocationRequest.setFastestInterval(1000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d("onLocationChanged", "entered");

    mLastLocation = location;
    if (mCurrLocationMarker != null) {
      mCurrLocationMarker.remove();
    }


    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

    //move map camera
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

    Toast.makeText(MapsActivity.this, "Your Current Location", Toast.LENGTH_LONG).show();


    //stop location updates
    if (mGoogleApiClient != null) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      Log.d("onLocationChanged", "Removing Location Updates");
    }

  }

  @Override
  public void onMarkerDragStart(Marker marker) {

    if (polylineRuta != null) {

      polylineRuta.remove();
      btnGuardarRuta.setEnabled(false);

    }


  }

  @Override
  public void onMarkerDrag(Marker marker) {


  }

  @Override
  public void onMarkerDragEnd(Marker marker) {

    generarRuta(institucionSeleccionada.getUbicacion(), marker.getPosition());

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int indice, long id) {

    markerDestino.remove();
    polylineRuta.remove();

    rutaActual = institucionSeleccionada.getRutas().get(indice);

    generarRuta(rutaActual.getPuntoInicio(), rutaActual.getPuntoFin());

    markerDestino = mMap.addMarker(new MarkerOptions().position(rutaActual.getPuntoFin()).title(institucionSeleccionada.getNombre()));
    markerDestino.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    markerDestino.setDraggable(false);

  }

  public void generarRuta(LatLng puntoInicio, LatLng puntoFin) {

    //this.start_latitude = puntoInicio.latitude;
    //this.start_longitude = puntoInicio.longitude;
    //this.end_latitude = puntoFin.latitude;
    //this.end_longitude = puntoFin.longitude;

    //Log.d("end_lat", "" + this.end_latitude);
    //Log.d("end_lng", "" + this.end_longitude);

    StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
    googleDirectionsUrl.append("origin=" + puntoInicio.latitude + "," + puntoInicio.longitude);
    googleDirectionsUrl.append("&destination=" + puntoFin.latitude + "," + puntoFin.longitude);
    googleDirectionsUrl.append("&mode=walking");
    googleDirectionsUrl.append("&key=" + "AIzaSyAUUwqv11kRDpUkOORcXEW58HqOaTZGEDU");

    Object dataTransfer[] = new Object[2];

    dataTransfer[0] = googleDirectionsUrl.toString();
    dataTransfer[1] = new LatLng(puntoFin.latitude, puntoFin.longitude);
    GetDirectionsData getDirectionsData = new GetDirectionsData(new AsyncResponse() {

      @Override
      public void processFinish(String output) {

        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        //Toast.makeText(MapsActivity.this, ""+output, Toast.LENGTH_LONG).show();
        DataParser parser = new DataParser();
        String[] directionsList = parser.parseDirections(output);
        mostrarRuta(directionsList);
        btnGuardarRuta.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnUnir.setEnabled(true);

      }

    });

    getDirectionsData.execute(dataTransfer);

  }

  public void mostrarRuta(String[] directionsList) {

    PolylineOptions options = new PolylineOptions();

    int count = directionsList.length;
    for (int i = 0; i < count; i++) {

      options.color(Color.RED);
      options.width(10);
      options.addAll(PolyUtil.decode(directionsList[i]));
      options.geodesic(true);

    }

    polylineRuta = mMap.addPolyline(options);

  }

  private String getUrl(double latitude, double longitude, String nearbyPlace) {
    StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
    googlePlacesUrl.append("location=" + latitude + "," + longitude);
    googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
    googlePlacesUrl.append("&type=" + nearbyPlace);
    googlePlacesUrl.append("&sensor=true");
    googlePlacesUrl.append("&key=" + "AIzaSyAUUwqv11kRDpUkOORcXEW58HqOaTZGEDU");
    Log.d("getUrl", googlePlacesUrl.toString());
    return (googlePlacesUrl.toString());
  }

  private boolean CheckGooglePlayServices() {
    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    int result = googleAPI.isGooglePlayServicesAvailable(this);
    if (result != ConnectionResult.SUCCESS) {
      if (googleAPI.isUserResolvableError(result)) {
        googleAPI.getErrorDialog(this, result,
                0).show();
      }
      return false;
    }
    return true;
  }

  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

  public boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

      // Asking user if explanation is needed
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);


      } else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
      }
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          // permission was granted. Do the
          // contacts-related task you need to do.
          if (ContextCompat.checkSelfPermission(this,
                  Manifest.permission.ACCESS_FINE_LOCATION)
                  == PackageManager.PERMISSION_GRANTED) {

            if (mGoogleApiClient == null) {
              buildGoogleApiClient();
            }
            mMap.setMyLocationEnabled(true);
          }

        } else {

          // Permission denied, Disable the functionality that depends on this permission.
          Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
        return;
      }

      // other 'case' lines to check for other permissions this app might request.
      // You can add here other case statements according to your requirement.
    }
  }

  @Override
  public void onBackPressed() {

    moveTaskToBack(true);

  }

}

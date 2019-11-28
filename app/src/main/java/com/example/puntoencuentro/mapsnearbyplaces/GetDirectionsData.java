package com.example.puntoencuentro.mapsnearbyplaces;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class GetDirectionsData extends AsyncTask<Object, String, String> {

    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public GetDirectionsData(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Object... objects) {
        url = (String)objects[0];
        latLng = (LatLng)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }


    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);

    }


}


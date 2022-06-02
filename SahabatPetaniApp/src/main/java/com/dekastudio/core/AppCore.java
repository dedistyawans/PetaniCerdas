package com.dekastudio.core;


import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppCore extends Application {

    private Location locationGps;
    private LocationManager locationManagerGps;
    private ListenerLokasiTerbaik listenerLokasiTerbaik;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManagerGps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManagerGps.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            locationGps = locationManagerGps.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else{
            // tampilkan alert permission dibutuhkan
            Toast.makeText(this, "Dibutuhkan ijin untuk mengakses lokasi!", Toast.LENGTH_LONG).show();
        }

    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    public void setListenerLokasiTerbaik(ListenerLokasiTerbaik listenerLokasiTerbaik){
        this.listenerLokasiTerbaik = listenerLokasiTerbaik;
    }

    //return null jika tak ada location yg didapatkan
    @Nullable
    public Location getLokasiTerbaik() {
        if(locationManagerGps != null && locationManagerGps.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationGps!=null)
            return locationGps;

        if(locationGps!=null)
            return locationGps;

        return null;
    }

    public boolean apakahGpsOn(){
        return locationManagerGps.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean apakahLokasiNull(){
        return getLokasiTerbaik()==null;
    }

    public void hapusListenerTerbaik(){
        this.listenerLokasiTerbaik = null;
    }

    public interface ListenerLokasiTerbaik{
        void onLokasiTerbaikBerubah(Location location);
    }

    private final LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("GPS", "GPS change->"+String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
            locationGps = location;
            if(listenerLokasiTerbaik!=null){
                listenerLokasiTerbaik.onLokasiTerbaikBerubah(getLokasiTerbaik());
            }
        }

        @Override

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Location location = getLokasiTerbaik();
            if(location!=null && listenerLokasiTerbaik!=null){
                listenerLokasiTerbaik.onLokasiTerbaikBerubah(location);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Location location = getLokasiTerbaik();
            if(location!=null && listenerLokasiTerbaik!=null){
                listenerLokasiTerbaik.onLokasiTerbaikBerubah(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Location location = getLokasiTerbaik();
            if(location!=null && listenerLokasiTerbaik!=null){
                listenerLokasiTerbaik.onLokasiTerbaikBerubah(location);
            }
        }
    };


}

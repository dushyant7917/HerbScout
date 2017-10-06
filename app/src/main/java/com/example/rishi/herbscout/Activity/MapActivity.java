package com.example.rishi.herbscout.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rishi.herbscout.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap googleMap;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        context=this;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        MapsInitializer.initialize(context);
        this.googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        enableMyLocation();
        mapView.onResume();
    }

//    public void enableMyLocation() {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission to access the location is missing.
//            PermissionUtils.requestPermission((AppCompatActivity) getContext(), LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        } else if (context != null) {
//            // Access to the location has been granted to the app.
//            googleMap.setMyLocationEnabled(true);
//        }
//    }
}

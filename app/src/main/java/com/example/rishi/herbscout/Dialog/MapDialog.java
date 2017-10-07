package com.example.rishi.herbscout.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;

import com.example.rishi.herbscout.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Rishi on 07/10/17.
 */

public class MapDialog extends Dialog implements View.OnClickListener,OnMapReadyCallback {

    ArrayList<Double> latList,lngList;
    MapView mapView;
    GoogleMap googleMap;
    Context context;
    Button btClose;
    private static final CameraPosition INDIA_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(20, 85.08)).zoom(3.5f).bearing(0).tilt(0).build();


    public MapDialog(@NonNull Context context, ArrayList<Double> lat, ArrayList<Double> lng) {
        super(context);
        this.context=context;
        this.latList=lat;
        this.lngList=lng;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        btClose=findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        MapsInitializer.initialize(context);
        this.googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapView.onResume();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(INDIA_CAMERA));
        for(int i=0;i<latList.size();i++){
            Marker m = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latList.get(i),lngList.get(i))));

            final Marker marker=m;
            //Make the marker bounce
            final Handler handler = new Handler();

            final long startTime = SystemClock.uptimeMillis();
            final long duration = 2000;

            Projection proj = googleMap.getProjection();
            final LatLng markerLatLng = marker.getPosition();
            Point startPoint = proj.toScreenLocation(markerLatLng);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - startTime;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });


        }
    }
}

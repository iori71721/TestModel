package com.iori.custom.testmodel;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarkerActivity extends AppCompatActivity implements OnMapReadyCallback
    , GoogleMap.OnMarkerClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_marker_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        addMarker(googleMap);
        addDragMarker(googleMap);
    }

    private void addDragMarker(GoogleMap googleMap) {
        final LatLng perthLocation = new LatLng(-31.90, 115.86);
        Marker perth = googleMap.addMarker(
                new MarkerOptions()
                        .position(perthLocation)
                        .draggable(true));
    }

    private void addMarker(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        Marker marker=googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.user));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        marker.setTag("happy 123");

        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("iori", "onMarkerClick: tag "+marker.getTag());
        return false;
    }
}

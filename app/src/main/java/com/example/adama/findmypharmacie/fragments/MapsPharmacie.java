package com.example.adama.findmypharmacie.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adama.findmypharmacie.R;
import com.example.adama.findmypharmacie.models.DatabaseHelperPharmacie;
import com.example.adama.findmypharmacie.models.Pharmacie;
import com.example.adama.findmypharmacie.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapsPharmacie extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps_pharmacie);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Carte des pharmacies");
        return inflater.inflate(R.layout.activity_maps_pharmacie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
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



        DatabaseHelperPharmacie db = new DatabaseHelperPharmacie(getContext());
        ArrayList pharmacies = db.getPharmacie();
        for (int i = 0; i < pharmacies.size(); i++){
            String[] pharmacie = pharmacies.get(i).toString().split(",");
                LatLng location = new LatLng(Double.parseDouble(pharmacie[4]), Double.parseDouble(pharmacie[5]));
                mMap.addMarker(new MarkerOptions().position(location).title(pharmacie[1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        }
        // Add a marker in Sydney and move the camera

        GPSTracker gps = new GPSTracker(getContext());
        LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11));
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Localisation actuelle").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_location)));

    }


}

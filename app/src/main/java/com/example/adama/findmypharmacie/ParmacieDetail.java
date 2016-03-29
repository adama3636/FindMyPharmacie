package com.example.adama.findmypharmacie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adama.findmypharmacie.utils.GPSTracker;
import com.example.adama.findmypharmacie.utils.HttpConnection;
import com.example.adama.findmypharmacie.utils.PathJSONParser;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ParmacieDetail extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    private ImageView ivImage;
    private TextView tvName, tvTelephone, tvAdresse;
    private String strName, strTelephone, strAddress, strEmei, strLatitude, strLongitude, strAccuracy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pharmacie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Details");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        strName = getIntent().getStringExtra("Name");
        strTelephone = getIntent().getStringExtra("Telephone");
        strAddress = getIntent().getStringExtra("Address");
        strEmei= getIntent().getStringExtra("Emei");
        strLatitude = getIntent().getStringExtra("Latitude");
        strLongitude = getIntent().getStringExtra("Longitude");
        strAccuracy = getIntent().getStringExtra("Accuracy");

        ivImage = (ImageView) findViewById(R.id.backgroundImageView);
        tvName = (TextView) findViewById(R.id.namePharmacy);
        tvTelephone = (TextView) findViewById(R.id.telephonePharmacy);
        tvAdresse = (TextView) findViewById(R.id.addressPharmacy);

        tvName.setText(strName);
        tvTelephone.setText(strTelephone);
        tvAdresse.setText(strAddress);

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("Image"));
        ivImage.setImageBitmap(photoReducedSizeBitmap);

        // Gets the MapView from the XML layout and creates it
        MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
         mapView.getMapAsync(this);
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       // map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(strLatitude),Double.parseDouble(strLongitude)))      // Sets the center of the map to Mountain View
                .zoom(14)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng location = new LatLng(Double.parseDouble(strLatitude), Double.parseDouble(strLongitude)+0.1);
        map.addMarker(new MarkerOptions().position(location).title(strName).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        GPSTracker gps = new GPSTracker(getApplicationContext());
        LatLng current_location = new LatLng(gps.getLatitude(), gps.getLongitude());
        map.addMarker(new MarkerOptions().position(current_location).title("Position actuelle").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_location)));



    }







}

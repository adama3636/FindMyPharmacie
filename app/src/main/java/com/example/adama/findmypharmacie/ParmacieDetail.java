package com.example.adama.findmypharmacie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ParmacieDetail extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    private long backPressedTime = 0;    // used by onBackPressed()
    public ImageView ivImage;
    private TextView tvName, tvTelephone, tvAdresse;
    private String strName, strTelephone, strAddress, strEmei, strLatitude, strLongitude, strAccuracy;


    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Appuyer sur la touche encore pour quitter",
                    Toast.LENGTH_SHORT).show();
        }else {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

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

        (new setPicture(ivImage)).execute(getIntent().getStringExtra("Image"));

        MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

         mapView.getMapAsync(this);
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  class setPicture extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public setPicture(ImageView bmImage){
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String ... params) {
            String imgDisplay = params[0];
            File file = new File(imgDisplay);
            Bitmap photoReducedSizeBitmap = null;
            if(file.exists())
            {
                photoReducedSizeBitmap = BitmapFactory.decodeFile(file.getPath());

            }
                return photoReducedSizeBitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        LatLng location = new LatLng(Double.parseDouble(strLatitude), Double.parseDouble(strLongitude));
        map.addMarker(new MarkerOptions().position(location).title(strName).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)      // Sets the center of the map to Mountain View
                .zoom(14)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}

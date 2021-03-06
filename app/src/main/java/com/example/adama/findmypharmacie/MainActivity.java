package com.example.adama.findmypharmacie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.adama.findmypharmacie.fragments.ListNumber;
import com.example.adama.findmypharmacie.fragments.ListPharmacie;
import com.example.adama.findmypharmacie.fragments.MainFragment;
import com.example.adama.findmypharmacie.fragments.MapsPharmacie;

//-Enregistrer des pharmacies dans la base de donnees (Nom Pharmacie, Tel , Adresse, Longitudes, lattitude, precision; imei, photo)
// Lister les pharmacies enregistrer sur une liste view (Nom, Adresse, Telephone) afficher les les autre information dans le dialogue
// afficher tout pharmacie sur un textview ($nom,telephone,adresse$nom,telephone,adresse$nom,telephone,adresse)
// Afficher la carte de google map avec les pharmacies mappées sur la carte
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        ListPharmacie.OnFragmentInteractionListener, ListNumber.OnFragmentInteractionListener{
    DrawerLayout drawer;
    private long backPressedTime = 0;    // used by onBackPressed()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Add the Very First i.e Squad Fragment to the Container
        Fragment mainFragment = new ListPharmacie();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment,mainFragment,null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to logout",
                    Toast.LENGTH_SHORT).show();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //MainActivity mainActivity = (MainActivity)getApplicationContext();
        this.drawer.closeDrawers();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            openFragment(new ListPharmacie());
        } else if (id == R.id.nav_gallery) {
            openFragment(new MainFragment());
        } else if (id == R.id.nav_slideshow) {
            openFragment(new MapsPharmacie());
        }  else if (id == R.id.nav_numbers) {
            openFragment(new ListNumber());
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "market://details?id=com.example.adama.findmypharmacy";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Telecharger Find My Pharmacy");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Partager via"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void openFragment(final Fragment fragment)   {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();

    }
}

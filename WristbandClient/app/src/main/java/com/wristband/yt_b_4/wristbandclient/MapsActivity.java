package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.models.Party;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import android.view.MenuInflater;
import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Geocoder;

import java.util.Locale;

import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.identity.intents.Address;

import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String address;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private Party party;
    private String party_name, prev_class;
    private String party_id, relation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        party_name = getIntent().getStringExtra("party_name");
        address = getIntent().getStringExtra("party_location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        party_id = intent.getStringExtra("party_name");
        relation = intent.getStringExtra("relation");
    }

    //    private void getLocationPermission() {
//    /*
//     * Request location permission, so that we can get the location of the
//     * device. The result of the permission request is handled by a callback,
//     * onRequestPermissionsResult.
//     */
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapsmenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MapsActivity.this, HomeScreen.class);
        startActivity(intent);
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        double longitude;
        double latitude;
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);
            android.location.Address address = addresses.get(0);
            longitude = address.getLongitude();
            latitude = address.getLatitude();
            LatLng loc = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(loc).title(party_name));
            float zoomLevel = 16.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));
        } catch (Exception e) {

            Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
            startActivity(searchAddress);

        }


    }
}

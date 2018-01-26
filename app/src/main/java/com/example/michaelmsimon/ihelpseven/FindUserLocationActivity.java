package com.example.michaelmsimon.ihelpseven;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;


import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

public class FindUserLocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;

    private TextView printLocation;
    private String TAG;

    Location lastLocation;
    double lat;
    double lon;

    @Override
    protected void onResume() {
        super.onResume();

    }

    //and then call the method


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_location);
        printLocation = (TextView) findViewById(R.id.printLocation);

        runTime_permission();
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }


    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    private boolean runTime_permission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }

        return false;
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();
                Log.d(TAG, "lOCATION------FINAL RESULT-----" + lat + lon);
                printLocation.append("\n" + "Latitude " + lat + "\n" + " Longtide" + lon);
            } else {
                printLocation.setText("Cannot Access Gps Location");
             /*   String action = "com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS";
                Intent settings = new Intent(action);
                startActivity(settings);
               */


            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Context mContext = this;
        Toast.makeText(mContext, "suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Context mContext = this;
        Toast.makeText(mContext, "failed", Toast.LENGTH_LONG).show();
    }


}


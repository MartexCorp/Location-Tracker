package com.example.application1;

import androidx.appcompat.app.AppCompatActivity;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.configuration.PermissionConfiguration;
import com.yayandroid.locationmanager.constants.ProviderType;
import com.yayandroid.locationmanager.listener.LocationListener;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Client extends AppCompatActivity {

    double lastSavedLon, lastSavedLat;

    LocationConfiguration awesomeConfiguration = new LocationConfiguration.Builder()
            .keepTracking(true)
            .askForPermission(new PermissionConfiguration.Builder()
                    .rationaleMessage("Provide Location for Tracking!")
                    .requiredPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION })
                    .build())
            .useGooglePlayServices(new GooglePlayServicesConfiguration.Builder()
                    .fallbackToDefault(true)
                    .askForGooglePlayServices(true)
                    .askForSettingsApi(true)
                    .failOnConnectionSuspended(true)
                    .failOnSettingsApiSuspended(false)
                    .ignoreLastKnowLocation(false)
                    .setWaitPeriod(5 * 1000)
                    .build())
            .useDefaultProviders(new DefaultProviderConfiguration.Builder()
                    .requiredTimeInterval(5 * 1000)
                    .requiredDistanceInterval(1)
                    .acceptableAccuracy(4.0f)
                    .acceptableTimePeriod(5 * 1000)
                    .gpsMessage("Turn on GPS?")
                    .setWaitPeriod(ProviderType.NETWORK, 1000)
                    .setWaitPeriod(ProviderType.GPS, 1000)
                    .build())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        TextView longitude= findViewById(R.id.lon);
        TextView latitude= findViewById(R.id.lat);
        TextView av_distace= findViewById(R.id.dist);
        TextView av_speed = findViewById(R.id.av_speed);

// LocationManager MUST be initialized with Application context in order to prevent MemoryLeaks
        LocationManager awesomeLocationManager = new LocationManager.Builder(getApplicationContext())
                .activity(this) // Only required to ask permission and/or GoogleApi - SettingsApi
                .configuration(awesomeConfiguration)
                .notify(new LocationListener() {
                    @Override
                    public void onProcessTypeChanged(int processType) {
                        Log.d("TAG", "Process Changed");

                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("TAG", "onLocationChanged: lon = "+ location.getLongitude()+", lat = "+location.getLatitude());

                        longitude.setText(String.valueOf(location.getLongitude()));
                        latitude.setText(String.valueOf(location.getLatitude()));

                        av_distace.setText(String.valueOf(Math.round(distance(lastSavedLat, lastSavedLon, location.getLatitude(), location.getLongitude()))));
                        av_speed.setText(String.valueOf(Double.parseDouble(av_distace.getText().toString())/5));


                        lastSavedLat = location.getLatitude();
                        lastSavedLon = location.getLongitude();

                    }

                    @Override
                    public void onLocationFailed(int type) {
                        Log.d("TAG", "Location Failed");

                    }

                    @Override
                    public void onPermissionGranted(boolean alreadyHadPermission) {
                        Log.d("TAG", "Permission Granted");

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d("TAG", "Status Changed");

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d("TAG", "Provider Enabled");

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d("TAG", "Provider Disabled");

                    } })
                .build();
        awesomeLocationManager.get();


    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
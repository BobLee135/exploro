package com.example.exploro.controllers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationListener implements LocationListener {

    public static LatLng currentLocation = null;
    public static String currentAddress = null;
    public static MarkerOptions userLocationMarker = null;
    private Context currentActivity = null;
    private LocationManager locationManager;

    public MyLocationListener(Context context) {
        this.currentActivity = context;
        userLocationMarker = new MarkerOptions();
        userLocationMarker.title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update users location
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        currentAddress = getAddressFromLocation(location.getLatitude(), location.getLongitude());
        // Update marker for displaying users location on maps
        userLocationMarker.position(currentLocation);

        MapsActivityController.updateUserLocation(currentLocation);
        MiniMapFragment.updateUserLocation(currentLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    // Function to get the users current address
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(currentActivity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getCurrentLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            return;
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }
}

package com.example.exploro.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.exploro.BuildConfig;
import com.example.exploro.Location;
import com.example.exploro.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.example.exploro.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivityController extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String URL = "";
    private Location[] dsts;

    private static Marker userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Build the url with all destinations
        Parcelable[] pList = getIntent().getParcelableArrayExtra("destinationList");
        dsts = new Location[pList.length];
        for (int i = 0; i < pList.length; i++) {
            dsts[i] = (Location) pList[i];
        }
        buildRoute(dsts);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.miniMap);
        mapFragment.getMapAsync(this);


        Button finish = (Button) findViewById(R.id.finishRoute);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                        INSERT ADD XP TO USER CODE HERE
                 */


                // Jump back to main page
                Activity currentActivity = getSupportFragmentManager().findFragmentById(R.id.miniMap).getActivity();
                Intent intent = new Intent(currentActivity, ApplicationActivityController.class);
                startActivity(intent);
            }
        });
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

        // Send a request to google directions api for the route
        Response response = sendRequest(this.URL);

        // Display the route on the map
        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray routesArray = jsonResponse.getJSONArray("routes");
            JSONObject route = routesArray.getJSONObject(0);
            JSONObject overview_polyline = route.getJSONObject("overview_polyline");
            String encodedString = overview_polyline.getString("points");

            List<LatLng> list = PolyUtil.decode(encodedString);

            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(list);
            lineOptions.width(10);
            lineOptions.color(Color.rgb(241, 131, 131));
            mMap.addPolyline(lineOptions);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        if (MyLocationListener.currentLocation != null) {
            // add markers for each place on the route to visit
            if (dsts != null) {
                for (int i = 1; i < dsts.length; i++) {
                    MarkerOptions place = new MarkerOptions().position(dsts[i].getLocation()).title(dsts[i].getAddress());
                    googleMap.addMarker(place);
                }
            }
            // add users location
            userLocation = googleMap.addMarker(MyLocationListener.userLocationMarker);
            // move the camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocationListener.currentLocation, 15));
        }

    }

// "https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyBUhyD3CQzp538kladlXAK1dBuZXduTjvs";
// "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=seattle&inputtype=textquery&fields=formatted_address%2Cgeometry&key=AIzaSyBUhyD3CQzp538kladlXAK1dBuZXduTjvs";

    // Returns a url for a route
    public void buildRoute(Location[] destinations) {
        this.URL = "https://maps.googleapis.com/maps/api/directions/json?origin=";
        this.URL += destinations[0].getAddress();
        this.URL += "&destination=" + destinations[destinations.length-1].getAddress();
        if (destinations.length > 2) {
            this.URL += "&waypoints=";
            for (int i = 1; i < destinations.length-1; i++) {
                this.URL += "via:" + destinations[i].getAddress();
                if (i != destinations.length-2) this.URL += "|";
            }
        }
        this.URL += "&key=" + BuildConfig.MAPS_API_KEY;
    }

    // Returns a single destination based on a search string
    public Location buildPlace(String place) {
        String query = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=";
        query += place;
        query += "&inputtype=textquery&locationbias=circle:";
        query += 1000; // radius of place search
        query += "@" + MyLocationListener.currentLocation.latitude + "," + MyLocationListener.currentLocation.longitude;
        query += "&fields=formatted_address%2Cgeometry&key=AIzaSyBUhyD3CQzp538kladlXAK1dBuZXduTjvs";

        // Build the location object from the query response
        Location destination = null;
        try {
            Response response = sendRequest(query);
            JSONObject jsonResponse = new JSONObject(response.body().string());

            destination = new Location(
                    "",
                    jsonResponse.getJSONArray("candidates").getJSONObject(0).getString("formatted_address"),
                    new LatLng(
                        jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                        jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                    )
            );
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return destination;
    }

    /*
    *   THIS FUNCTION CAN BE EXPENSIVE AS IT CALLS FOR 20 LOCATIONS
    *   USE buildPlace FUNCTION INSTEAD IF POSSIBLE
    */
    // Returns an array of addresses representing destinations based on a search string
    public Location[] buildPlaces(String place, String type, int limit) {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
        url += "query=" + place;
        url += "&location=" + MyLocationListener.currentLocation.latitude + "," + MyLocationListener.currentLocation.longitude;
        url += "&opennow=" + false;
        url += "&rankby=distance";
        //url += "&radius=" + 5000;
        url += "&type=" + type;
        url += "&key=" + BuildConfig.MAPS_API_KEY;

        Location[] destinationList = new Location[limit];
        Response response = sendRequest(url);
        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            for (int i = 0; i < limit; i++) {
                Location destination = new Location(
                        "",
                        jsonResponse.getJSONArray("results").getJSONObject(i).getString("formatted_address"),
                        new LatLng(
                                jsonResponse.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                jsonResponse.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                        )
                );
                destinationList[i] = destination;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return destinationList;
    }

    // Returns a response from a request to a url
    public Response sendRequest(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    // Updates the position of the marker
    public static void updateUserLocation(LatLng newPos) {
        if (userLocation != null) {
            userLocation.setPosition(newPos);
        }
    }

}
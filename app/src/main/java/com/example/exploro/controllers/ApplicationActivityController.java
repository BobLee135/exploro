package com.example.exploro.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Property;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.example.exploro.BuildConfig;
import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.Properties;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApplicationActivityController extends AppCompatActivity {

    private GoogleMap mMap;
    private LocationsBottomSheetBehavior mLocationsBottomSheetBehavior;
    public DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        View bottomSheet = (View) findViewById(R.id.locationBottomSheet);

        // Get bottom sheet and set it to expanded by default
        mLocationsBottomSheetBehavior = (LocationsBottomSheetBehavior) LocationsBottomSheetBehavior.from(bottomSheet);
        mLocationsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        Button drawerToggle = (Button) findViewById(R.id.sideNavBarToggle);
        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        Button myButton = (Button) findViewById(R.id.createOwnRouteBtn);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.logo2).setVisibility(View.INVISIBLE);
                findViewById(R.id.sideNavBarToggle).setVisibility(View.INVISIBLE);

                SelectDestinations selectDst = new SelectDestinations();
                findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);

            }
        });

        LayoutInflater layoutInflater = getLayoutInflater();
        CardView categoryCard = findViewById(R.id.locationCategoryCard0);

        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
        url = url + "query=restaurant";
        url = url + "&key=" + BuildConfig.MAPS_API_KEY;

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < 2; i++) {
                JSONObject place = resultsArray.getJSONObject(i);
                Log.d("JSONREQUEST", place.toString());
                View locationView = layoutInflater.inflate(R.layout.place_view, categoryCard, false);
                locationView.setX(480 * i);

                TextView locationTitle = locationView.findViewById(R.id.locationViewTitle);
                locationTitle.setText(place.getString("name"));

                ImageView locationImage = (ImageView) findViewById(R.id.locationViewImage0);
                Log.d("IMAGEURL", place.getJSONObject("0").getString("photo_reference"));


                String imageUrlAPI = "https://maps.googleapis.com/maps/api/place/photo?";
                imageUrlAPI += "ARywPAKpTJYMmR_mDJxlNJ1BaHCHrvD8WxTPrFG4QIgxWczr3Ahfw_U556bA3Nnd1VLHvpHOfFbui_wrSZoCmv3nzKvMW8GFuresB4-ll5oVtfyg5cAJnQoccN4IHvJxDcxNkQEY-iqM7--knyreD8tZgbn3fbHeMhfX5osk_avddo0O4dyu";
                imageUrlAPI += "&key=" + BuildConfig.MAPS_API_KEY;

                request = new Request.Builder().url(imageUrlAPI).get().build();
                Response imageResponse = null;
                try {
                    imageResponse = client.newCall(request).execute();
                    JSONObject responseJson = new JSONObject(imageResponse.body().string());
                    Log.d("IMAGETEST", imageResponse.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                categoryCard.addView(locationView);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        */





    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}

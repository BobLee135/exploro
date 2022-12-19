package com.example.exploro.controllers;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Property;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.example.exploro.BuildConfig;
import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.MapsAPICaller;
import com.example.exploro.MessageHelper;
import com.example.exploro.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApplicationActivityController extends AppCompatActivity {

    private MapsAPICaller mMapsApiCaller;
    private MessageHelper mMessageHelper;
    private GoogleMap mMap;
    private LocationsBottomSheetBehavior mLocationsBottomSheetBehavior;
    public DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        mMessageHelper = new MessageHelper();
        mMapsApiCaller = new MapsAPICaller();

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

        MapsAPICaller.PlaceTypes[] categories = {
            MapsAPICaller.PlaceTypes.RESTAURANT,
            MapsAPICaller.PlaceTypes.MUSEUM,
            MapsAPICaller.PlaceTypes.TOURIST_ATTRACTION,
            MapsAPICaller.PlaceTypes.NIGHT_CLUB,
            MapsAPICaller.PlaceTypes.PARK,
            MapsAPICaller.PlaceTypes.BAR,
            MapsAPICaller.PlaceTypes.AMUSEMENT_PARK
        };

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LayoutInflater layoutInflater = getLayoutInflater();
                        LinearLayout categoryHolder = findViewById(R.id.locationCategoryHolder);
                        for (int i = 0; i < categories.length; i++) {
                            // Create new card
                            View categoryCard = layoutInflater.inflate(R.layout.location_category_view, categoryHolder, false);
                            categoryHolder.addView(categoryCard);
                            createCardViewsForCategoryView(categories[i].toString(), categories[i], categoryCard);

                            // Set card title text
                            TextView categoryTitle = categoryCard.findViewById(R.id.locationsCategoryText0);
                            String title = categories[i].toString();
                            title = title.substring(0, 1).toUpperCase(Locale.ROOT) + title.substring(1);
                            categoryTitle.setText(title);
                        }
                    }
                });
            }
        }.start();

    }


    /**
     * Get the top two places for specified category and create card view with place info and image
     * in bottom sheet menu
     *
     * @param query - Search string query
     * @param type - category type
     * @param categoryCard - Which category card to create new location views in
     */
    private void createCardViewsForCategoryView(String query, MapsAPICaller.PlaceTypes type, View categoryCard) {
        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayout locationViewHolder = categoryCard.findViewById(R.id.locationViewHolder);

        // Get places from API
        JSONArray places = mMapsApiCaller.getPlacesFromQuery(query, false, 2000, type, 2);

        // If places couldn't be retrieved
        if (places == null) {
            mMessageHelper.displaySnackbar("Couldn't load places from location", 3 , "Error", locationViewHolder);
            return;
        }

        // Add location views for each location
        for (int i  = 0; i < places.length(); i++) {
            try {
                JSONObject place = places.getJSONObject(i);
                View locationView = layoutInflater.inflate(R.layout.place_view, locationViewHolder, false);

                // remove padding of last item
                if (i == places.length() - 1)
                    locationView.setPadding(0,0,0,0);

                // Create the view
                TextView locationTitle = locationView.findViewById(R.id.locationViewTitle);
                locationTitle.setText(place.getString("name"));
                locationViewHolder.addView(locationView);


                // Get image for place
                // Check if place has image
                if (!place.has("photos")) {
                    mMessageHelper.displaySnackbar("Couldn't load photos array from location", 3 , "Error", locationViewHolder);
                    continue;
                }

                // Dp to px scale
                float scale = this.getResources().getDisplayMetrics().density;

                // Get image from API using place photo reference
                String photoRef = place.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                byte[] imageUrl = mMapsApiCaller.getImageURLFromPhotoReference(photoRef, (int)(150 * scale + 0.5f), (int) (110 * scale + 0.5f));
                if (imageUrl == null) {
                    mMessageHelper.displaySnackbar("Couldn't load photo for location", 3 , "Error", locationViewHolder);
                    continue;
                }
                // Convert API response into image bitmap
                Bitmap imageBitMap = BitmapFactory.decodeByteArray(imageUrl, 0, imageUrl.length);
                if (imageBitMap == null) {
                    Log.d("bitmaptest", imageUrl.toString());
                    continue;
                }

                // Set image
                ImageView imageView = locationView.findViewById(R.id.locationViewImage0);
                imageView.setImageBitmap(imageBitMap);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}

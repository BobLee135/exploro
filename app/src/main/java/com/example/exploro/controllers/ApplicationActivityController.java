package com.example.exploro.controllers;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.exploro.Location;
import com.example.exploro.RunnableWithIndex;
import com.example.exploro.DataObservable;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.exploro.BuildConfig;

import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.MapsAPICaller;
import com.example.exploro.MessageHelper;
import com.example.exploro.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ApplicationActivityController extends AppCompatActivity {

    private MapsAPICaller mMapsApiCaller;
    private MessageHelper mMessageHelper;
    private DataObservable mDataObservable;
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
        Button createButton = (Button) findViewById(R.id.createOwnRouteBtn);
        ViewGroup.LayoutParams params = createButton.getLayoutParams();

        // Get bottom sheet and set it to expanded by default
        mLocationsBottomSheetBehavior = (LocationsBottomSheetBehavior) LocationsBottomSheetBehavior.from(bottomSheet);
        mLocationsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        int bottomSheetState = mLocationsBottomSheetBehavior.getState();

        mLocationsBottomSheetBehavior.addBottomSheetCallback(new LocationsBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View v, int state) {
                if (bottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.d("STATETEST", "STATE: " + state);
                }
                if (state == BottomSheetBehavior.STATE_EXPANDED)
                    createButton.setTranslationY(0);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("STATETEST", "PEEK: " + mLocationsBottomSheetBehavior.getPeekHeight());
                Log.d("STATETEST", "SLIDE: " + (slideOffset * -1 + 1));
                createButton.setTranslationY(1200 * (slideOffset * -1 + 1));
            }
        });

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
        NavigationView nw = drawerLayout.findViewById(R.id.nav_view);
        nw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_leaderboards) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                    fragmentTransaction.replace(R.id.selectFragmentContainer, leaderboardFragment);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.logo2).setVisibility(View.INVISIBLE);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }
        });


        // See pre planned routes button
        Button prePlanned = (Button) findViewById(R.id.seePrePlannedRoutesBtn);
        prePlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.logo2).setVisibility(View.INVISIBLE);

                findViewById(R.id.listOfPrePlanned).setVisibility(View.VISIBLE);
                findViewById(R.id.prePlannedCreateOwn).setVisibility(View.VISIBLE);
            }
        });

        // Create my own route button
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

        // Create my own route button in pre planned view
        Button preCreate = (Button) findViewById(R.id.prePlannedCreateOwn);
        preCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.logo2).setVisibility(View.INVISIBLE);
                findViewById(R.id.sideNavBarToggle).setVisibility(View.INVISIBLE);
                findViewById(R.id.prePlannedCreateOwn).setVisibility(View.INVISIBLE);

                SelectDestinations selectDst = new SelectDestinations();
                findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);
            }
        });

        /*
        *   Pre planned routes buttons
        */
        MapsActivityController mac = new MapsActivityController();

        ImageView viewFood = (ImageView) findViewById(R.id.imageViewFood);
        viewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number_of_dsts = 4;
                Location[] destinationList = new Location[1 + number_of_dsts]; // user location + destinations
                destinationList[0] = new Location("You", MyLocationListener.currentAddress, MyLocationListener.currentLocation);

                Location[] dsts = mac.buildPlaces("restaurant", "", number_of_dsts);
                for (int i = 0; i < dsts.length; i++) {
                    destinationList[i+1] = dsts[i];
                }

                shipAndSendRoute(destinationList);
            }
        });
        ImageView viewPub = (ImageView) findViewById(R.id.imageViewPub);
        viewPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ImageView viewMustSee = (ImageView) findViewById(R.id.imageViewMustSee);
        viewMustSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ImageView viewParty = (ImageView) findViewById(R.id.imageViewParty);
        viewParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ImageView viewPerfect = (ImageView) findViewById(R.id.imageViewPerfect);
        viewPerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        /* LOADING PLACES FOR BOTTOM SHEET, DISABLED ATM TO REDUCE COST
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

        int numThreads = 16;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread();
        }


        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayout categoryHolder = findViewById(R.id.locationCategoryHolder);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < categories.length; i++) {
            // Create new card
            RunnableWithIndex r = new RunnableWithIndex(i) {
                @Override
                public void run() {
                    View categoryCard = layoutInflater.inflate(R.layout.location_category_view, categoryHolder, false);
                    categoryHolder.addView(categoryCard);
                    createCardViewsForCategoryView(categories[index].toString(), categories[index], categoryCard);

                    // Set card title text
                    TextView categoryTitle = categoryCard.findViewById(R.id.locationsCategoryText0);
                    String title = categories[index].toString();
                    title = title.substring(0, 1).toUpperCase(Locale.ROOT) + title.substring(1);
                    categoryTitle.setText(title);
                }
            };
            executor.execute(r);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */
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

    private void shipAndSendRoute(Location[] destinations) {
        Intent intent = new Intent(this, MapsActivityController.class);
        intent.putExtra("destinationList", destinations);
        startActivity(intent);
    }
}

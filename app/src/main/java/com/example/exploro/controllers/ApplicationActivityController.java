package com.example.exploro.controllers;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.exploro.Location;
import com.example.exploro.DataObservable;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.MapsAPICaller;
import com.example.exploro.MessageHelper;
import com.example.exploro.R;
import com.example.exploro.RunnableWithIndex;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ApplicationActivityController extends AppCompatActivity {

    // Objects
    private MapsAPICaller mMapsApiCaller;
    private MessageHelper mMessageHelper;
    private DataObservable mDataObservable;
    private GoogleMap mMap;
    private LocationsBottomSheetBehavior mLocationsBottomSheetBehavior;

    // Caches
    private JSONArray placeCache;
    private JSONArray queryCaches;

    // OnclickListeners
    private View.OnClickListener placeClickListener;

    // Views
    private DrawerLayout drawerLayout;
    private ScrollView locationsScroll;
    private ScrollView categoriesScroll;

    // Global variables
    private float initOffset = -200f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Get components
        mMessageHelper = new MessageHelper();
        mMapsApiCaller = new MapsAPICaller();

        // Initialize cache arrays
        placeCache = new JSONArray();
        queryCaches = new JSONArray();

        // Get views
        locationsScroll = findViewById(R.id.allLocationsScrollView);
        categoriesScroll = findViewById(R.id.allCategoriesView);
        View bottomSheet = (View) findViewById(R.id.locationBottomSheet);
        Button createButton = (Button) findViewById(R.id.createOwnRouteBtn);
        Button preplannedButton = (Button) findViewById(R.id.seePrePlannedRoutesBtn);
        Button viewAllBackButton = (Button) findViewById(R.id.viewAllBackButton);
        /*
        ImageView IVFood = (ImageView) findViewById(R.id.imageViewFood);
        ImageView IVPub = (ImageView) findViewById(R.id.imageViewPub);
        ImageView IVPerfect = (ImageView) findViewById(R.id.imageViewPerfect);
        ImageView IVMustSee = (ImageView) findViewById(R.id.imageViewMustSee);
        ImageView IVParty = (ImageView) findViewById(R.id.imageViewParty);
         */
        RelativeLayout RLFood = (RelativeLayout) findViewById(R.id.RLFood);
        RelativeLayout RLPub = (RelativeLayout) findViewById(R.id.RLPub);
        RelativeLayout RLPerfect = (RelativeLayout) findViewById(R.id.RLPerfect);
        RelativeLayout RLMustSee = (RelativeLayout) findViewById(R.id.RLMustSee);
        RelativeLayout RLParty = (RelativeLayout) findViewById(R.id.RLParty);

        buttonsInit();

        // Get bottom sheet and set it to expanded by default
        mLocationsBottomSheetBehavior = (LocationsBottomSheetBehavior) LocationsBottomSheetBehavior.from(bottomSheet);
        mLocationsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        mLocationsBottomSheetBehavior.addBottomSheetCallback(new LocationsBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View v, int state) {
                if (state == BottomSheetBehavior.STATE_EXPANDED)
                    buttonsInit();
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // If sheet almost all the way up, reset buttons
                if (slideOffset >= 0.9)
                    buttonsInit();

                // get the inverse of slideOffset, so instead of going 1 to 0, it goes 0 - 1
                float inverseOffset = (slideOffset * -1 + 1);

                // transition buttons depending on slide distance of bottom sheet
                createButton.setTranslationY(1340 * inverseOffset);
                preplannedButton.setAlpha(1 * slideOffset);

                if (inverseOffset >= 0.1) {
                    RLFood.setAlpha(1 * inverseOffset);
                    RLFood.setTranslationY(lerp(initOffset, 0, inverseOffset));
                } else
                    RLFood.setAlpha(0f);

                if (inverseOffset >= 0.3) {
                    RLPub.setAlpha(1 * inverseOffset);
                    RLPub.setTranslationY(lerp(initOffset, 0, inverseOffset));
                } else
                    RLPub.setAlpha(0f);

                if (inverseOffset >= 0.5) {
                    RLMustSee.setAlpha(1 * inverseOffset);
                    RLMustSee.setTranslationY(lerp(initOffset, 0, inverseOffset));
                } else
                    RLMustSee.setAlpha(0f);

                if (inverseOffset >= 0.7) {
                    RLParty.setAlpha(1 * inverseOffset);
                    RLParty.setTranslationY(lerp(initOffset, 0, inverseOffset));
                } else
                    RLParty.setAlpha(0f);

                if (inverseOffset >= 0.9) {
                    RLPerfect.setAlpha(1 * inverseOffset);
                    RLPerfect.setTranslationY(lerp(initOffset, 0, inverseOffset));
                } else
                    RLPerfect.setAlpha(0f);

            }
        });

        viewAllBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("HEJSAN BITCH");
                exitViewAllMode();
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

        // Handle nav bar inputs
        nw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Leaderboards
                if (item.getItemId() == R.id.nav_leaderboards) {
                    // Fragment transaction
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                    fragmentTransaction.replace(R.id.selectFragmentContainer, leaderboardFragment);

                    // UI changes
                    drawerLayout.closeDrawer(GravityCompat.START);
                    findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.logo2).setVisibility(View.INVISIBLE);

                    // Hide bottom sheet
                    mLocationsBottomSheetBehavior.setState(mLocationsBottomSheetBehavior.STATE_COLLAPSED);

                    fragmentTransaction.commit();
                    return true;
                }

                // Profile
                if (item.getItemId() == R.id.nav_profile) {
                    // Fragment transaction
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ProfileFragmentController profileFragment = new ProfileFragmentController();
                    fragmentTransaction.replace(R.id.selectFragmentContainer, profileFragment);

                    // UI changes
                    drawerLayout.closeDrawer(GravityCompat.START);
                    findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.logo2).setVisibility(View.INVISIBLE);

                    // Hide bottom sheet
                    mLocationsBottomSheetBehavior.setState(mLocationsBottomSheetBehavior.STATE_COLLAPSED);

                    fragmentTransaction.commit();
                    return true;
                }

                // Friends
                if (item.getItemId() == R.id.nav_friends) {
                    // Fragment transaction
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FriendsFragmentController friendsFragment = new FriendsFragmentController();
                    fragmentTransaction.replace(R.id.selectFragmentContainer, friendsFragment);

                    // UI changes
                    drawerLayout.closeDrawer(GravityCompat.START);
                    findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.logo2).setVisibility(View.INVISIBLE);

                    // Hide bottom sheet
                    mLocationsBottomSheetBehavior.setState(mLocationsBottomSheetBehavior.STATE_COLLAPSED);

                    fragmentTransaction.commit();
                }


                // Log out
                if (item.getItemId() == R.id.nav_log_out)
                    finish();

                return false;
            }
        });


        // See pre planned routes button
        preplannedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsFinalState();
                // Hide bottom sheet
                mLocationsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // Create my own route button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.logo2).setVisibility(View.INVISIBLE);

                SelectDestinations selectDst = new SelectDestinations();
                findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);
                // Hide bottom sheet
                mLocationsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
                String[] destinationList = {"restaurant", "restaurant", "restaurant", "restaurant"};
                shipAndSendRoute(destinationList);
            }
        });
        ImageView viewPub = (ImageView) findViewById(R.id.imageViewPub);
        viewPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] destinationList = {"bar", "bar", "bar", "bar"};
                shipAndSendRoute(destinationList);
            }
        });
        ImageView viewMustSee = (ImageView) findViewById(R.id.imageViewMustSee);
        viewMustSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] destinationList = {"tourist attraction", "tourist attraction", "tourist attraction", "tourist attraction"};
                shipAndSendRoute(destinationList);
            }
        });
        ImageView viewParty = (ImageView) findViewById(R.id.imageViewParty);
        viewParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] destinationList = {"bar", "nightclub", "kebab", "hotel"};
                shipAndSendRoute(destinationList);
            }
        });
        ImageView viewPerfect = (ImageView) findViewById(R.id.imageViewPerfect);
        viewPerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] destinationList = {"park", "bowling", "restaurant", "bar"};
                shipAndSendRoute(destinationList);
            }
        });


        placeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View place) {
                TextView placeNameText = place.findViewById(R.id.locationViewTitle);
                ImageView placeImage = place.findViewById(R.id.locationViewImage);
                String placeName = placeNameText.getText().toString();

                // Get place info
                JSONObject placeInfo = getPlaceFromCache(placeName);
                if (placeInfo == null)
                    return;

                // Create dialog window
                Dialog dialog = createDialogWindowForPlace(placeInfo, placeName, placeImage);
                if (dialog == null)
                    return;

                dialog.show();
            }
        };

        /**
         * GENERATE BOTTOM SHEET CONTENT
         */

        // Categories to create for bottom sheet
        MapsAPICaller.PlaceTypes[] categories = {
            MapsAPICaller.PlaceTypes.RESTAURANT,
            MapsAPICaller.PlaceTypes.MUSEUM,
            MapsAPICaller.PlaceTypes.TOURIST_ATTRACTION,
            MapsAPICaller.PlaceTypes.NIGHT_CLUB,
            MapsAPICaller.PlaceTypes.PARK,
            MapsAPICaller.PlaceTypes.BAR,
            MapsAPICaller.PlaceTypes.AMUSEMENT_PARK
        };

        // Set thread policy to strict mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create array of threads
        int numThreads = 16;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread();
        }

        // Get necessary components to generate category cards
        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayout categoryHolder = findViewById(R.id.locationCategoryHolder);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Execute
        generateCategoryCardsWithContent(executor, categories, categoryHolder, layoutInflater);
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create dialog window full of information about a place
     *
     * @param placeInfo - JSONObject with google maps text search of place response
     * @param placeName - specific place name
     * @param placeImage - specific place image
     * @return the dialog window created
     */
    private Dialog createDialogWindowForPlace(JSONObject placeInfo, String placeName, ImageView placeImage) {

        // Create dialog to show place info
        Dialog dialog = new Dialog(ApplicationActivityController.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.place_info_dialog);

        // Get views to change in dialog
        TextView placeInfoName = (TextView) dialog.findViewById(R.id.placeInfoName);
        TextView placeInfoAddress = (TextView) dialog.findViewById(R.id.placeInfoAddress);
        TextView placeInfoHours = (TextView) dialog.findViewById(R.id.placeInfoHours);
        TextView placeInfoPrice = (TextView) dialog.findViewById(R.id.placeInfoPrice);
        TextView placeInfoRating = (TextView) dialog.findViewById(R.id.placeInfoRating);
        TextView placeInfoRatingsNr = (TextView) dialog.findViewById(R.id.placeInfoRatingsNr);
        TextView placeInfoWebsite = (TextView) dialog.findViewById(R.id.placeInfoWebsite);
        ImageView placeInfoImage = (ImageView) dialog.findViewById(R.id.placeInfoImage);

        // Cancel button
        Button closePlaceInfoButton = (Button) dialog.findViewById(R.id.closePlaceInfoButton);

        // Set values to view
        placeInfoName.setText(placeName);
        placeInfoImage.setImageBitmap(((BitmapDrawable) placeImage.getDrawable()).getBitmap());
        try {
            // Set address
            if (placeInfo.has("formatted_address"))
                placeInfoAddress.setText("Address: " + placeInfo.getString("formatted_address"));

            // Set opening hours
            if (placeInfo.has("opening_hours")) {
                String openHoursText = "Open Hours: ";
                JSONObject openHours = placeInfo.getJSONObject("opening_hours");
                if (openHours != null) {

                    // Check if place has optional open_now field
                    if (openHours.has("open_now")) {
                        if (openHours.getBoolean("open_now"))
                            openHoursText += " Currently open!";
                        else openHoursText += " Not open!";
                    }

                    // Check if place has optional periods field
                    if (openHours.has("periods")) {
                        JSONArray periods = openHours.getJSONArray("periods");
                        for (int i = 0; i < periods.length(); i++) {
                            JSONObject period = periods.getJSONObject(i);
                            if (period != null) {
                                JSONObject openTime = period.getJSONObject("open");
                                int dayNr = openTime.getInt("day");
                                String day = "";
                                String time = openTime.getString("time");

                                // Convert dayNr to correct day
                                switch (dayNr) {
                                    case 0:
                                        day = "Sunday";
                                        break;
                                    case 1:
                                        day = "Monday";
                                        break;
                                    case 2:
                                        day = "Tuesday";
                                        break;
                                    case 3:
                                        day = "Wednesday";
                                        break;
                                    case 4:
                                        day = "Thursday";
                                        break;
                                    case 5:
                                        day = "Friday";
                                        break;
                                    case 6:
                                        day = "Saturday";
                                        break;
                                }

                                openHoursText += "\n" + day + " " + time;
                            }

                        }
                    }
                    placeInfoHours.setText(openHoursText);
                }
            }

            // Set price level
            if (placeInfo.has("price_level")) {
                int priceLevel = placeInfo.getInt("price_level"); // Price level integer
                String priceLevelFlair = ""; // descriptive text after level
                switch (priceLevel) {
                    case 0:
                        priceLevelFlair = "Free";
                        break;
                    case 1:
                        priceLevelFlair = "Inexpensive";
                        break;
                    case 2:
                        priceLevelFlair = "Moderate";
                        break;
                    case 3:
                        priceLevelFlair = "Expensive";
                        break;
                    case 4:
                        priceLevelFlair = "Very Expensive";
                        break;
                    default:
                        priceLevelFlair = "Unknown";
                        break;
                }

                placeInfoPrice.setText("Price Level: " + priceLevel + " " + priceLevelFlair);
            }

            // Set rating
            if (placeInfo.has("rating"))
                placeInfoRating.setText("Rating: " + placeInfo.getInt("rating") + " / 5");

            // Set amount of ratings
            if (placeInfo.has("user_ratings_total"))
                placeInfoRatingsNr.setText("Reviews: " + placeInfo.getInt("user_ratings_total"));

            // Set website url
            if (placeInfo.has("website"))
                placeInfoWebsite.setText("Website URL:" + placeInfo.getString("website"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Handle close button clicked
        closePlaceInfoButton.setOnClickListener((v -> {
            dialog.dismiss();
        }));

        return dialog;
    }

    /**
     * Generate category cards filled with place cards as content
     *
     * @param executor - Executor service to run threads
     * @param categories - categories array to go from
     * @param holder - Layout to hold category cards
     * @param inflater - Layout inflater
     */
    private void generateCategoryCardsWithContent(Executor executor, MapsAPICaller.PlaceTypes[] categories, LinearLayout holder, LayoutInflater inflater) {
        for (int i = 0; i < categories.length; i++) {
            // Create new card
            RunnableWithIndex r = new RunnableWithIndex(i) {
                @Override
                public void run() {
                    View categoryCard = inflater.inflate(R.layout.location_category_view, holder, false);
                    holder.addView(categoryCard);
                    LinearLayout categoryCardHolder = categoryCard.findViewById(R.id.locationViewHolder);
                    JSONArray pairPlaces = getPlaces(categories[index].toString(), categories[index], 0);
                    generatePlaceCards(pairPlaces, categoryCardHolder, holder);

                    // Set card title text
                    TextView categoryTitle = categoryCard.findViewById(R.id.locationsCategoryText0);

                    // Remove underscore '_' (if it exists in title)
                    char[] tempTitle = categories[index].toString().toCharArray();
                    for (int c = 0; c < tempTitle.length; c++) {
                        if (tempTitle[c] == '_')
                            tempTitle[c] = ' ';
                    }

                    String title = new String(tempTitle);
                    title = title.substring(0, 1).toUpperCase(Locale.ROOT) + title.substring(1);
                    categoryTitle.setText(title);

                    // Set click listener for view all button
                    View viewAll = categoryCard.findViewById(R.id.viewAllText0);
                    viewAll.setOnClickListener((v -> {
                        locationsScroll.setVisibility(View.VISIBLE);
                        categoriesScroll.setVisibility(View.INVISIBLE);
                        findViewById(R.id.viewAllBackButton).setVisibility(View.VISIBLE);
                        LinearLayout viewAllHolder = locationsScroll.findViewById(R.id.categoryPlaceViewHolder);
                        TextView headerTitle = (TextView) viewAllHolder.findViewById(R.id.categoryTitleText);

                        // If previous load was of the same category then no need to reload it
                        if (headerTitle.getText().equals("Top " + categoryTitle.getText() + "s"))
                            return;

                        headerTitle.setText("Top " + categoryTitle.getText() + "s");

                        // Clear up view from previous uses
                        for (int i = 0; i < viewAllHolder.getChildCount(); i++) {
                            if (viewAllHolder.getChildAt(i) == headerTitle)
                                continue;
                            viewAllHolder.removeViewAt(i);
                        }

                        // Generate new views
                        LinearLayout pairHolder = (LinearLayout) getLayoutInflater().inflate(R.layout.location_pair_holder, viewAllHolder, false);
                        viewAllHolder.addView(pairHolder);
                        if (viewAllHolder != null) {
                            JSONArray places = getPlaces(categories[index].toString(), categories[index], 0);
                            generatePlaceCards(places, pairHolder, viewAllHolder);
                        }
                    }));
                }
            };
            executor.execute(r);
        }
    }



    /**
     * Get place json object info from its name in cache
     * @param placeName - place title name
     * @return JSONobject with corresponding place info
     */
    private JSONObject getPlaceFromCache(String placeName) {
        for (int i = 0; i < placeCache.length(); i++) {
            try {
                JSONObject place = placeCache.getJSONObject(i);
                if (place.get("name").equals(placeName))
                    return place;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JSONArray getPlaces(String query, MapsAPICaller.PlaceTypes type, int amount) {
        JSONArray places = null;

        // Check if wanted places are already in cache
        for (int i = 0; i < queryCaches.length(); i++) {
            try {
                if (queryCaches.getJSONObject(i).has(query)) {
                    System.out.println(queryCaches.getJSONObject(i).getJSONArray(query).length());

                    if (queryCaches.getJSONObject(i).getJSONArray(query).length() == amount) {
                        places = queryCaches.getJSONObject(i).getJSONArray(query);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Get places from API if places wasn't already in cache
        if (places == null) {
            places = mMapsApiCaller.getPlacesFromQuery(query, false, 2000, type, amount);

            // If places couldn't be retrieved
            if (places == null) {
                Log.e("Warning", "Couldn't load places from location");
                return null;
            }

            // Cache api results
            JSONObject queryToCache = new JSONObject();
            try {
                queryToCache.put(query, places);
                queryCaches.put(queryToCache);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return places;
    }

    /**
     * Generate place card views for a JSONArray of places
     *
     * @param places - JSONArray of places
     * @param holder - Which category card to create new location views in
     * @param root - Root holder
     */
    private void generatePlaceCards(JSONArray places, LinearLayout holder, LinearLayout root) {
        LayoutInflater layoutInflater = getLayoutInflater();

        // Add location views for each location
        for (int i  = 0; i < places.length(); i++) {
            try {
                // Get place and cache it
                JSONObject place = places.getJSONObject(i);
                placeCache.put(place);

                // Inflate place view
                View locationView = layoutInflater.inflate(R.layout.place_view, holder, false);

                // remove padding of last item
                /*if (i == places.length() - 1)
                    locationView.setPadding(0,0,0,0);*/

                // Create the view
                TextView locationTitle = locationView.findViewById(R.id.locationViewTitle);
                locationTitle.setText(place.getString("name"));
                RatingBar locationRating = locationView.findViewById(R.id.locationViewRating);
                locationRating.setRating(place.getInt("rating"));
                holder.addView(locationView);

                // Add a new horizontal holder for every two places
                if (i % 2 == 1 && places.length() > 2) {
                    LinearLayout newHolder = (LinearLayout) layoutInflater.inflate(R.layout.location_pair_holder, root, false);
                    root.addView(newHolder);
                    holder = newHolder;
                }

                // Set click listener for view
                if (placeClickListener != null)
                    locationView.setOnClickListener(placeClickListener);

                // Get image for place
                // Check if place has image
                if (!place.has("photos")) {
                    Log.e("Warning", "Couldn't load photos array from location");
                    continue;
                }

                // Dp to px scale
                float scale = this.getResources().getDisplayMetrics().density;

                // Get image from API using place photo reference
                String photoRef = place.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                byte[] imageUrl = mMapsApiCaller.getImageURLFromPhotoReference(photoRef, (int)(150 * scale + 0.5f), (int) (110 * scale + 0.5f));
                if (imageUrl == null) {
                    Log.e("Warning", "Couldn't load photo for location");
                    continue;
                }

                // Convert API response into image bitmap
                Bitmap imageBitMap = BitmapFactory.decodeByteArray(imageUrl, 0, imageUrl.length);
                if (imageBitMap == null) {
                    Log.d("bitmaptest", imageUrl.toString());
                    continue;
                }

                // Set image
                ImageView imageView = locationView.findViewById(R.id.locationViewImage);
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
        else {
            if (locationsScroll.getVisibility() == View.VISIBLE)
                exitViewAllMode();
            else
                super.onBackPressed();
        }
    }

    /**
     * Exit view all mode, change visibilities and delete content
     */
    private void exitViewAllMode() {
        locationsScroll.setVisibility(View.INVISIBLE);
        categoriesScroll.setVisibility(View.VISIBLE);
        findViewById(R.id.viewAllBackButton).setVisibility(View.INVISIBLE);
    }

    private void shipAndSendRoute(String[] destinations) {
        Intent intent = new Intent(this, MapsActivityController.class);
        intent.putExtra("destinationList", destinations);
        startActivity(intent);
    }

    /**
     * Lerp function
     *
     * @param start - Start value
     * @param end - Value to end at
     * @param point - point from 0-1 (start-end ratio)
     * @return float point value of lerp
     */
    private float lerp(float start, float end, float point) {
        return start + point * (end - start);
    }

    // Set preplanned routes buttons to the initialized position and alpha values
    // TODO: CLEANUP
    private void buttonsInit() {
        Button createButton = (Button) findViewById(R.id.createOwnRouteBtn);
        Button preplannedButton = (Button) findViewById(R.id.seePrePlannedRoutesBtn);
        RelativeLayout RLFood = (RelativeLayout) findViewById(R.id.RLFood);
        RelativeLayout RLPub = (RelativeLayout) findViewById(R.id.RLPub);
        RelativeLayout RLPerfect = (RelativeLayout) findViewById(R.id.RLPerfect);
        RelativeLayout RLMustSee = (RelativeLayout) findViewById(R.id.RLMustSee);
        RelativeLayout RLParty = (RelativeLayout) findViewById(R.id.RLParty);

        RLFood.setAlpha(0f);
        RLPub.setAlpha(0f);
        RLPerfect.setAlpha(0f);
        RLMustSee.setAlpha(0f);
        RLParty.setAlpha(0f);
        preplannedButton.setAlpha(1f);

        RLFood.setTranslationY(initOffset);
        RLPub.setTranslationY(initOffset);
        RLPerfect.setTranslationY(initOffset);
        RLMustSee.setTranslationY(initOffset);
        RLParty.setTranslationY(initOffset);
        preplannedButton.setTranslationY(0);
        createButton.setTranslationY(0);
    }

    // Set planned routes buttons in the finalized position and alpha values
    // TODO: CLEANUP
    private void buttonsFinalState() {
        Button createButton = (Button) findViewById(R.id.createOwnRouteBtn);
        Button preplannedButton = (Button) findViewById(R.id.seePrePlannedRoutesBtn);
        ImageView IVFood = (ImageView) findViewById(R.id.imageViewFood);
        ImageView IVPub = (ImageView) findViewById(R.id.imageViewPub);
        ImageView IVPerfect = (ImageView) findViewById(R.id.imageViewPerfect);
        ImageView IVMustSee = (ImageView) findViewById(R.id.imageViewMustSee);
        ImageView IVParty = (ImageView) findViewById(R.id.imageViewParty);

        IVFood.setAlpha(1f);
        IVPub.setAlpha(1f);
        IVPerfect.setAlpha(1f);
        IVMustSee.setAlpha(1f);
        IVParty.setAlpha(1f);
        preplannedButton.setAlpha(0f);

        IVFood.setTranslationY(0f);
        IVPub.setTranslationY(0f);
        IVPerfect.setTranslationY(0f);
        IVMustSee.setTranslationY(0f);
        IVParty.setTranslationY(0f);

        createButton.setTranslationY(1340f);
    }
}


package com.example.exploro.controllers;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

public class ApplicationActivityController extends AppCompatActivity {

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
        NavigationView nw = findViewById(R.id.nav_view);
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
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}

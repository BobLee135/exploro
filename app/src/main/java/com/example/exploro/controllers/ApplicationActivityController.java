package com.example.exploro.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ApplicationActivityController extends AppCompatActivity {

    private LocationsBottomSheetBehavior mLocationsBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        View bottomSheet = (View) findViewById(R.id.locationBottomSheet);

        // Get bottom sheet and set it to expanded by default
        mLocationsBottomSheetBehavior = (LocationsBottomSheetBehavior) LocationsBottomSheetBehavior.from(bottomSheet);
        mLocationsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        Button myButton = (Button) findViewById(R.id.createOwnRouteBtn);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.seePrePlannedRoutesBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.createOwnRouteBtn).setVisibility(View.INVISIBLE);
                findViewById(R.id.logo2).setVisibility(View.INVISIBLE);
                findViewById(R.id.button).setVisibility(View.INVISIBLE);

                SelectDestinations selectDst = new SelectDestinations();
                findViewById(R.id.selectFragmentContainer).setVisibility(View.VISIBLE);

            }
        });

    }

}

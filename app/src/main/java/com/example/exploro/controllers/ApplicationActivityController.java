package com.example.exploro.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.exploro.LocationsBottomSheetBehavior;
import com.example.exploro.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ApplicationActivityController extends AppCompatActivity {

    private BottomSheetBehavior mLocationsBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        View bottomSheet = (View) findViewById(R.id.locationBottomSheet);


        mLocationsBottomSheetBehavior = LocationsBottomSheetBehavior.from(bottomSheet);



        Button myButton = (Button) findViewById(R.id.createOwnRouteBtn);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivityController.class);
                startActivity(intent);
            }
        });

    }

}

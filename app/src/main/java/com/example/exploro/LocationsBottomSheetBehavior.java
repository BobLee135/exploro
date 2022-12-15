package com.example.exploro;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.exploro.controllers.MapsActivityController;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class LocationsBottomSheetBehavior extends BottomSheetBehavior<android.view.View> {

    public void LocationsFragmentController(Context context) {
        super.setDraggable(true);
        //super.setHideable(false);
        super.setPeekHeight(50);
        Log.d("TOUCHTEST", "TEST");
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        Log.d("TOUCHTEST", "TEST");
        return super.onInterceptTouchEvent(parent, child, event);
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        Log.d("TEST", "HELP");
        return super.onTouchEvent(parent, child, event);
    }
}
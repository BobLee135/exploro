package com.example.exploro;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class LocationsBottomSheetBehavior extends BottomSheetBehavior {

    public LocationsBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void LocationsFragmentController() {
    }

    /** Only allow user to drag up and down bottom sheet by touching the correct component
     *
     * @param parent
     * @param child
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        boolean draggable = false;
        // Check if user is touching the bottom sheet drag button
        // set draggable to true if they are, and false if they are not
        View drag = (View)child.findViewById(R.id.bottomSheetDrag);
        if (drag != null)
            draggable = (parent.isPointInChildBounds(drag, (int) event.getX(), (int) event.getY())) ? true : false;
        super.setDraggable(draggable);

        return super.onInterceptTouchEvent(parent, child, event);
    }
}
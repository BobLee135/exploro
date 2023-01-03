package com.example.exploro.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.exploro.Location;
import com.example.exploro.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class SelectDestinations extends Fragment {

    private int currentNumberOfDestinations;
    private int maxNumberOfDestinations = 6; // (no more than 24) google maps api allows for origin + 23 waypoints + destination in a route
    private ArrayList<Location> currentlySelectedLocations = new ArrayList<Location>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentNumberOfDestinations = 0;
    }

    public static SelectDestinations newInstance() {
        SelectDestinations fragment = new SelectDestinations();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MapsActivityController mac = new MapsActivityController();

        View view = inflater.inflate(R.layout.select_fragment, container, false);

        Button create = (Button) view.findViewById(R.id.createOwnRoute);

        // Create new destination text field
        Button add = (Button) view.findViewById(R.id.addDestination);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Not infinite number of destinations in a route
                if (currentNumberOfDestinations >= maxNumberOfDestinations) return;
                currentNumberOfDestinations++;

                if (currentNumberOfDestinations == maxNumberOfDestinations) {
                    add.setEnabled(false);
                    add.setVisibility(View.INVISIBLE);
                    //add.getBackground().setColorFilter(Color.parseColor("#C0C0C0"), PorterDuff.Mode.MULTIPLY);
                    //add.setTextColor(Color.parseColor("#808080"));
                }

                if (currentNumberOfDestinations > 0)
                    create.setVisibility(View.VISIBLE);
                else
                    create.setVisibility(View.INVISIBLE);

                // Create a layout to add the inputs to
                LinearLayout fieldLayout = new LinearLayout(getActivity());
                fieldLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                fieldLayout.setLayoutParams(fieldParams);

                // Create a new input field
                EditText newDst = new EditText(getActivity());
                newDst.setHint("Route Destination");
                newDst.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_background));
                newDst.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_red_half_transparent));
                newDst.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.text_red_half_transparent));
                newDst.setSingleLine(true);
                newDst.setPadding(dpToPx(10), 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        dpToPx(275),
                        dpToPx(50)
                );
                params.setMargins(0, 0, 0, dpToPx(5));
                newDst.setLayoutParams(params);

                newDst.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (!hasFocus)
                            hideKeyboard(view);
                    }
                });

                // Add a listener to the button
                // When enter is pressed or the user leaves the text input we add a market of the location on the map
                newDst.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT
                                || i == EditorInfo.IME_ACTION_GO || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                                && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            Thread pinThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // Send a request for the location
                                    Location destination = mac.buildPlace(textView.getText().toString());
                                    // Update the list of selected locations
                                    currentlySelectedLocations.add(destination);

                                    // Update the text field with the correct address
                                    newDst.setText(destination.getAddress());

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Pin the location on the map
                                            MarkerOptions markerOptions = new MarkerOptions().position(destination.getLocation()).title(destination.getAddress());
                                            MiniMapFragment.minimap.addMarker(markerOptions);
                                            MiniMapFragment.minimap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination.getLocation(), 15));
                                        }
                                    });

                                }
                            });
                            pinThread.start();

                            return true;
                        }
                        return false;
                    }
                });
                fieldLayout.addView(newDst); // Add the text input to the layout

                // Create a delete button
                Button deleteBtn = new Button(getActivity());
                deleteBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                        dpToPx(35),
                        dpToPx(35)
                );
                btnParams.setMargins(dpToPx(5),0,0,0);
                deleteBtn.setLayoutParams(btnParams);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // When button is pressed, the entire field and button should be removed
                        ViewGroup parentView = (ViewGroup) view.getParent().getParent();
                        parentView.removeView((ViewGroup) view.getParent());

                        currentNumberOfDestinations--;
                        if (currentNumberOfDestinations == 0) {
                            create.setEnabled(false);
                            create.setVisibility(View.INVISIBLE);
                        } else {
                            add.setEnabled(true);
                            add.setVisibility(View.VISIBLE);
                        }

                        // Update the list of selected locations
                        String address = ((TextView)((ViewGroup)view.getParent()).getChildAt(0)).getText().toString();
                        for (int i = 0; i < currentlySelectedLocations.size(); i++) {
                            if (address.compareTo(currentlySelectedLocations.get(i).getAddress()) == 0) {
                                currentlySelectedLocations.remove(i);
                                break;
                            }
                        }

                        // Remove the old marker on the map
                        updateMapMarkers();
                    }
                });
                fieldLayout.addView(deleteBtn); // Add the button to the layout

                // Add the new input and delete button field to the layout
                LinearLayout myLayout = (LinearLayout) view.findViewById(R.id.createRoute);
                myLayout.addView(fieldLayout);
            }
        });


        // Create new route with provided destinations
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Only perform action if we have at least selected one location
                if (currentlySelectedLocations.size() == 0)
                    return;

                // Retrieve all destinations
                String[] destinationList = new String[currentlySelectedLocations.size()];
                for (int i = 0; i < currentlySelectedLocations.size(); i++) {
                    // NOTICE that the input we send to the map is the locations names and not their addresses
                    destinationList[i] = currentlySelectedLocations.get(i).getName();
                }
                shipAndSendRoute(destinationList);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // Method to update all of the markers on the map (from the input fields)
    public void updateMapMarkers() {
        // Remove all markers
        MiniMapFragment.minimap.clear();
        // Add user location marker
        MiniMapFragment.minimap.addMarker(MyLocationListener.userLocationMarker);
        // Add destination markers
        for (int i = 0; i < currentlySelectedLocations.size(); i++) {
            Location current = currentlySelectedLocations.get(i);
            MarkerOptions markerOptions = new MarkerOptions().position(current.getLocation()).title(current.getAddress());
            MiniMapFragment.minimap.addMarker(markerOptions);
        }
    }

    // Method to convert dp to pixels
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void shipAndSendRoute(String[] destinations) {
        Intent intent = new Intent(getActivity(), MapsActivityController.class);
        intent.putExtra("destinationList", destinations);
        startActivity(intent);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

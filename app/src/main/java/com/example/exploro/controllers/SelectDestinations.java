package com.example.exploro.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import okhttp3.Response;

public class SelectDestinations extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SelectDestinations newInstance() {
        SelectDestinations fragment = new SelectDestinations();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MapsActivityController mac = new MapsActivityController();

        View view = inflater.inflate(R.layout.select_fragment, container, false);


        // Create new destination text field
        Button add = (Button) view.findViewById(R.id.addDestination);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new input field
                EditText newDst = new EditText(getActivity());
                newDst.setHint("Route Destination");
                newDst.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_background));
                newDst.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_red_half_transparent));
                newDst.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.text_red_half_transparent));
                newDst.setSingleLine(true);
                newDst.setPadding(dpToPx(10), 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(320), dpToPx(50));
                params.setMargins(dpToPx(20), 0, 0, 0);
                newDst.setLayoutParams(params);

                // When enter is pressed or the user leaves the text input we add a market of the location on the map
                newDst.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT
                                || i == EditorInfo.IME_ACTION_GO || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                                && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            // Send a request for the location
                            Response response = mac.sendRequest(mac.buildPlace(textView.getText().toString()));
                            double lat = 0.0;
                            double lng = 0.0;
                            String name = "Location not found";
                            try {
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                lat = jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                lng = jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                name = jsonResponse.getJSONArray("candidates").getJSONObject(0).getString("formatted_address");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Pin the location on the map
                            LatLng search = new LatLng(lat, lng);
                            MarkerOptions markerOptions = new MarkerOptions().position(search).title(name);
                            MiniMapFragment.minimap.addMarker(markerOptions);
                            MiniMapFragment.minimap.moveCamera(CameraUpdateFactory.newLatLngZoom(search, 10));

                            return true;
                        }
                        return false;
                    }
                });

                // Add the new input field to the layout
                LinearLayout myLayout = (LinearLayout)view.findViewById(R.id.createRoute);
                myLayout.addView(newDst);
            }
        });


        // Create new route with provided destinations
        Button create = (Button) view.findViewById(R.id.createOwnRoute);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Retrieve all destinations
                ViewGroup layout = (ViewGroup) view.findViewById(R.id.createRoute);
                // Loop through each of the views in the layout
                String[] destinationList = new String[layout.getChildCount()];
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View view = layout.getChildAt(i);

                    if (view instanceof EditText) {
                        destinationList[i] = ((EditText) view).getText().toString();
                    }
                }
                // Add all destinations as a bundle and send to the new activity
                Bundle bundle = new Bundle();
                bundle.putStringArray("destinationList", destinationList);
                Intent intent = new Intent(getActivity(), MapsActivityController.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    // Method to convert dp to pixels
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}

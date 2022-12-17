package com.example.exploro.controllers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.exploro.R;

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

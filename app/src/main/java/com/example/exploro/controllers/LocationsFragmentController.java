package com.example.exploro.controllers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exploro.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragmentController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragmentController extends BottomSheetDialogFragment {

    private MessageHelper messageHelper;

    public LocationsFragmentController() {
        // Required empty public constructor
        messageHelper = new MessageHelper();
    }

    /**
     * @return A new instance of fragment LocationsFragmentController.
     */
    public static LocationsFragmentController newInstance() {
        LocationsFragmentController fragment = new LocationsFragmentController();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get Objects
        View view = inflater.inflate(R.layout.locations_fragment, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Inflate the layout for this fragment
        return view;
    }

}
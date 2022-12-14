package com.example.exploro.controllers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.exploro.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragmentController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragmentController extends Fragment {

    public RegisterFragmentController() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragmentController newInstance() {
        RegisterFragmentController fragment = new RegisterFragmentController();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        Button cancelRegisterBtn = (Button)view.findViewById(R.id.cancelRegistrationBtn);

        LoginFragmentController loginFragmentController = new LoginFragmentController();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        /**
         * Cancel register button listener
         * Go back to login page
         */
        cancelRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, loginFragmentController, "loginFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
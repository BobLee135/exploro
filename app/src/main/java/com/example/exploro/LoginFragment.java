package com.example.exploro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        Button registerBtn = (Button)view.findViewById(R.id.registerBtn);
        Button loginBtn = (Button)view.findViewById(R.id.loginBtn);

        RegisterFragment registerFragment = new RegisterFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        /**
         *  Register button listener
         *  Open register page
         */
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, registerFragment, "registerFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


        /**
        * Login button listener
        * Launches application acitivty
         *
        * TODO: Handle user input and authenticate user. If authentication failed show error message.
         * Otherwise launch application activity and login user.
         */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ApplicationActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
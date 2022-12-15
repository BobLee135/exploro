package com.example.exploro.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.exploro.MessageHelper;
import com.example.exploro.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragmentController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragmentController extends Fragment {

    private MessageHelper messageHelper;

    public LoginFragmentController() {
        // Required empty public constructor
        messageHelper = new MessageHelper();
    }

    /**
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragmentController newInstance() {
        LoginFragmentController fragment = new LoginFragmentController();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get Objects
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        Button registerBtn = (Button)view.findViewById(R.id.registerBtn);
        Button loginBtn = (Button)view.findViewById(R.id.loginBtn);

        EditText usernameInput = (EditText) view.findViewById(R.id.usernameInputField);
        EditText passwordInput = (EditText) view.findViewById(R.id.passwordInputField);

        RegisterFragmentController registerFragmentController = new RegisterFragmentController();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        /**
         *  Register button listener
         *  Open register page
         */
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, registerFragmentController, "registerFragment")
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
                // Validate input data
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (!isValid(username, password)) {
                    messageHelper.displaySnackbar("Invalid input", 3, "Error", v);
                    return;
                }

                // TODO: Authenticate user through UserModel

                // Empty input fields
                usernameInput.setText("");
                passwordInput.setText("");


                // Launch app activity
                startActivity(new Intent(getActivity(), ApplicationActivityController.class));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Validate if user input is valid for login form
     *
     * @param username - username input
     * @param password - password input
     * @return true if input is valid, false if input is not valid
     */
    public boolean isValid(String username, String password) {
        if (username.length() == 0 || password.length() == 0)
            return false;
        return true;
    }
}
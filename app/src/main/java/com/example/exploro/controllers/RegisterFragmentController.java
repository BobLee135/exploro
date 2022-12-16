package com.example.exploro.controllers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.exploro.MessageHelper;
import com.example.exploro.R;
import com.example.exploro.models.UserModel;
import com.example.exploro.models.schemas.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragmentController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragmentController extends Fragment {

    private MessageHelper mMessageHelper;

    public RegisterFragmentController() {
        // Required empty public constructor
        mMessageHelper = new MessageHelper();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        // Get views
        Button cancelRegisterBtn = (Button)view.findViewById(R.id.cancelRegistrationBtn);
        Button registerBtn = (Button)view.findViewById(R.id.registerButton);

        EditText nameInputField = (EditText) view.findViewById(R.id.fullNameInputField);
        EditText emailInputField = (EditText) view.findViewById(R.id.emailnputField);
        EditText usernameInputField = (EditText) view.findViewById(R.id.registerUsernameInputField) ;
        EditText passwordInputField = (EditText) view.findViewById(R.id.registerPasswordInputField);
        EditText confirmPasswordInputField = (EditText) view.findViewById(R.id.registerConfirmPasswordInputField);


        LoginFragmentController loginFragmentController = new LoginFragmentController();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        UserModel userModel = new UserModel(getActivity());

        /**
         * Start process of creating a new user from input data
         */
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values
                String fullName = nameInputField.getText().toString();
                String email = emailInputField.getText().toString();
                String username = usernameInputField.getText().toString();
                String password = passwordInputField.getText().toString();
                String confirmPassword = confirmPasswordInputField.getText().toString();

                // Check data validity
                if (!isValid(fullName, email, username, password, confirmPassword)) {
                    mMessageHelper.displaySnackbar("Invalid input", 3, "Error", v);
                    return;
                }


                // Check if username is available
                userModel.findUsernameInUsers(username, new UserModel.ResultStatus() {
                    @Override
                    public void resultLoaded(List<User> users) {
                        if (!users.isEmpty() && users.get(0) != null) {
                            Log.d("RESPONSE", users.get(0).getName().toString());
                            mMessageHelper.displaySnackbar("Username already taken", 3, "Error", v);
                            return;
                        }

                        // Check if email is available
                        userModel.findEmailInUsers(email, new UserModel.ResultStatus() {
                            @Override
                            public void resultLoaded(List<User> users) {
                                if (!users.isEmpty() && users.get(0) != null) {
                                    Log.d("RESPONSE", users.get(0).getEmail().toString());
                                    mMessageHelper.displaySnackbar("Email already taken", 3, "Error", v);
                                    return;
                                }
                                // Create new user in database
                                User user = userModel.createNewUser(fullName, username, email, password);
                                swapFragment(fragmentManager, loginFragmentController);
                                mMessageHelper.displaySnackbar("User registered", 3, "Correct", v);
                            }
                        });
                    }
                });
            }
        });

        /**
         * Cancel register button listener
         * Go back to login page
         */
        cancelRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(fragmentManager, loginFragmentController);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     *  Validate user input from registration form
     *
     * @param fullName - User input name
     * @param email - User input email
     * @param username - User input username
     * @param password - User input password
     * @param confirmPassword - User input confirm password
     * @return true if input passes validation, otherwise false
     */
    private boolean isValid(String fullName, String email, String username, String password, String confirmPassword) {
        if (fullName.length() == 0
                || email.length() == 0
                || username.length() == 0
                || password.length() == 0
                || confirmPassword.length() == 0)
            return false;
        return true;
    }

    /**
     * Change active fragment
     *
     * @param fm - FragmentManager
     * @param fragmentController - Fragment controller to swap to
     */
    private void swapFragment(FragmentManager fm, Fragment fragmentController) {
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentController, "loginFragment")
                .addToBackStack(null)
                .commit();
    }

}
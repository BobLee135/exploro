package com.example.exploro.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MessageHelper {

    /**
     * Display a message in a snackbar component at the bottom of the screen
     *
     * @param message - Message to display
     * @param duration - Time snackbar is displayed in seconds
     * @param type - Defines if snackbar should be red (Error) or green (Correct)
     * @param view -
     */
    public void displaySnackbar(String message, int duration, String type, View view) {
        // Validate parameters
        if (message.length() == 0)
            return;
        if (!type.matches("Error|Correct"))
            return;

        duration = duration * 1000; // Convert to seconds

        // Create Snackbar
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.show();
    }

}

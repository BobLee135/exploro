package com.example.exploro.controllers;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.exploro.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrationUiTests {

    @Rule
    public ActivityScenarioRule<MainActivityController> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivityController.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");
    @Test
    public void appStart() {
        ViewInteraction button = onView(
                allOf(withId(R.id.registerBtn), withText("NEW USER? CREATE ACCOUNT"),
                        withParent(withParent(withId(R.id.fragmentContainer))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }

    @Test
    public void registrationToLogin() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.registerBtn), withText("New User? Create Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentContainer),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.cancelRegistrationBtn), withText("Already A User? Log In"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentContainer),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.loginBtn), withText("LOG IN"),
                        withParent(withParent(withId(R.id.fragmentContainer))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }
    @Test
    public void registrationEmptyUser() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.registerBtn), withText("New User? Create Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentContainer),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.registerButton), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentContainer),
                                        0),
                                6),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.cancelRegistrationBtn), withText("ALREADY A USER? LOG IN"),
                        withParent(withParent(withId(R.id.fragmentContainer))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }
    @Test
    public void registrationClick() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.registerBtn), withText("New User? Create Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentContainer),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.registerButton), withText("REGISTER"),
                        withParent(withParent(withId(R.id.fragmentContainer))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

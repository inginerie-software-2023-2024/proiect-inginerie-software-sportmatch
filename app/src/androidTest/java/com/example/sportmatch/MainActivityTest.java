package com.example.sportmatch;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();  // Clear any existing data
    }

    @Test
    public void testMainActivityComponents() {
        // check if components are displayed
        Espresso.onView(withId(R.id.backG)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.splashtext)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.hometext)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.bottons)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.googleLogo)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginButtonNavigation() {

        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());

        Intents.intended(hasComponent(LoginActivity.class.getName()));
    }

}

package com.example.sportmatch;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.dx.command.Main;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {


    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private ActivityScenario<MainActivity> activityScenario;
    @Before
    public void setUp() {
        Intents.init();
        activityScenario = activityScenarioRule.getScenario();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();  // Clear any existing data
    }

    @After
    public void tearDown() {
        Intents.release();
        activityScenario.close();
    }



    @Test
    public void testLoginWithValidCredentialsNavigatesToBottomNavActivity() throws InterruptedException {
        // Launch the activity
        activityScenario = activityScenarioRule.getScenario();
        Espresso.onView(withId(R.id.backG)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.hometext)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.bottons)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.googleLogo)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());

        Intents.intended(IntentMatchers.hasComponent(LoginActivity.class.getName()));

        // Enter valid credentials
        Espresso.onView(withId(R.id.activity_main_usernameEditText))
                .perform(ViewActions.typeText("buna@yahoo.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.activity_main_passwordEditText))
                .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());


    }


}
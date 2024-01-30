package com.example.sportmatch;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.android.dx.command.Main;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    private ActivityScenario<MainActivity> activityScenario;
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
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
    public void testMainActivityComponents() {
        // check if components are displayed
        Espresso.onView(withId(R.id.backG)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.hometext)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.bottons)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.googleLogo)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        intended(hasComponent(LoginActivity.class.getName()));

        Espresso.onView(withId(R.id.cardViewLogin)).check(matches(isDisplayed()));
    }

}

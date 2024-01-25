package com.example.sportmatch;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import androidx.test.rule.ActivityTestRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.sportmatch", appContext.getPackageName());
    }
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void loginTest() {
        // Type the email address
        Espresso.onView(withId(R.id.activity_main_usernameEditText))
                .perform(typeText("buna@yahoo.com"), ViewActions.closeSoftKeyboard());

        // Type the password
        Espresso.onView(withId(R.id.activity_main_passwordEditText))
                .perform(typeText("123456"), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());

        Espresso.onView(withText("Login successful!")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

//    @Rule
//    public ActivityTestRule<CreateEventActivity> activityRule = new ActivityTestRule<>(CreateEventActivity.class);

//    @Test
//    public void testCreateEventActivity() {

//
//
//// waits for the activity to be ready
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            onView(withId(R.id.newEventNameEdt)).perform(ViewActions.typeText("Test Event"));
//            onData(anything())
//                    .inAdapterView(withId(R.id.autocomplete_sport))
//                    .atPosition(0)
//                    .perform(click());
//            onData(anything())
//                    .inAdapterView(withId(R.id.autocomplete_loc))
//                    .atPosition(0)
//                    .perform(click());
//            onData(anything())
//                    .inAdapterView(withId(R.id.autocomplete_players))
//                    .atPosition(0)
//                    .perform(click());
//            onView(withId(R.id.newEventDescEdt)).perform(ViewActions.typeText("test description"));
//
//            //picks date
//            onView(withId(R.id.newEventDateEdt)).perform(click());
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(2024, Calendar.JANUARY, 1);
//
//            // performs the date selection
//            onView(ViewMatchers.withClassName(Matchers.equalTo(android.widget.DatePicker.class.getName())))
//                    .perform(PickerActions.setDate(
//                            calendar.get(Calendar.YEAR),
//                            calendar.get(Calendar.MONTH) + 1, // Months are zero-based in Calendar
//                            calendar.get(Calendar.DAY_OF_MONTH)
//                    ));
//
//            // confirms the date selection
//            onView(withId(android.R.id.button1)).perform(click());
//
//            // verifies that the selected date is displayed in the EditText
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            String expectedDate = formatter.format(calendar.getTime());
//            onView(withId(R.id.newEventDateEdt)).check(matches(withText(expectedDate)));
//
//            //pick the time
//            onView(withId(R.id.newEventTimeEdt)).perform(click());
//
//            //selects a time
//            calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 15);
//            calendar.set(Calendar.MINUTE, 30);
//
//            //performs the time selection
//            onView(ViewMatchers.withClassName(Matchers.equalTo(android.widget.TimePicker.class.getName())))
//                    .perform(PickerActions.setTime(
//                            calendar.get(Calendar.HOUR_OF_DAY),
//                            calendar.get(Calendar.MINUTE)
//                    ));
//
//            //confirms the time selection
//            onView(withId(android.R.id.button1)).perform(click());
//
//            //verify that the selected time is displayed in the EditText
//            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
//            String expectedTime = formatterTime.format(calendar.getTime());
//            onView(withId(R.id.newEventTimeEdt)).check(matches(withText(expectedTime)));
//
//
//            onView(withId(R.id.buttonCEvent)).perform(click());
//
//            //wait for the actions to finish
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            onView(withId(R.id.previewBtnAddEv)).perform(click());
//
//            onView(withText("Event added successfully"))
//                    .inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView())))
//                    .check(matches(isDisplayed()));
//
//

//    }

}
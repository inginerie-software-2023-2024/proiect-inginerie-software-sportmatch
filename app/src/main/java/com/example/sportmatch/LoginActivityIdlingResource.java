package com.example.sportmatch;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.test.espresso.IdlingResource;
import androidx.test.platform.app.InstrumentationRegistry;

import com.bumptech.glide.request.ResourceCallback;

public class LoginActivityIdlingResource implements IdlingResource {
    private volatile ResourceCallback resourceCallback;

    @Override
    public String getName() {
        return LoginActivityIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        // Implement your logic to check if LoginActivity is in the foreground
        boolean isIdle = isLoginActivityVisible();

        if (isIdle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }

        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    private boolean isLoginActivityVisible() {

        return true;
    }

    private Activity getCurrentActivity(Application application) {
        final Activity[] currentActivity = new Activity[1];

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity[0] = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });

        return currentActivity[0];
    }
}
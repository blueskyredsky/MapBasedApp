package com.reza.places;

import android.content.Context;
import android.util.Log;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.reza.location.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultPlacesManager implements PlacesManager {

    private final Context context;
    private static final String TAG = "DefaultPlacesManagerTAG";

    @Inject
    public DefaultPlacesManager(Context context) {
        this.context = context;
    }

    @Override
    public PlacesClient getPlacesClient() {
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.GOOGLE_MAP_API_KEY);
            try {
                Places.createClient(context); //Verify initialization
            } catch (Exception e) {
                Log.e(TAG, "Places initialization failed: " + e.getMessage());
            }
        }
        return Places.createClient(context);
    }
}

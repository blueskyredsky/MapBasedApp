package com.reza.di;

import android.content.Context;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.reza.location.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = PlacesAbstractModule.class)
public class PlacesModule {

    @Provides
    @Singleton
    public PlacesClient providePlaces(Context context) {
        Places.initialize(context, BuildConfig.GOOGLE_MAP_API_KEY);
        return Places.createClient(context);
    }
}

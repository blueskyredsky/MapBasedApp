package com.reza.mapapp;

import android.app.Application;

import com.reza.map.data.di.MapComponent;
import com.reza.map.data.di.MapComponentProvider;
import com.reza.mapapp.di.ApplicationComponent;
import com.reza.mapapp.di.DaggerApplicationComponent;

public class MapApplication extends Application implements MapComponentProvider {

    // Reference to the application graph that is used across the whole app
    ApplicationComponent appComponent = DaggerApplicationComponent.factory().create(this);

    @Override
    public MapComponent provideMapComponent() {
        return appComponent.mapComponent().create();
    }
}

package com.reza.mapapp;

import android.app.Application;

import com.reza.details.di.DetailsComponent;
import com.reza.details.di.DetailsComponentProvider;
import com.reza.map.di.MapComponent;
import com.reza.map.di.MapComponentProvider;
import com.reza.mapapp.di.ApplicationComponent;
import com.reza.mapapp.di.DaggerApplicationComponent;

public class MapApplication extends Application implements MapComponentProvider, DetailsComponentProvider {

    // Reference to the application graph that is used across the whole app
    //ApplicationComponent appComponent = DaggerApplicationComponent.factory().create(getApplicationContext());

    private ApplicationComponent appComponent;

    public ApplicationComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerApplicationComponent.factory().create(getApplicationContext());
        }
        return appComponent;
    }

    @Override
    public MapComponent provideMapComponent() {
        return getAppComponent().mapComponent().create();
    }

    @Override
    public DetailsComponent provideDetailsComponent() {
        return getAppComponent().detailsComponent().create();
    }
}

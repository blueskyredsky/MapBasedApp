package com.reza.di;

import com.reza.places.DefaultPlacesManager;
import com.reza.places.PlacesManager;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PlacesModule {

    @Binds
    abstract PlacesManager bindPlacesManager(DefaultPlacesManager defaultPlacesManager);
}

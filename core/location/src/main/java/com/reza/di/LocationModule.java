package com.reza.di;

import com.reza.location.DefaultLocationManager;
import com.reza.location.LocationManager;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LocationModule {

    @Binds
    abstract LocationManager bindLocationManager(DefaultLocationManager defaultLocationManager);
}

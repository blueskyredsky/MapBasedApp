package com.reza.mapapp.di;

import com.reza.details.di.DetailsComponent;
import com.reza.map.di.MapComponent;

import dagger.Module;

@Module(subcomponents = {MapComponent.class, DetailsComponent.class})
public class SubComponentModule {
}

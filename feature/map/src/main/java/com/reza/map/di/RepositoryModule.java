package com.reza.map.di;

import com.reza.map.data.repository.location.DefaultLocationRepository;
import com.reza.map.data.repository.location.LocationRepository;
import com.reza.map.data.repository.place.DefaultPlaceRepository;
import com.reza.map.data.repository.place.PlaceRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    abstract LocationRepository bindLocationRepository(DefaultLocationRepository defaultLocationRepository);

    @Binds
    abstract PlaceRepository bindPlaceRepository(DefaultPlaceRepository defaultPlaceRepository);
}

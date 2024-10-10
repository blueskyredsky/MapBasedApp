package com.reza.map.data.di;

import com.reza.map.data.repository.DefaultLocationRepository;
import com.reza.map.data.repository.LocationRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    abstract LocationRepository bindRepository(DefaultLocationRepository defaultLocationRepository);
}

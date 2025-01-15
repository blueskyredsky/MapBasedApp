package com.reza.map.data.di;

import com.reza.map.data.repository.bookmark.BookmarkRepository;
import com.reza.map.data.repository.bookmark.DefaultBookmarkRepository;
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

    @Binds
    abstract BookmarkRepository bindBookmarkRepository(DefaultBookmarkRepository defaultBookmarkRepository);
}

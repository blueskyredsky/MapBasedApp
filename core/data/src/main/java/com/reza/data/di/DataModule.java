package com.reza.data.di;

import com.reza.data.repository.BookmarkRepository;
import com.reza.data.repository.DefaultBookmarkRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DataModule {

    @Binds
    abstract BookmarkRepository bindBookmarkRepository(DefaultBookmarkRepository defaultBookmarkRepository);
}

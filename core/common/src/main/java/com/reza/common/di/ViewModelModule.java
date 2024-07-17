package com.reza.common.di;

import androidx.lifecycle.ViewModelProvider;

import com.reza.common.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}

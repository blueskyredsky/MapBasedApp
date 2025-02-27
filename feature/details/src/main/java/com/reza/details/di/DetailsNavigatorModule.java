package com.reza.details.di;

import com.reza.common.navigator.ActivityNavigator;
import com.reza.details.navigation.DetailsActivityNavigator;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DetailsNavigatorModule {
    @Binds
    abstract ActivityNavigator bindActivityNavigator(DetailsActivityNavigator detailsActivityNavigator);
}

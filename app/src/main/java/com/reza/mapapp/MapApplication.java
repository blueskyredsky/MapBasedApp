package com.reza.mapapp;

import android.app.Application;

import com.reza.mapapp.di.ApplicationComponent;
import com.reza.mapapp.di.DaggerApplicationComponent;

public class MapApplication extends Application {

    ApplicationComponent appComponent = DaggerApplicationComponent.factory().create(this);
}

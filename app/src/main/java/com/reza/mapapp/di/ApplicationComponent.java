package com.reza.mapapp.di;

import android.content.Context;

import com.reza.di.LocationModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {LocationModule.class})
public interface ApplicationComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        ApplicationComponent create(@BindsInstance Context context);
    }
}

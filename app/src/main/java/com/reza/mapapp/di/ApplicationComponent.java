package com.reza.mapapp.di;

import android.content.Context;

import com.reza.common.di.CommonModule;
import com.reza.di.LocationModule;
import com.reza.threading.di.ThreadingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {LocationModule.class, ThreadingModule.class, CommonModule.class})
public interface ApplicationComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        ApplicationComponent create(@BindsInstance Context context);
    }
}

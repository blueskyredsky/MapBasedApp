package com.reza.mapapp.di;

import android.content.Context;

import com.reza.common.di.CommonModule;
import com.reza.common.di.ViewModelModule;
import com.reza.di.LocationModule;
import com.reza.di.PlacesModule;
import com.reza.map.data.di.MapComponent;
import com.reza.threading.di.ThreadingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        LocationModule.class,
        PlacesModule.class,
        ThreadingModule.class,
        CommonModule.class,
        ViewModelModule.class,
        SubComponentModule.class
})
public interface ApplicationComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        ApplicationComponent create(@BindsInstance Context context);
    }

    MapComponent.Factory mapComponent();
}

package com.reza.map.data.di;

import com.reza.common.di.scope.ActivityScope;
import com.reza.map.ui.MapActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {MapViewModelModule.class})
public interface MapComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        MapComponent create();
    }

    void inject(MapActivity activity);
}

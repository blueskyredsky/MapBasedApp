package com.reza.details.di;

import com.reza.common.di.scope.ActivityScope;
import com.reza.details.ui.DetailsActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {DetailsViewModelModule.class})
public interface DetailsComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        DetailsComponent create();
    }

    void inject(DetailsActivity activity);
}

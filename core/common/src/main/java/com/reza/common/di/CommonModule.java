package com.reza.common.di;

import com.reza.common.util.imagehelper.DefaultImageHelper;
import com.reza.common.util.imagehelper.ImageHelper;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public abstract class CommonModule {

    @Provides
    static CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Binds
    abstract ImageHelper bindImageHelper(DefaultImageHelper defaultImageHelper);
}

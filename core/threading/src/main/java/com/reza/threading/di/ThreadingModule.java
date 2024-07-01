package com.reza.threading.di;

import com.reza.threading.schedulers.ComputationScheduler;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@Module
public class ThreadingModule {

    @Provides
    @Singleton
    @ComputationScheduler
    public Scheduler provideComputationScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Singleton
    @IoScheduler
    public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @MainScheduler
    public Scheduler provideAndroidScheduler() {
        return AndroidSchedulers.mainThread();
    }
}

package com.reza.details.ui;

import androidx.lifecycle.ViewModel;

import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import javax.inject.Inject;

import io.reactivex.Scheduler;

public class DetailsViewModel extends ViewModel {

    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    @Inject
    public DetailsViewModel(@IoScheduler Scheduler ioScheduler,
                            @MainScheduler Scheduler mainScheduler) {
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }
}

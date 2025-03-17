package com.reza.details.ui;

import androidx.lifecycle.ViewModel;

import com.reza.data.repository.BookmarkRepository;
import com.reza.database.BookmarkEntity;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class DetailsViewModel extends ViewModel {

    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;
    private final BookmarkRepository bookmarkRepository;

    @Inject
    public DetailsViewModel(@IoScheduler Scheduler ioScheduler,
                            @MainScheduler Scheduler mainScheduler,
                            BookmarkRepository bookmarkRepository) {
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.bookmarkRepository = bookmarkRepository;
    }

    Single<BookmarkEntity> getBookmark(Long bookmarkId) {
        return bookmarkRepository.getBookmark(bookmarkId);
    }
}

package com.reza.details.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reza.common.util.imagehelper.ImageHelper;
import com.reza.data.repository.BookmarkRepository;
import com.reza.database.BookmarkEntity;
import com.reza.details.data.model.BookmarkDetailsView;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = "DetailsViewModelTag";

    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;
    private final CompositeDisposable compositeDisposable;
    private final BookmarkRepository bookmarkRepository;
    private final ImageHelper imageHelper;

    private final MutableLiveData<BookmarkDetailsView> _bookmarks = new MutableLiveData<>();
    LiveData<BookmarkDetailsView> bookmarks = _bookmarks;

    private final MutableLiveData<Boolean> _isSavingDone = new MutableLiveData<>();
    LiveData<Boolean> isSavingDone = _isSavingDone;

    @Inject
    public DetailsViewModel(@IoScheduler Scheduler ioScheduler,
                            @MainScheduler Scheduler mainScheduler,
                            BookmarkRepository bookmarkRepository,
                            CompositeDisposable compositeDisposable,
                            ImageHelper imageHelper) {
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.bookmarkRepository = bookmarkRepository;
        this.compositeDisposable = compositeDisposable;
        this.imageHelper = imageHelper;
    }

    Single<File> createUniqueImageFile() {
        return imageHelper.createUniqueImageFile();
    }

    public void loadBookmark(Long bookmarkId) {
        compositeDisposable.add(
                getBookmark(bookmarkId)
                        .map(this::bookmarkEntityToBookmarkDetailsView)
                        .flatMap(bookmarkDetailsView -> imageHelper.loadBitmapFromFile(imageHelper.generateImageFilename(bookmarkId))
                                .map(bitmap -> {
                                    bookmarkDetailsView.setImage(bitmap);
                                    return bookmarkDetailsView;
                                }).toSingle())
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(_bookmarks::setValue, throwable -> {
                            Log.e(TAG, "loadBookmark: " + throwable);
                        })
        );
    }

    private BookmarkDetailsView bookmarkEntityToBookmarkDetailsView(BookmarkEntity bookmarkEntity) {
        return new BookmarkDetailsView(
                bookmarkEntity.getAddress(),
                bookmarkEntity.getNotes(),
                bookmarkEntity.getName(),
                bookmarkEntity.getPhone(),
                null
        );
    }

    public void updateBookmark(BookmarkDetailsView bookmarkDetailsView, Long bookmarkId) {
        compositeDisposable.add(
                getBookmark(bookmarkId)
                        .map(bookmarkEntity -> bookmarkDetailsViewToBookmarkEntity(bookmarkDetailsView, bookmarkEntity))
                        .flatMapCompletable(bookmarkRepository::updateBookmark)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .doOnComplete(() -> {
                            _isSavingDone.setValue(true);
                        })
                        .doOnError((throwable) -> {
                            _isSavingDone.setValue(false);
                            Log.e(TAG, "updateBookmark: " + throwable);
                        })
                        .subscribe()
        );
    }

    private BookmarkEntity bookmarkDetailsViewToBookmarkEntity(BookmarkDetailsView bookmarkDetailsView, BookmarkEntity bookmarkEntity) {
        return new BookmarkEntity(
                bookmarkEntity.getId(),
                bookmarkEntity.getPlaceId(),
                bookmarkDetailsView.getName(),
                bookmarkDetailsView.getAddress(),
                bookmarkEntity.getLatitude(),
                bookmarkEntity.getLongitude(),
                bookmarkDetailsView.getPhoneNumber(),
                bookmarkDetailsView.getNotes(),
                bookmarkEntity.getCategory()); // fixme added temporarily
    }

    private Single<BookmarkEntity> getBookmark(Long bookmarkId) {
        return bookmarkRepository.getBookmark(bookmarkId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

package com.reza.data.repository;

import com.reza.database.BookmarkDao;
import com.reza.database.BookmarkEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DefaultBookmarkRepository implements BookmarkRepository {

    private final BookmarkDao bookmarkDao;

    @Inject
    public DefaultBookmarkRepository(BookmarkDao bookmarkDao) {
        this.bookmarkDao = bookmarkDao;
    }

    @Override
    public Flowable<List<BookmarkEntity>> getAllBookmarks() {
        return bookmarkDao.getAllBookmarks();
    }

    @Override
    public Single<BookmarkEntity> getBookmark(Long bookmarkId) {
        return bookmarkDao.getBookmark(bookmarkId);
    }

    @Override
    public Single<BookmarkEntity> getBookmark(String placeId) {
        return bookmarkDao.getBookmark(placeId);
    }

    @Override
    public Single<Long> addBookmark(BookmarkEntity bookmark) {
        return bookmarkDao.addBookmark(bookmark);
    }

    @Override
    public Completable updateBookmark(BookmarkEntity bookmark) {
        return bookmarkDao.updateBookmark(bookmark);
    }

    @Override
    public Completable deleteBookmark(BookmarkEntity bookmark) {
        return bookmarkDao.deleteBookmark(bookmark);
    }
}
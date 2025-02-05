package com.reza.map.data.repository.bookmark;

import com.reza.database.BookmarkEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * A repository interface for managing bookmarks.
 */
public interface BookmarkRepository {

    /**
     * Retrieves all bookmarks.
     */
    Flowable<List<BookmarkEntity>> getAllBookmarks();

    /**
     * Retrieves a bookmark by its ID.
     */
    Single<BookmarkEntity> getBookmark(Long bookmarkId);

    /**
     * Retrieves a bookmark by its place ID.
     */
    Single<BookmarkEntity> getBookmark(String placeId);

    /**
     * Adds a new bookmark.
     */
    Single<Long> addBookmark(BookmarkEntity bookmark);

    /**
     * Updates an existing bookmark.
     */
    Completable updateBookmark(BookmarkEntity bookmark);

    /**
     * Deletes a bookmark.
     */
    Completable deleteBookmark(BookmarkEntity bookmark);
}

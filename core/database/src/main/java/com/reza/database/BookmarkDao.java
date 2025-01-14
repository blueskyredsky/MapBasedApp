package com.reza.database;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface BookmarkDao {

    @Query("SELECT * FROM bookmark")
    Flowable<List<BookmarkEntity>> loadAllBookmarksFlowable();

    @Query("SELECT * FROM bookmark WHERE id = :bookmarkId")
    Single<BookmarkEntity> loadBookmarkSingle(Long bookmarkId);

    @Query("SELECT * FROM bookmark WHERE place_id = :placeId")
    Single<BookmarkEntity> loadBookmarkSingle(String placeId);

    @Insert(onConflict = IGNORE)
    Completable insertBookmark(BookmarkEntity bookmark);

    @Update(onConflict = REPLACE)
    Completable updateBookmark(BookmarkEntity bookmark);

    @Delete
    Completable deleteBookmark(BookmarkEntity bookmark);
}

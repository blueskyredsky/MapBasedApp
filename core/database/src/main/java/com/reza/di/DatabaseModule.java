package com.reza.di;

import android.content.Context;

import androidx.room.Room;

import com.reza.database.BookmarkDao;
import com.reza.database.PlaceBookDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    public static PlaceBookDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(
                        context.getApplicationContext(),
                        PlaceBookDatabase.class,
                        "placeBook_database"
                )
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    public static BookmarkDao provideBookmarkDao(PlaceBookDatabase database) {
        return database.bookmarkDao();
    }
}

package com.reza.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BookmarkEntity.class}, version = 2, exportSchema = false)
public abstract class PlaceBookDatabase extends RoomDatabase {

    public abstract BookmarkDao bookmarkDao();
}

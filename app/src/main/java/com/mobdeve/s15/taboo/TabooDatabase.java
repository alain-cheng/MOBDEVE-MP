package com.mobdeve.s15.taboo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PlayerData.class, Treasure.class}, version = 1, exportSchema = false)
public abstract class TabooDatabase extends RoomDatabase {

    public abstract DAO TabooDao();

    private static volatile TabooDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TabooDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TabooDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TabooDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

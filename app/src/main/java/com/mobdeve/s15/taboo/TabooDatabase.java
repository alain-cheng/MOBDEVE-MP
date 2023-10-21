package com.mobdeve.s15.taboo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PlayerData.class, Treasure.class}, version = 1, exportSchema = false)
public abstract class TabooDatabase extends RoomDatabase {

    public abstract DAO TabooDao();

    private static volatile TabooDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                DAO dao = INSTANCE.TabooDao();
                dao.deletePlayer();
                dao.deleteTreasures();

                PlayerData playerData = new PlayerData(0, 1, 0, 1, 0, 0);
                dao.updatePlayer(playerData);
            });
        }
    };

    static TabooDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TabooDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TabooDatabase.class, "taboo_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}

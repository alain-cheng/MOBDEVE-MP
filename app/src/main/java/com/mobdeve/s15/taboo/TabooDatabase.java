package com.mobdeve.s15.taboo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Random;
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

                //Load Player Data
                PlayerData playerData = new PlayerData(0, 1, 0, 1, 0, 0);

                //TEST: Load Treasury Data. This should be loaded only if you get the item but testing stuff for now.
                Treasure treasure;
                Random rand = new Random(System.nanoTime());
                for(int i = 0; i < TreasureList.names.length; i++){
                    treasure = new Treasure("item" + i+1, TreasureList.names[i], TreasureList.images[0],
                            TreasureList.bonuses[0], TreasureList.lores[i], TreasureList.rarities[i], rand.nextInt(5));
                    switch (treasure.getRarity()){
                        case "COMMON":{
                            playerData.setBounty(playerData.getBounty() + 5*treasure.getCount());
                            break;
                        }
                        case "RARE":{
                            playerData.setBounty(playerData.getBounty() + 10*treasure.getCount());
                            break;
                        }
                        case "FORBIDDEN":{
                            playerData.setBounty(playerData.getBounty() + 25*treasure.getCount());
                            break;
                        }
                        case "BLASPHEMY":{
                            playerData.setBounty(playerData.getBounty() + 50*treasure.getCount());
                        }
                    }
                    dao.updateTreasury(treasure);
                }

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

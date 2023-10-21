package com.mobdeve.s15.taboo;

import android.app.Application;
import android.widget.Switch;

import androidx.lifecycle.LiveData;

import java.util.List;

class DataRepository {

    private DAO mTabooDao;
    private LiveData<List<Treasure>> mTreasury;
    private LiveData<PlayerData> mPlayerData;

    DataRepository(Application application) {
        TabooDatabase db = TabooDatabase.getDatabase(application);
        mTabooDao = db.TabooDao();
        mPlayerData = mTabooDao.getPlayerData();
        mTreasury = mTabooDao.getTreasury();
    }

    LiveData<List<Treasure>> getTreasury() {
        return mTreasury;
    }

    LiveData<PlayerData> getPlayerData(){
        return mPlayerData;
    }

    void updatePlayer(PlayerData playerData) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            playerData.setId(); //Set id to 0
            mTabooDao.updatePlayer(playerData);
        });
    }
    void updateTreasury(Treasure treasure) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            mTabooDao.updateTreasury(treasure);
        });
    }

    void deleteData() {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            mTabooDao.deletePlayer();
            mTabooDao.deleteTreasures();
            mTabooDao.updatePlayer(new PlayerData(0, 1, 0, 1, 0, 0));
        });
    }
}

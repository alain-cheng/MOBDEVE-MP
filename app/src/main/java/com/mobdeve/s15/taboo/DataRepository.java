package com.mobdeve.s15.taboo;

import android.app.Application;

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
    void updateTreasury(Treasure treasure, PlayerData playerData) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            //Calculate new bounty
            playerData.setId(); //Set id to 0
            int newBounty = 0;
            switch (treasure.getRarity()){
                case "COMMON":{
                    playerData.setBounty(playerData.getBounty() + 5);
                    break;
                }
                case "RARE":{
                    playerData.setBounty(playerData.getBounty() + 10);
                    break;
                }
                case "FORBIDDEN":{
                    playerData.setBounty(playerData.getBounty() + 25);
                    break;
                }
                case "BLASPHEMY":{
                    playerData.setBounty(playerData.getBounty() + 50);
                }
            }
            mTabooDao.updateTreasury(treasure);
            mTabooDao.updatePlayer(playerData);
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

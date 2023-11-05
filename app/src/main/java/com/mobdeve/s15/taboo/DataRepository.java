package com.mobdeve.s15.taboo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class DataRepository {

    private DAO mTabooDao;
    private LiveData<List<Treasure>> mTreasury;
    private LiveData<PlayerData> mPlayerData;
    private LiveData<User> mUser;

    DataRepository(Application application) {
        TabooDatabase db = TabooDatabase.getDatabase(application);
        mTabooDao = db.TabooDao();
        mPlayerData = mTabooDao.getPlayerData();
        mTreasury = mTabooDao.getTreasury();
        mUser = mTabooDao.getUser();
    }

    LiveData<List<Treasure>> getTreasury() {
        return mTreasury;
    }
    List<Treasure> getCurrentTreasury() {
        return mTabooDao.getCurrentTreasury();
    }

    LiveData<PlayerData> getPlayerData(){
        return mPlayerData;
    }
    PlayerData getCurrentPlayerData() {
        return mTabooDao.getCurrentPlayerData();
    }

    LiveData<User> getUser(){
        return mUser;
    }
    User getCurrentUser() {
        return mTabooDao.getCurrentUser();
    }

    void updatePlayer(PlayerData playerData) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            playerData.setId(); //Set id to 0
            mTabooDao.updatePlayer(playerData);
        });
    }
    void updateTreasury(Treasure treasure, PlayerData playerData) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            Treasure temp = treasure;
            //Add random treasure to treasury but check if its already there, add 1 to count if so.
            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<List<Treasure>> futureTreasure = threadpool.submit(() -> mTabooDao.getCurrentTreasury());
            while (!futureTreasure.isDone()) {
                Log.v("DATA_REPOSITORY", "Retrieving data...");
            }
            try {
                List<Treasure> currentTreasure = futureTreasure.get();

                if(currentTreasure.size() > 0){
                    for(int i = 0; i < currentTreasure.size(); i++){
                        if(currentTreasure.get(i).getId().equals(temp.getId())){
                            temp = currentTreasure.get(i);
                            temp.setCount(temp.getCount() + 1); //Increment amount
                            mTabooDao.updateTreasury(temp);
                            break;
                        }
                        else if(i == currentTreasure.size()-1){
                            mTabooDao.updateTreasury(temp); //Add new treasure at end of loop
                        }
                    }
                }else mTabooDao.updateTreasury(temp); //Just add if treasury is empty
            }catch (Exception e){
                Log.v("DATA_REPOSITORY", e.toString());
            }

            //TODO: Add check for bonuses

            threadpool.shutdown();

            //Calculate new bounty
            playerData.setId(); //Set id to 0
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
            mTabooDao.updatePlayer(playerData);
        });
    }

    void updateUser(User user) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            mTabooDao.deleteUser();
            mTabooDao.updateUser(user);
        });
    }

    void sellTreasure(Treasure treasure, PlayerData playerData) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            //Calculate new bounty
            playerData.setId(); //Set id to 0
            switch (treasure.getRarity()){
                case "COMMON":{
                    playerData.setBounty(playerData.getBounty() - 15);
                    break;
                }
                case "RARE":{
                    playerData.setBounty(playerData.getBounty() - 30);
                    break;
                }
                case "FORBIDDEN":{
                    playerData.setBounty(playerData.getBounty() - 75);
                    break;
                }
                case "BLASPHEMY":{
                    playerData.setBounty(playerData.getBounty() - 150);
                }
            }
            treasure.setCount(treasure.getCount() - 3);
            mTabooDao.updateTreasury(treasure);

            //TODO: Add check for bonuses
        });
    }

    void deleteUser() {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            mTabooDao.deleteUser();
            mTabooDao.updateUser(new User("", ""));
        });
    }

    void deleteData() {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            mTabooDao.deletePlayer();
            mTabooDao.deleteTreasures();
            mTabooDao.deleteUser();
            mTabooDao.updatePlayer(new PlayerData(0, "", 1, 0, 0, 0, 0, 0));
            mTabooDao.updateUser(new User("", ""));
        });
    }
}

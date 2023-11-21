package com.mobdeve.s15.taboo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class DataRepository {

    private DAO mTabooDao;
    private LiveData<List<Treasure>> mTreasury;
    private LiveData<PlayerData> mPlayerData;
    private LiveData<User> mUser;
    private boolean isSelling = false;

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
            PlayerData tempP = playerData;
            //Add random treasure to treasury but check if its already there, add 1 to count if so.
            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<List<Treasure>> futureTreasure = threadpool.submit(() -> mTabooDao.getCurrentTreasury());
            while (!futureTreasure.isDone()) {
                Log.v("DATA_REPOSITORY", "Retrieving data...");
            }
            try {
                List<Treasure> currentTreasure = futureTreasure.get();
                ArrayList<Treasure> cacheT = new ArrayList<>(currentTreasure);

                if(currentTreasure.size() > 0){
                    for(int i = 0; i < currentTreasure.size(); i++){
                        if(currentTreasure.get(i).getId().equals(temp.getId())){
                            temp = currentTreasure.get(i);
                            temp.setCount(temp.getCount() + treasure.getCount()); //Increment or decrease amount
                            mTabooDao.updateTreasury(temp);
                            cacheT.set(i, temp);//Update Cached treasury
                            break;
                        }
                        else if(i == currentTreasure.size()-1){
                            mTabooDao.updateTreasury(temp); //Add new treasure at end of loop
                            cacheT.add(temp);//Update Cached treasury
                        }
                    }

                    //Generate random item if selling, no need to check for size since cant sell with size = 0
                    if(treasure.getCount() == -3){//-3 is for selling
                        isSelling = true; //Activate sell mode for certain bonuses
                        futureTreasure = threadpool.submit(() -> mTabooDao.getCurrentTreasury());
                        while (!futureTreasure.isDone()) {
                            Log.v("DATA_REPOSITORY", "Retrieving data...");
                        }
                        for(int i = 0; i < currentTreasure.size(); i++){
                            if(currentTreasure.get(i).getId().equals(TreasureList.lastRandom.getId())){
                                temp = currentTreasure.get(i);
                                temp.setCount(temp.getCount() + TreasureList.lastRandom.getCount()); //Increment or decrease amount
                                mTabooDao.updateTreasury(temp);
                                cacheT.set(i, temp);//Update Cached treasury
                                break;
                            }
                            else if(i == currentTreasure.size()-1){
                                mTabooDao.updateTreasury(TreasureList.lastRandom); //Add new treasure at end of loop
                                cacheT.add(TreasureList.lastRandom);//Update Cached treasury
                            }
                        }

                        //Calculate new bounty for added random
                        tempP = calcBounty(tempP, TreasureList.lastRandom.getRarity(), TreasureList.lastRandom.getCount());
                    }

                    //Check bonuses after loops and selling
                    tempP = checkBonuses(cacheT, tempP);

                }else mTabooDao.updateTreasury(temp); //Just add if treasury is empty
            }catch (Exception e){
                Log.v("DATA_REPOSITORY", e.toString());
            }

            threadpool.shutdown();

            //Calculate new bounty added/sold treasure
            tempP = calcBounty(tempP, treasure.getRarity(), treasure.getCount());
            mTabooDao.updatePlayer(tempP);
        });
    }

    PlayerData calcBounty(PlayerData tempP, String rarity, int count){
        //Calculate new bounty
        tempP.setId(); //Set id to 0
        switch (rarity){
            case "COMMON":{
                tempP.setBounty(tempP.getBounty() + ((5 * count)));
                break;
            }
            case "RARE":{
                tempP.setBounty(tempP.getBounty() + (10 * count));
                break;
            }
            case "FORBIDDEN":{
                tempP.setBounty(tempP.getBounty() + (25 * count));
                break;
            }
            case "BLASPHEMY":{
                tempP.setBounty(tempP.getBounty() + (50 * count));
                break;
            }
            case "LOST":{
                tempP.setBounty(tempP.getBounty() + (100 * count));
                break;
            }
        }
        return tempP;
    }

    void updateUser(User user) {
        TabooDatabase.databaseWriteExecutor.execute(() -> {
            mTabooDao.deleteUser();
            mTabooDao.updateUser(user);
        });
    }

    //Use to check for set bonuses upon treasure acquisition and selling
    PlayerData checkBonuses(ArrayList<Treasure> treasures, PlayerData player){
        //Vars for checking complete set
        int set1TreasureCount = 0; //Needs 4

        //Check inventory for set treasures
        for(int i = 0; i < treasures.size(); i++){
            //SET 1
            if(treasures.get(i).getName().equals(TreasureList.names[0]) && treasures.get(i).getCount() > 0)
                set1TreasureCount++;
            if(treasures.get(i).getName().equals(TreasureList.names[1]) && treasures.get(i).getCount() > 0)
                set1TreasureCount++;
            if(treasures.get(i).getName().equals(TreasureList.names[2]) && treasures.get(i).getCount() > 0)
                set1TreasureCount++;
            if(treasures.get(i).getName().equals(TreasureList.names[3]) && treasures.get(i).getCount() > 0)
                set1TreasureCount++;
        }

        //Smoking Giant Set bonus checks
        if(player.getSetBonus1() == 0 && set1TreasureCount == 4){ //On obtaining complete set
            player.setSetBonus1(1);
            player.setHealth(player.getHealth()+1); //Extra health
        }
        else if(player.getSetBonus1() == 1 && set1TreasureCount < 4){ //On losing complete set
            player.setSetBonus1(0);
            player.setHealth(player.getHealth()-1); //Extra health
        }

        isSelling = false; //Deactivate sell mode
        return player;
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
            mTabooDao.updatePlayer(new PlayerData(0, "", 3, 0, 0, 0, 0, 0, 0));
            mTabooDao.updateUser(new User("", ""));
        });
    }
}

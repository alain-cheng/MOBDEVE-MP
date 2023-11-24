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
                    tempP = checkBonuses(cacheT, tempP, treasure.getRarity());

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
    PlayerData checkBonuses(ArrayList<Treasure> treasures, PlayerData player, String rarity){
        //NOTE: To add new set bonuses, just copy until you hit j++ in the loop and b++ outside

        //Vars for checking complete set
        int[] setTreasureCount = new int[TreasureList.EMPTY_SET_BONUS.length()]; //Checks if setBonus complete

        //Check inventory for set treasures
        for(int i = 0; i < treasures.size(); i++){
            int j = 0; //Index of set Complete

            //SET 1: Kapre
            if(treasures.get(i).getName().equals(TreasureList.names[0]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[1]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[2]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[3]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 2: Conquest
            if(treasures.get(i).getName().equals(TreasureList.names[4]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[5]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[6]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[7]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 3: Legendary Arms
            if(treasures.get(i).getName().equals(TreasureList.names[8]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[9]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[10]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[11]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 4: Not a Reference
            if(treasures.get(i).getName().equals(TreasureList.names[12]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[13]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[14]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[15]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 5: Blood Moon
            if(treasures.get(i).getName().equals(TreasureList.names[16]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[17]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[18]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[19]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 6: Mythical Four
            if(treasures.get(i).getName().equals(TreasureList.names[20]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[21]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[22]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[23]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 7: It's a Revolution
            if(treasures.get(i).getName().equals(TreasureList.names[24]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[25]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[26]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[27]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 8: Nation Pride
            if(treasures.get(i).getName().equals(TreasureList.names[28]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[29]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[30]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[31]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET 9: Fruities
            if(treasures.get(i).getName().equals(TreasureList.names[32]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[33]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[34]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[35]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set

            //SET Final: The Creators
            if(treasures.get(i).getName().equals(TreasureList.names[TreasureList.fullTreasury.length - 3]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[TreasureList.fullTreasury.length - 2]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++;
            if(treasures.get(i).getName().equals(TreasureList.names[TreasureList.fullTreasury.length - 1]) && treasures.get(i).getCount() > 0)
                setTreasureCount[j]++; j++; //Increment to next set
        }

        int b = 0; //Index for setBonus chars

        //Smoking Giant Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setHealth(player.getHealth()+1); //Extra health
        } b++; //Increment index

        /*
        else if(player.getSetBonus().charAt(b) == '1' && setTreasureCount[b] < 4){ //On losing complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '0';
            player.setSetBonus(String.valueOf(temp));
            player.setHealth(player.getHealth()-1); //Extra health
        } b++; //Increment index

         The above is unused code for deactivating bonuses
         */

        //Conquest Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp)); //Signals Godot to reduce amount of floors
        } b++; //Increment index

        //Legendary Arms Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setBountyBonus(player.getBountyBonus() + 100); //Get 100 extra bounty
        } b++; //Increment index

        //Not a Reference Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setHealth(player.getHealth()+1); //Extra health
        } b++; //Increment index

        //Blood moon bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setTabooBonus(player.getTabooBonus() + 2); //Faster taboo gauge
        } b++; //Increment index

        //Mythical Four Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setBountyBonus(player.getBountyBonus() + 50); //Get 50 extra bounty
        } b++; //Increment index

        //Revolution Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setBountyBonus(player.getBountyBonus() + 50); //Get 50 extra bounty
        } b++; //Increment index

        //Nation Pride Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setHealth(player.getHealth()+1); //Extra health
        } b++; //Increment index

        //Fruities Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 4){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));
            player.setBountyBonus(player.getBountyBonus() + 50); //Get 50 extra bounty
        } b++; //Increment index

        //Creator Set bonus checks
        if(player.getSetBonus().charAt(b) == '0' && setTreasureCount[b] >= 3){ //On obtaining complete set
            char[] temp = player.getSetBonus().toCharArray();
            temp[b] = '1';
            player.setSetBonus(String.valueOf(temp));

            int correction = 0;
            if(isSelling){
                switch (rarity){
                    case "COMMON":{
                        correction = -15;
                        break;
                    }
                    case "RARE":{
                        correction = -30;
                        break;
                    }
                    case "FORBIDDEN":{
                        correction = -75;
                        break;
                    }
                    case "BLASPHEMY":{
                        correction = -150;
                        break;
                    }
                    case "LOST":{
                        correction = -300;
                        break;
                    }
                }
            }

            int altTimelineBounty = player.getBounty() + correction;
            //Get Double Total Bounty
            player.setBountyBonus((player.getBountyBonus()*2 + altTimelineBounty)*2 - altTimelineBounty);
        } b++; //Increment index

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
            mTabooDao.updatePlayer(new PlayerData(0, 3, 0, 0, 0, 0, 0, TreasureList.EMPTY_SET_BONUS));
            mTabooDao.updateUser(new User("", ""));
        });
    }
}

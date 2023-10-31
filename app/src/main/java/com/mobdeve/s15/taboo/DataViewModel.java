package com.mobdeve.s15.taboo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DataViewModel extends AndroidViewModel {
    private DataRepository mDataRepository;
    private final LiveData<List<Treasure>> mTreasury;
    private LiveData<PlayerData> mPlayer;
    private LiveData<User> mUser;
    public DataViewModel(Application application) {
        super(application);
        mDataRepository = new DataRepository(application);
        mTreasury = mDataRepository.getTreasury();
        mPlayer = mDataRepository.getPlayerData();
        mUser = mDataRepository.getUser();
    }

    LiveData<List<Treasure>> getTreasury() {
        return mTreasury;
    }
    List<Treasure> getCurrentTreasury() {
        return mDataRepository.getCurrentTreasury();
    }

    LiveData<PlayerData> getPlayer() {
        return mPlayer;
    }
    PlayerData getCurrentPlayerData() {
        return mDataRepository.getCurrentPlayerData();
    }

    LiveData<User> getUser() {
        return mUser;
    }
    User getCurrentUser() {
        return mDataRepository.getCurrentUser();
    }

    public void updatePlayer(PlayerData playerData){
        mDataRepository.updatePlayer(playerData);
    }

    public void updateTreasury(Treasure treasure, PlayerData playerData){
        mDataRepository.updateTreasury(treasure, playerData);
    }

    public void sellTreasure(Treasure treasure, PlayerData playerData){
        mDataRepository.sellTreasure(treasure, playerData);
    }

    public void login(User user) {
        mDataRepository.updateUser(user);
    }

    public void logout() {
        mDataRepository.deleteUser();
    }

    public void deleteData(){
        mDataRepository.deleteData();
    }
}
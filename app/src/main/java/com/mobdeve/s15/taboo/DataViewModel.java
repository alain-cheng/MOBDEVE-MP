package com.mobdeve.s15.taboo;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import java.util.List;

public class DataViewModel extends AndroidViewModel {
    //TODO: Finish the viewModel features for the treasures and connect to database
    private DataRepository mDataRepository;
    private final LiveData<List<Treasure>> mTreasury;
    private LiveData<PlayerData> mPlayer;
    public DataViewModel(Application application) {
        super(application);
        mDataRepository = new DataRepository(application);
        mTreasury = mDataRepository.getTreasury();
        mPlayer = mDataRepository.getPlayerData();
    }

    LiveData<List<Treasure>> getTreasury() {
        return mTreasury;
    }

    LiveData<PlayerData> getPlayer() {
        return mPlayer;
    }

    public void updatePlayer(PlayerData playerData){
        mDataRepository.updatePlayer(playerData);
    }

    public void updateTreasury(Treasure treasure, PlayerData playerData){
        mDataRepository.updateTreasury(treasure, playerData);
    }

    public void deleteData(){
        mDataRepository.deleteData();
    }
}
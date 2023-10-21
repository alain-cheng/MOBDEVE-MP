package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.app.Application;
import android.os.Bundle;

import java.util.List;

public class TreasureViewModel extends AndroidViewModel {
    //TODO: Finish the viewModel for the treasures and connect to database
    private DataRepository mDataRepository;
    private final LiveData<List<Treasure>> mTreasury;
    public TreasureViewModel(Application application) {
        super(application);
        mDataRepository = new DataRepository(application);
        mTreasury = mDataRepository.getTreasury();
    }

    LiveData<List<Treasure>> getTreasury() {
        return mTreasury;
    }

    public void updateTreasury(Treasure treasure){
        mDataRepository.updateTreasury(treasure);
    }
}
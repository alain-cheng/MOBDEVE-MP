package com.mobdeve.s15.taboo;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; //viewBinding variable
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataRepository mDataRepository;
    private LiveData<PlayerData> mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init(getApplication());
    }

    private void setBounty(int i){
        binding.activityMainTxtBounty.setText(String.valueOf(i));
    }

    private void init(Application application) {
        //Listeners
        binding.activityMainBtnSettings.setOnClickListener(this::settingsListener);
        binding.activityMainBtnPlay.setOnClickListener(this::playListener);
        binding.activityMainBtnTreasure.setOnClickListener(this::treasureListener);

        //Get PlayerData
        mDataRepository = new DataRepository(application);
        mPlayer = mDataRepository.getPlayerData();

        setBounty(1);
    }

    private void settingsListener(View v){
        v.startAnimation(buttonClick);
        //Add transition to settings activity
    }
    private void playListener(View v){
        v.startAnimation(buttonClick);
        //Add transition to game activity
    }
    private void treasureListener(View v){
        v.startAnimation(buttonClick);
        //Add transition to view treasures activity
    }
}
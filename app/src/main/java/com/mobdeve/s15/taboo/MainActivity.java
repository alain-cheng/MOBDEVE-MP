package com.mobdeve.s15.taboo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; //viewBinding variable
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        mDataViewModel.getPlayer().observe(this, playerData -> {
            try {
                setBounty(playerData.getBounty());
            }catch (Exception e){
                Log.v("ACTIVITY_ERR", e.toString());
                setBounty(0);
            }
        });

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.activityMainBtnSettings.setOnClickListener(this::settingsListener);
        binding.activityMainBtnPlay.setOnClickListener(this::playListener);
        binding.activityMainBtnTreasure.setOnClickListener(this::treasureListener);
    }

    private void setBounty(int i){
        binding.activityMainTxtBounty.setText(String.valueOf(i));
    }

    private void settingsListener(View v){
        v.startAnimation(buttonClick);
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }
    private void playListener(View v){
        v.startAnimation(buttonClick);
        //Add transition to game activity
    }
    private void treasureListener(View v){
        v.startAnimation(buttonClick);
        Intent intent = new Intent(this, Collection.class);
        startActivity(intent);
    }
}
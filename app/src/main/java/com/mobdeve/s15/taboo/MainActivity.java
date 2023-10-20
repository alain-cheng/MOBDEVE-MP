package com.mobdeve.s15.taboo;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; //viewBinding variable
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();

        //Testing
        Random rand = new Random(System.nanoTime());
        setCounter(rand.nextInt(101));
    }

    private void setCounter(int i){
        binding.activityMainTxtCounter.setText(String.valueOf(i));
    }

    private void init() {
        //Listeners
        binding.activityMainBtnSettings.setOnClickListener(this::settingsListener);
        binding.activityMainBtnPlay.setOnClickListener(this::playListener);
        binding.activityMainBtnTreasure.setOnClickListener(this::treasureListener);
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
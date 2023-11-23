package com.mobdeve.s15.taboo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityTutorialBinding;

// Tutorial (How to play) Activity
public class Tutorial extends AppCompatActivity {
    ActivityTutorialBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
    private DataViewModel mDataViewModel;
    private MediaPlayer backSfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        backSfx = MediaPlayer.create(this.getBaseContext(), R.raw.button_press_4);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        binding.tutorialBackIbtn.setOnClickListener(this::backListener);
    }

    private void backListener(View v) {
        v.startAnimation(buttonClick);
        backSfx.start();
        finish();
    }
}

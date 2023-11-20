package com.mobdeve.s15.taboo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        binding.tutorialBackIbtn.setOnClickListener(this::backListener);
    }

    private void backListener(View v) {
        v.startAnimation(buttonClick);
        finish();
    }
}

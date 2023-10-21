package com.mobdeve.s15.taboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;
import com.mobdeve.s15.taboo.databinding.ActivitySettingBinding;

public class Setting extends AppCompatActivity {
    ActivitySettingBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.settingsBackIbtn.setOnClickListener(this::backListener);
        binding.erasebutton.setOnClickListener(this::eraseListener);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }
    private void eraseListener(View v){
        v.startAnimation(buttonClick);
        mDataViewModel.deleteData();
        finish();
    }
}
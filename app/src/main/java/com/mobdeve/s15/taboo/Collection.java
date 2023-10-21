package com.mobdeve.s15.taboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.mobdeve.s15.taboo.databinding.ActivityCollectionBinding;
import com.mobdeve.s15.taboo.databinding.ActivitySettingBinding;

public class Collection extends AppCompatActivity {
    ActivityCollectionBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.collectionBackIbtn.setOnClickListener(this::backListener);
        binding.item1.setOnClickListener(this::itemListener);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }

    private void itemListener(View v) {
        v.startAnimation(buttonClick);
        //Intent intent = new Intent(TreasureView.class);
        //startActivity(intent);
    }
}
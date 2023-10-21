package com.mobdeve.s15.taboo;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityCollectionBinding;
import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;
import com.mobdeve.s15.taboo.databinding.ActivityTreasureViewBinding;
import com.mobdeve.s15.taboo.databinding.ActivityTreasureViewModelBinding;

public class TreasureView extends AppCompatActivity {
    ActivityTreasureViewBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTreasureViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        binding.backBtn.setOnClickListener(this::backListener);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }
}

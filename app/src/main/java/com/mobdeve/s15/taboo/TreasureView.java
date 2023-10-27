package com.mobdeve.s15.taboo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityTreasureViewBinding;

import java.util.Objects;

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

        Intent intent = getIntent();
        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
        initData(intent);
    }

    private void initListeners() {
        binding.sellBtn.setOnClickListener(this::sellListener);
        binding.backBtn.setOnClickListener(this::backListener);
    }
    private void initData(Intent intent) {
        binding.treasureIv.setImageResource(intent.getIntExtra("ITEM_IMG", 0));
        binding.treasureNameTv.setText(intent.getStringExtra("ITEM_NAME"));
        binding.treasureDescriptionTv.setText(intent.getStringExtra("ITEM_DESC"));
        if(intent.getIntExtra("ITEM_CNT", 0) >= 3){
            binding.sellBtn.setVisibility(View.VISIBLE);
        }

        String color = "";
        switch (Objects.requireNonNull(intent.getStringExtra("ITEM_RAR"))){
            case "COMMON":{
                color = "#A0A0A0";
                break;
            }
            case "RARE":{
                color = "#0094FF";
                break;
            }
            case "FORBIDDEN":{
                color = "#FF006E";
                break;
            }
            case "BLASPHEMY":{
                color = "#B200FF";
            }
        }
        binding.treasureNameTv.setTextColor(Color.parseColor(color));
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }
    private void sellListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }
}

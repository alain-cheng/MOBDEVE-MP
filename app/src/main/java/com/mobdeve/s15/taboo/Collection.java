package com.mobdeve.s15.taboo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s15.taboo.databinding.ActivityCollectionBinding;

// Collection Activity
public class Collection extends AppCompatActivity {
    ActivityCollectionBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    private MediaPlayer backSfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        backSfx = MediaPlayer.create(this.getBaseContext(), R.raw.button_press_2);
        RecyclerView recyclerView = findViewById(R.id.item_recyclerView);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        mDataViewModel.getTreasury().observe(this, treasures -> {
            try {
                TreasureRVAdapter adapter = new TreasureRVAdapter(this, treasures);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            }catch (Exception e){
                Log.v("ACTIVITY_ERR", e.toString());
            }
        });

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.collectionBackIbtn.setOnClickListener(this::backListener);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        backSfx.start();
        finish();
    }
}
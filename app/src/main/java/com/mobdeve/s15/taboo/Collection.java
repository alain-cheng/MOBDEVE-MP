package com.mobdeve.s15.taboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.mobdeve.s15.taboo.databinding.ActivityCollectionBinding;
import com.mobdeve.s15.taboo.databinding.ActivitySettingBinding;

import java.util.ArrayList;

// Collection Activity
public class Collection extends AppCompatActivity {
    ActivityCollectionBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;

    // placeholder: for Prototype App only
    ArrayList<TreasureModelTemp> treasures = new ArrayList<>();
    int[] itemThumbnails = {R.drawable.item_kaprecigar,
            R.drawable.item_kaprecigar, R.drawable.item_kaprecigar,
            R.drawable.item_kaprecigar, R.drawable.item_kaprecigar};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        RecyclerView recyclerView = findViewById(R.id.item_recyclerView);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        setupTreasureModels();

        TreasureRVAdapter adapter = new TreasureRVAdapter(this, treasures);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initListeners();
    }

    private void setupTreasureModels() {
        String[] itemNames = getResources().getStringArray(R.array.treasureNames);
        String[] itemBonuses = getResources().getStringArray(R.array.treasureBonuses);

        for(int i = 0; i < itemNames.length; i++) {
            treasures.add(new TreasureModelTemp(
                    itemThumbnails[i],
                    itemNames[i],
                    itemBonuses[i]
            ));
        }
    }

    private void initListeners() {
        //Listeners
        binding.collectionBackIbtn.setOnClickListener(this::backListener);
        //binding.itemRecyclerView.setOnClickListener(this::itemListener);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }

    private void itemListener(View v) {
        v.startAnimation(buttonClick);
        Intent intent = new Intent(this, TreasureView.class);
        startActivity(intent);
    }
}
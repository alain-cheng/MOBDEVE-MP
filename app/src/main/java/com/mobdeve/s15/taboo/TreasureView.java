package com.mobdeve.s15.taboo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityTreasureViewBinding;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TreasureView extends AppCompatActivity implements ConfirmationListener {
    ActivityTreasureViewBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    private int img, cnt;
    private String name, desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTreasureViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        img = intent.getIntExtra("ITEM_IMG", 0);
        cnt = intent.getIntExtra("ITEM_CNT", 0);
        name = intent.getStringExtra("ITEM_NAME");
        desc = intent.getStringExtra("ITEM_DESC");

        initListeners();
        initData(intent);
    }

    private void initListeners() {
        binding.sellBtn.setOnClickListener(this::sellListener);
        binding.backBtn.setOnClickListener(this::backListener);
    }
    private void initData(Intent intent) {
        binding.treasureIv.setImageResource(img);
        binding.treasureNameTv.setText(name);
        binding.treasureDescriptionTv.setText(desc);
        if(cnt >= 3){
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
        DialogFragment dialog = new ConfirmationDialog();
        dialog.show(getSupportFragmentManager(), name);
    }

    @Override
    public void onYes(DialogFragment dialog, String tag) {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        Future<List<Treasure>> futureTreasure = threadpool.submit(() -> mDataViewModel.getCurrentTreasury());
        Future<PlayerData> futureData = threadpool.submit(() -> mDataViewModel.getCurrentPlayerData());

        while (!futureTreasure.isDone() && !futureData.isDone()) {
            Log.v("TREASURE_VIEW", "Retrieving data...");
        }

        try {
            List<Treasure> currentTreasure = futureTreasure.get();
            PlayerData playerData = futureData.get();

            for(int i = 0; i < currentTreasure.size(); i++){
                if(tag.equals(currentTreasure.get(i).getName())){
                    mDataViewModel.sellTreasure(currentTreasure.get(i), playerData);
                    //Todo: Implement proper random treasure generation
                    Treasure random = new Treasure("item420", "TEST", R.drawable.itembox_cheese,
                            "Nah", "Created for Testing", "BLASPHEMY", 1);
                    mDataViewModel.updateTreasury(random, playerData);
                    break;
                }
            }
        }catch (Exception e){
            Log.v("TREASURE_VIEW", e.toString());
            Toast t = Toast.makeText(this, "Could not sell item", Toast.LENGTH_SHORT);
            t.show();
        }

        threadpool.shutdown();
        finish();
    }

    @Override
    public void onNo(DialogFragment dialog, String tag) {

    }
}

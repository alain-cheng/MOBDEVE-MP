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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TreasureView extends AppCompatActivity implements ConfirmationListener {
    ActivityTreasureViewBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    private int img, cnt;
    private String name, desc, rar;
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
        rar = intent.getStringExtra("ITEM_RAR");

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
        if(cnt == -1)
            binding.receivedNotifTxt.setVisibility(View.VISIBLE);

        String color = "";
        switch (rar){
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
                    
                    Random rand = new Random(System.nanoTime());
                    String rarity = currentTreasure.get(i).getRarity();
                    //Get indexes of treasures with rarities[i] == rarity
                    ArrayList<Integer> indexes = new ArrayList<>();
                    for(int j = 0; j < TreasureList.rarities.length; j++){
                        if(TreasureList.rarities[j].equals(rarity))
                            indexes.add(j);
                    }
                    //Select between the indexes at random
                    int index = rand.nextInt(indexes.size());
                    //Generate Treasure and add
                    Treasure random = new Treasure(
                            TreasureList.ids[indexes.get(index)],
                            TreasureList.names[indexes.get(index)],
                            TreasureList.images[indexes.get(index)],
                            TreasureList.bonuses[indexes.get(index)],
                            TreasureList.lores[indexes.get(index)],
                            TreasureList.rarities[indexes.get(index)],
                            1
                    );

                    mDataViewModel.updateTreasury(random, playerData);

                    //Display in TreasureView
                    Intent intent = new Intent(this, TreasureView.class);
                    intent.putExtra("ITEM_IMG", random.getImageid());
                    intent.putExtra("ITEM_NAME", random.getName());
                    intent.putExtra("ITEM_DESC", random.getLore());
                    intent.putExtra("ITEM_RAR", random.getRarity());
                    intent.putExtra("ITEM_CNT", -1);
                    startActivity(intent);
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

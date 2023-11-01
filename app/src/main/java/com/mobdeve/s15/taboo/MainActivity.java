package com.mobdeve.s15.taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; //viewBinding variable
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        context = getApplicationContext();
        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        mDataViewModel.getPlayer().observe(this, playerData -> {
            try {
                setBounty(playerData.getBounty());
                switch (playerData.getTaboo()){
                    case 0:{
                        binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge);
                        break;
                    }
                    case 1:{
                        binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_1_4);
                        break;
                    }
                    case 2:{
                        binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_2_4);
                        break;
                    }
                    case 3:{
                        binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_3_4);
                        break;
                    }
                    case 4:{
                        binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_4_4);
                        break;
                    }
                }
            }catch (Exception e){
                Log.v("MAIN_ACTIVITY", e.toString());
            }
        });

        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get data from signal_data.json
        File file = new File(context.getFilesDir(),"signal_data.json");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String response = stringBuilder.toString();
            JSONObject signal  = new JSONObject(response);

            //If generateTreasure = true, send to GenerateTreasure Activity
            boolean generateTreasure = signal.getBoolean("generateTreasure");
            if(generateTreasure){
                Log.v("MAIN_ACTIVITY", "Generating treasure...");

                //Set signal to false
                JSONObject signalBack = new JSONObject();
                signalBack.put("generateTreasure", false);
                String userString = signalBack.toString();
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(userString);
                bufferedWriter.close();

                //Get CurrentPlayerData and Current Treasury
                ExecutorService threadpool = Executors.newCachedThreadPool();
                Future<List<Treasure>> futureTreasure = threadpool.submit(() -> mDataViewModel.getCurrentTreasury());
                Future<PlayerData> futureData = threadpool.submit(() -> mDataViewModel.getCurrentPlayerData());

                while (!futureTreasure.isDone() && !futureData.isDone()) {
                    Log.v("MAIN_ACTIVITY", "Retrieving data...");
                }

                List<Treasure> currentTreasure = futureTreasure.get();
                PlayerData playerData = futureData.get();

                if(futureData.isDone() && futureTreasure.isDone()){ //Wait until finish
                    //Check if currentTreasure.size() != TreasureList.names.length() and taboo >= 4
                    //Generate a unique treasure starting from item1, otherwise generate random based on rarity
                    if(currentTreasure.size() != TreasureList.names.length && playerData.getTaboo() >= 4){
                        Log.v("MAIN_ACTIVITY", "Generating unique treasure...");
                        //Loop through all names
                        boolean unique;
                        for(int i = 0; i < TreasureList.names.length; i++){
                            unique = true;
                            //Loop through treasury
                            for(int j = 0; j < currentTreasure.size(); j++){
                                if(TreasureList.names[i].equals(currentTreasure.get(j).getName())){
                                    unique = false;
                                    break;
                                }
                            }
                            if(unique){
                                //Add treasure and stop loop
                                Treasure treasure = new Treasure(
                                        TreasureList.ids[i],
                                        TreasureList.names[i],
                                        TreasureList.images[i],
                                        TreasureList.bonuses[i],
                                        TreasureList.lores[i],
                                        TreasureList.rarities[i],
                                        1
                                );
                                mDataViewModel.updateTreasury(treasure, playerData);
                                break;
                            }
                        }

                        //Reset taboo to 0
                        playerData.setTaboo(0);
                        mDataViewModel.updatePlayer(playerData);
                    }else{
                        //Generate rarity of treasure, taboo of 4 increases totalLuck by 10
                        int totalLuck = playerData.getLuck();
                        if(playerData.getTaboo() >= 4)
                            totalLuck += 10;
                        Random rand = new Random(System.nanoTime());
                        int rng = rand.nextInt(101) + totalLuck;

                        //Select rarity based on luck
                        String rarity = "COMMON";
                        if(rng <= 40)
                            rarity = TreasureList.RARITY[0];
                        else if(rng <= 70)
                            rarity = TreasureList.RARITY[1];
                        else if(rng <= 90)
                            rarity = TreasureList.RARITY[2];
                        else if(rng > 90)
                            rarity = TreasureList.RARITY[3];

                        //Get indexes of treasures with rarities[i] == rarity
                        ArrayList<Integer> indexes = new ArrayList<>();
                        for(int i = 0; i < TreasureList.rarities.length; i++){
                            if(TreasureList.rarities[i].equals(rarity))
                                indexes.add(i);
                        }
                        //Select between the indexes at random
                        int index = rand.nextInt(indexes.size());
                        //Generate Treasure and add
                        Treasure treasure = new Treasure(
                                TreasureList.ids[indexes.get(index)],
                                TreasureList.names[indexes.get(index)],
                                TreasureList.images[indexes.get(index)],
                                TreasureList.bonuses[indexes.get(index)],
                                TreasureList.lores[indexes.get(index)],
                                TreasureList.rarities[indexes.get(index)],
                                1
                        );
                        mDataViewModel.updateTreasury(treasure, playerData);

                        //Update taboo
                        if(playerData.getTaboo() < 4)
                            playerData.setTaboo(playerData.getTaboo() + 1); //Increment while below 4
                        else //Reset to zero
                            playerData.setTaboo(0);
                        mDataViewModel.updatePlayer(playerData);
                    }

                    threadpool.shutdown();
                }
            }
        }catch (Exception e){
            Log.v("MAIN_ACTIVITY", e.toString());
        }
    }

    private void initListeners() {
        //Listeners
        binding.activityMainBtnSettings.setOnClickListener(this::settingsListener);
        binding.activityMainBtnPlay.setOnClickListener(this::playListener);
        binding.activityMainBtnTreasure.setOnClickListener(this::treasureListener);
    }

    private void setBounty(int i){
        binding.activityMainTxtBounty.setText(String.valueOf(i));
    }

    private void settingsListener(View v){
        v.startAnimation(buttonClick);
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }
    private void playListener(View v){
        v.startAnimation(buttonClick);

        //Retrieve currentPlayerData
        ExecutorService threadpool = Executors.newCachedThreadPool();
        Future<PlayerData> futureData = threadpool.submit(() -> mDataViewModel.getCurrentPlayerData());
        while (!futureData.isDone()) {
            Log.v("MAIN_ACTIVITY", "Retrieving data...");
        }

        try {
            if(futureData.isDone()){
                PlayerData playerData = futureData.get();

                //Write JSON Files
                JSONObject player = new JSONObject();
                player.put("health", playerData.getHealth());
                player.put("bounty", playerData.getBounty());
                player.put("taboo", playerData.getTaboo());
                player.put("tabooBonus", playerData.getTabooBonus());
                player.put("luck", playerData.getLuck());
                player.put("bountyBonus", playerData.getBountyBonus());

                JSONObject signal = new JSONObject();
                signal.put("generateTreasure", false);

                String userString = player.toString();
                File file = new File(context.getFilesDir(),"player_data.json");
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(userString);
                bufferedWriter.close();

                userString = signal.toString();
                file = new File(context.getFilesDir(), "signal_data.json");
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(userString);
                bufferedWriter.close();

                threadpool.shutdown();
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){
            Log.v("MAIN_ACTIVITY", e.toString());
        }
    }
    private void treasureListener(View v){
        v.startAnimation(buttonClick);
        Intent intent = new Intent(this, Collection.class);
        startActivity(intent);
    }
}
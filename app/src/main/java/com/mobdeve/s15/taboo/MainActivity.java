package com.mobdeve.s15.taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
            }catch (Exception e){
                Log.v("MAIN_ACTIVITY", e.toString());
                setBounty(0);
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
package com.mobdeve.s15.taboo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity implements ConfirmationListener {
    private ActivityMainBinding binding; //viewBinding variable
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;
    private Context context;
    private MediaPlayer buttonSfx, playSfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Initialize Treasure Data
        TreasureList.initData();

        //Init MongoDB Realm
        RealmHandler.startInit();

        //Dialog before closing app
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogFragment dialog = new ConfirmationDialog();
                dialog.show(getSupportFragmentManager(), "CloseMain");
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        context = getApplicationContext();
        buttonSfx = MediaPlayer.create(context, R.raw.button_press_1);
        playSfx = MediaPlayer.create(context, R.raw.button_press_3);
        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        mDataViewModel.getPlayer().observe(this, playerData -> {
            try {
                setBounty(playerData.getBounty() + playerData.getBountyBonus());
                int CASE = playerData.getTaboo();
                if(CASE < TreasureList.PHASE_1)
                    binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_0_v2);
                else if(CASE < TreasureList.PHASE_2)
                    binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_1_v2);
                else if(CASE < TreasureList.PHASE_3)
                    binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_2_v2);
                else if(CASE < TreasureList.PHASE_4)
                    binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_3_v2);
                else if(CASE >= TreasureList.PHASE_4)
                    binding.activityMainImgGauge.setImageResource(R.drawable.taboo_gauge_4_v2);
            }catch (Exception e){
                Log.v("MAIN_ACTIVITY", e.toString());
            }
        });

        initListeners();
    }

    private void sendToServer(){
        System.out.println("Syncing with Realm...");
        //Upload Player Data if Logged in
        ExecutorService threadpool = Executors.newCachedThreadPool();
        Future<PlayerData> futurePlayer = threadpool.submit(() -> mDataViewModel.getCurrentPlayerData());
        Future<List<Treasure>> futureTreasure = threadpool.submit(() -> mDataViewModel.getCurrentTreasury());
        //Get futureUser using Threadpool, use this to check for login
        Future<User> futureUser = threadpool.submit(() -> mDataViewModel.getCurrentUser());
        while (!futureTreasure.isDone() && !futureUser.isDone() && !futurePlayer.isDone()) {
            Log.v("MAIN_ACTIVITY", "Retrieving data...");
        }
        try {
            List<Treasure> currentTreasure = futureTreasure.get();
            User currentUser = futureUser.get();
            PlayerData currentPlayer = futurePlayer.get();
            if(!currentUser.getUsername().isBlank()){
                RealmHandler.updatePlayerData(currentPlayer, currentTreasure, currentUser.getUsername(), currentUser.getEmail());
            }
        }catch(Exception e){
            Log.v("MAIN_ACTIVITY", e.toString());
        }
    }

    @Override //DEV NOTE: WHY!?!??!?! WHY IS THIS THE ONE THAT WORKS!??!?!?!? IT MAKES NO SENSE!>>!>!>!>!>!>:?ed'wdLQ,
    protected void onStop() {
        super.onStop();
        sendToServer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        animateUI();

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

            boolean generateTreasure = signal.getBoolean("generateTreasure");
            boolean loss = signal.getBoolean("loss");//Get boolean loss

            //Get CurrentPlayerData and Current Treasury
            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<List<Treasure>> futureTreasure = threadpool.submit(() -> mDataViewModel.getCurrentTreasury());
            Future<PlayerData> futureData = threadpool.submit(() -> mDataViewModel.getCurrentPlayerData());

            while (!futureTreasure.isDone() && !futureData.isDone()) {
                Log.v("MAIN_ACTIVITY", "Retrieving data...");
            }

            List<Treasure> currentTreasure = futureTreasure.get();
            PlayerData playerData = futureData.get();

            if(futureData.isDone() && futureTreasure.isDone()){
                //An if statement that subtracts taboo on loss if taboo > 0
                if(loss && playerData.getTaboo() > 0){
                    playerData.setTaboo(playerData.getTaboo() + TreasureList.LOSS_ADD);
                    if(playerData.getTaboo() < 0) //Prevent negative taboo value
                        playerData.setTaboo(0);
                    mDataViewModel.updatePlayer(playerData);

                    //Set signals to false
                    JSONObject signalBack = new JSONObject();
                    signalBack.put("generateTreasure", false);
                    signalBack.put("loss", false);//Add signal for player loss
                    String userString = signalBack.toString();
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(userString);
                    bufferedWriter.close();
                }

                //If generateTreasure = true, send to GenerateTreasure Activity
                else if(generateTreasure){
                    Log.v("MAIN_ACTIVITY", "Generating treasure...");

                    //Set signals to false
                    JSONObject signalBack = new JSONObject();
                    signalBack.put("generateTreasure", false);
                    signalBack.put("loss", false);//Add signal for player loss
                    String userString = signalBack.toString();
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(userString);
                    bufferedWriter.close();

                    //Wait until finish
                    //Check if currentTreasure.size() != TreasureList.fullTreasury.length() and taboo >= PHASE_4
                    //Generate a unique treasure starting from item1, otherwise generate random based on rarity
                    if(currentTreasure.size() != TreasureList.fullTreasury.length && playerData.getTaboo() >= TreasureList.PHASE_4){
                        Log.v("MAIN_ACTIVITY", "Generating unique treasure...");
                        //Loop through all names
                        boolean unique;
                        Treasure treasure = new Treasure("","",0,"","","",0);
                        for(int i = 0; i < TreasureList.fullTreasury.length; i++){
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
                                treasure = new Treasure(
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

                        //Display in TreasureView
                        Intent intent = new Intent(this, TreasureView.class);
                        intent.putExtra("ITEM_IMG", treasure.getImageid());
                        intent.putExtra("ITEM_NAME", treasure.getName());
                        intent.putExtra("ITEM_DESC", treasure.getLore());
                        intent.putExtra("ITEM_RAR", treasure.getRarity());
                        intent.putExtra("ITEM_CNT", -1);
                        intent.putExtra("ITEM_BON", treasure.getItemBonus());
                        startActivity(intent);

                        //Reset taboo to 0
                        playerData.setTaboo(0);
                        mDataViewModel.updatePlayer(playerData);
                    }else{
                        //Generate rarity of treasure, taboo of PHASE_4 increases totalLuck by 10
                        //Generate Treasure and add
                        TreasureList.genRandomTreasure("REWARD", "COMMON", playerData);
                        Treasure treasure = TreasureList.lastRandom;
                        mDataViewModel.updateTreasury(treasure, playerData);

                        //Display in TreasureView
                        Intent intent = new Intent(this, TreasureView.class);
                        intent.putExtra("ITEM_IMG", treasure.getImageid());
                        intent.putExtra("ITEM_NAME", treasure.getName());
                        intent.putExtra("ITEM_DESC", treasure.getLore());
                        intent.putExtra("ITEM_RAR", treasure.getRarity());
                        intent.putExtra("ITEM_CNT", -1);
                        intent.putExtra("ITEM_BON", treasure.getItemBonus());
                        startActivity(intent);

                        //Update taboo
                        if(playerData.getTaboo() < TreasureList.PHASE_4) //ADD constant plus bonus while below PHASE_4
                            playerData.setTaboo(playerData.getTaboo() + TreasureList.WIN_ADD + playerData.getTabooBonus());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendToServer();
        //Shutdown app
        android.os.Process.killProcess(android.os.Process.myPid());
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
        buttonSfx.start();
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }
    private void playListener(View v){
        v.startAnimation(buttonClick);
        playSfx.start();
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
                player.put("setBonus", playerData.getSetBonus());

                JSONObject signal = new JSONObject();
                signal.put("generateTreasure", false);
                signal.put("loss", false);//Add signal for player loss

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
        buttonSfx.start();
        Intent intent = new Intent(this, Collection.class);
        startActivity(intent);
    }

    @Override
    public void onYes(DialogFragment dialog, String tag) {
        finish(); //Close the app if back pressed in main
    }

    @Override
    public void onNo(DialogFragment dialog, String tag) {

    }

    // UI Animations
    private void animateUI() {
        binding.activityMainBtnSettings.setAlpha(0f);
        binding.activityMainBtnSettings.setTranslationY(250);
        binding.activityMainBtnSettings.animate()
                .alpha(1f)
                .translationYBy(-250)
                .setDuration(500);
        binding.activityMainBtnPlay.setAlpha(0f);
        binding.activityMainBtnPlay.setTranslationY(250);
        binding.activityMainBtnPlay.animate()
                .alpha(1f)
                .translationYBy(-250)
                .setDuration(500);
        binding.activityMainBtnTreasure.setAlpha(0f);
        binding.activityMainBtnTreasure.setTranslationY(250);
        binding.activityMainBtnTreasure.animate()
                .alpha(1f)
                .translationYBy(-250)
                .setDuration(500);
        binding.activityMainTxtBounty.setAlpha(0f);
        binding.activityMainTxtBounty.setTranslationY(250);
        binding.activityMainTxtBounty.animate()
                .alpha(1f)
                .translationYBy(-250)
                .setDuration(500);
        binding.activityMainImgTarget.setAlpha(0f);
        binding.activityMainImgTarget.setTranslationY(250);
        binding.activityMainImgTarget.animate()
                .alpha(1f)
                .translationYBy(-250)
                .setDuration(500);
        binding.activityMainImgTitle.setAlpha(0f);
        binding.activityMainImgTitle.setTranslationY(-250);
        binding.activityMainImgTitle.animate()
                .setStartDelay(500)
                .alpha(1f)
                .translationYBy(250)
                .setDuration(500);
        binding.activityMainImgGauge.setAlpha(0f);
        binding.activityMainImgGauge.animate()
                .alpha(1f)
                .setDuration(1000);
    }
}
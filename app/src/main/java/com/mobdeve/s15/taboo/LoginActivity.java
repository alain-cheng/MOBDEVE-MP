package com.mobdeve.s15.taboo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DataViewModel mDataViewModel;
    String mode = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.confirm.setOnClickListener(this::confirmListener);
        binding.mode.setOnClickListener(this::modeListener);
    }

    private void confirmListener(View view){
        //checks for all fields
        boolean checks = true;
        if(binding.username.getText().toString().isBlank())
            checks = false;
        if(binding.email.getText().toString().isBlank())
            checks = false;
        if(binding.password.getText().toString().isBlank())
            checks = false;
        if(!checks){
            Toast t = Toast.makeText(this, "Missing user information!", Toast.LENGTH_SHORT);
            t.show();
        }

        else if(checks) //Only Run if all checks pass
            switch (mode){
                case "Login":{
                    //Check user details on online DB
                    boolean correctDetails = RealmHandler.checkUserInfo(
                            binding.username.getText().toString(),
                            binding.email.getText().toString(),
                            binding.password.getText().toString()
                    );
                    if(correctDetails){
                        //Load data from RealmHandler to replace device data
                        mDataViewModel.loadData(RealmHandler.getUserPlayer(), RealmHandler.getUserTreasury());

                        //Clear data after use
                        RealmHandler.setUserPlayer(null);
                        RealmHandler.setUserTreasury(new ArrayList<>());

                        //Load userdata to user table
                        mDataViewModel.login(new User(binding.username.getText().toString(),
                                binding.email.getText().toString()));
                        finish(); //Return to settings
                    }
                    else{
                        Toast t = Toast.makeText(this, "Incorrect user information or account does not exist!", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    break;
                }
                case "Register":{
                    //Check if username already exists!
                    boolean userExists = true;
                    userExists = RealmHandler.checkExistingUser(binding.username.getText().toString());

                    if(!userExists){
                        //Upload to Online Database
                        ExecutorService threadpool = Executors.newCachedThreadPool();
                        Future<List<Treasure>> futureTreasury = threadpool.submit(() -> mDataViewModel.getCurrentTreasury());
                        Future<PlayerData> futurePlayer = threadpool.submit(() -> mDataViewModel.getCurrentPlayerData());
                        while (!futureTreasury.isDone() && !futurePlayer.isDone()) {
                            Log.v("LOGIN_ACTIVITY", "Retrieving data...");
                        }
                        try {
                            RealmHandler.registerAccount(
                                    futurePlayer.get(),
                                    futureTreasury.get(),
                                    binding.username.getText().toString(),
                                    binding.email.getText().toString(),
                                    binding.password.getText().toString()
                            );
                        }catch (Exception e){
                            Log.v("LOGIN_ACTIVITY", e.toString());
                        }

                        //Load userdata to user table
                        mDataViewModel.login(new User(binding.username.getText().toString(),
                                binding.email.getText().toString()));
                        finish(); //Return to settings
                    }
                    else{
                        Toast t = Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    break;
                }
            }
    }
    private void modeListener(View view){
        switch (mode){
            case "Login":{
                binding.mode.setText(mode);
                mode = "Register";
                binding.loginTitle.setText(mode);
                binding.loginWarning.setVisibility(View.GONE);
                break;
            }
            case "Register":{
                binding.mode.setText(mode);
                mode = "Login";
                binding.loginTitle.setText(mode);
                binding.loginWarning.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}
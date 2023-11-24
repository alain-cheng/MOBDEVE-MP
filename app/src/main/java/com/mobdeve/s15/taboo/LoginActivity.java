package com.mobdeve.s15.taboo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityLoginBinding;

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
        //TODO: Add checks for all fields
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
                    System.out.println("Login!");
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

                        //Set load userdata to user table
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
                break;
            }
            case "Register":{
                binding.mode.setText(mode);
                mode = "Login";
                binding.loginTitle.setText(mode);
                break;
            }
        }
    }
}
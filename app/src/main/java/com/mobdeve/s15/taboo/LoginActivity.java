package com.mobdeve.s15.taboo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityLoginBinding;

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
        //TODO: FIX THIS BUGGY MESS
        boolean checks = true; //Enable if all checks passed

        if(mode.equals("Register"))
            if(binding.username.getText().toString().isBlank())
                checks = false;
        if(binding.email.getText().toString().isBlank())
            checks = false;
        if(binding.password.getText().toString().isBlank())
            checks = false;

        if(checks){
            //TODO: Connect to cloud later. Add password checking and account searching
            ExecutorService threadpool = Executors.newCachedThreadPool();
            if(mode.equals("Register")){
                //TODO: Add logic here for inserting userdata and gamedata into cloud.
                Future<String> futureConfirmation = threadpool.submit(() -> {
                    mDataViewModel.login(new User(binding.username.getText().toString(), binding.email.getText().toString()));
                    return "OK!";
                });
                while (!futureConfirmation.isDone()) {
                    Log.v("LOGIN_ACTIVITY", "Uploading data...");
                }
            }
            Future<User> futureUser = threadpool.submit(() -> {
                return mDataViewModel.getCurrentUser(); //TODO: Replace this with pulling from cloud
            });
            while (!futureUser.isDone()) {
                Log.v("LOGIN_ACTIVITY", "Retrieving data...");
            }

            try {
                if(futureUser.isDone()){
                    User result = futureUser.get();
                    if(result.getUsername().isBlank() && mode.equals("Login")) {
                        throw new Exception("Account could not be found");
                    }
                    mDataViewModel.login(result);
                    Toast t = Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT);
                    t.show();
                    threadpool.shutdown();
                    finish();
                }
            }
            catch (Exception e){
                if(futureUser.isDone()){
                    Log.v("LOGIN_ACTIVITY", e.toString());
                    Toast t = Toast.makeText(this, "Could not login", Toast.LENGTH_SHORT);
                    t.show();
                    threadpool.shutdown();
                    finish();
                }
            }
        }
        else{
            Toast t = Toast.makeText(this, "Error on login info", Toast.LENGTH_SHORT);
            t.show();
        }
    }
    private void modeListener(View view){
        switch (mode){
            case "Login":{
                binding.mode.setText(mode);
                mode = "Register";
                binding.username.setVisibility(View.VISIBLE);
                binding.loginTitle.setText(mode);
                break;
            }
            case "Register":{
                binding.mode.setText(mode);
                mode = "Login";
                binding.username.setVisibility(View.GONE);
                binding.loginTitle.setText(mode);
                break;
            }
        }
    }
}
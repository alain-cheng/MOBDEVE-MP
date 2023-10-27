package com.mobdeve.s15.taboo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mobdeve.s15.taboo.databinding.ActivityLoginBinding;
import com.mobdeve.s15.taboo.databinding.ActivityMainBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String mode = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.confirm.setOnClickListener(this::confirmListener);
        binding.mode.setOnClickListener(this::modeListener);
    }

    private void confirmListener(View view){
        //TODO: Add logic for waiting for login result. Use below for getting login info
        //ExecutorService threadpool = Executors.newCachedThreadPool();
        //Future<String[]> futureTask = threadpool.submit(() -> {
        //    return String[];
        //});
        //while (!futureTask.isDone()) {
        //    System.out.println("FutureTask is not finished yet...");
        //}
        //String[] result = futureTask.get();
        //
        //threadpool.shutdown();

        finish();
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
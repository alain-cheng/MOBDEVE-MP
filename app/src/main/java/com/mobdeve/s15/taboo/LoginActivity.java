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
        switch (mode){
            case "Login":{
                System.out.println("Login!");
                break;
            }
            case "Register":{
                //TODO: Check if username already exists!

                //TODO:
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
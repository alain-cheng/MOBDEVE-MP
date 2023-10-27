package com.mobdeve.s15.taboo;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivitySettingBinding;

public class Setting extends AppCompatActivity implements ConfirmationListener {
    ActivitySettingBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private DataViewModel mDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.settingsBackIbtn.setOnClickListener(this::backListener);
        binding.erasebutton.setOnClickListener(this::eraseListener);
        binding.loginbutton.setOnClickListener(this::loginListener);
        binding.logoutbutton.setOnClickListener(this::logoutListener);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        finish();
    }
    private void loginListener(View v){
        v.startAnimation(buttonClick);
        //TODO: Add a popup that will ask for login details to sign up or login.
        //TODO: Add a function in DataViewModel that will contact server to check or add login info
        //TODO: Connect to db to store login details in a table if data matches the server, logout empties the table.
        //

        //Test Only Delete later
        binding.nameguestView.setImageResource(R.drawable.itemboxname_sample);
        binding.loginbutton.setImageResource(R.drawable.group_718email_sample);
        binding.loginbutton.setClickable(false); //To prevent animation from playing or logging in twice
        binding.logoutbutton.setVisibility(View.VISIBLE);

    }
    private void logoutListener(View v){
        v.startAnimation(buttonClick);
        DialogFragment dialog = new ConfirmationDialog();
        dialog.show(getSupportFragmentManager(), "LogoutDialog");

    }
    private void eraseListener(View v){
        v.startAnimation(buttonClick);
        DialogFragment dialog = new ConfirmationDialog();
        dialog.show(getSupportFragmentManager(), "EraseDialog");
    }

    //Erase button confirmation
    @Override
    public void onYes(DialogFragment dialog, String tag) {
        switch (tag){
            case "EraseDialog":{
                mDataViewModel.deleteData();
                finish();
                break;
            }
            case "LogoutDialog":{
                //TODO: logout should delete data in table account
                binding.nameguestView.setImageResource(R.drawable.itemboxname_guest);
                binding.loginbutton.setImageResource(R.drawable.group_716login_button);
                binding.loginbutton.setClickable(true);
                binding.logoutbutton.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public void onNo(DialogFragment dialog, String tag) {

    }
}


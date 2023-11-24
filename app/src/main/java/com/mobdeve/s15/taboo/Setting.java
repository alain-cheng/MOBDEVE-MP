package com.mobdeve.s15.taboo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
    private MediaPlayer buttonSfx;
    private MediaPlayer backSfx;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        buttonSfx = MediaPlayer.create(this.getBaseContext(), R.raw.button_press_3);
        backSfx = MediaPlayer.create(this.getBaseContext(), R.raw.button_press_4);
        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        mDataViewModel.getUser().observe(this, user -> {
            try {
                if(user.getUsername().equals("") || user.getEmail().equals("")){
                    binding.usernameTxt.setText("Name: Guest");
                    binding.emailTxt.setText("Login");
                    binding.nameguestView.setImageResource(R.drawable.itemboxname_guest);
                    binding.loginbutton.setImageResource(R.drawable.group_716login_button);
                    binding.loginbutton.setClickable(true);
                    binding.logoutbutton.setVisibility(View.GONE);
                }
                else{
                    binding.usernameTxt.setText(String.format("Name: %s", user.getUsername()));
                    binding.emailTxt.setText(user.getEmail());
                    binding.loginbutton.setImageResource(R.drawable.group_718email_sample);
                    binding.loginbutton.setClickable(false); //To prevent animation from playing or logging in twice
                    binding.logoutbutton.setVisibility(View.VISIBLE);
                    loggedIn = true;
                }
            }catch (Exception e){
                Log.v("SETTINGS_ACTIVITY", e.toString());
            }
        });

        initListeners();
    }

    private void initListeners() {
        //Listeners
        binding.settingsBackIbtn.setOnClickListener(this::backListener);
        binding.erasebutton.setOnClickListener(this::eraseListener);
        binding.loginbutton.setOnClickListener(this::loginListener);
        binding.logoutbutton.setOnClickListener(this::logoutListener);
        binding.taboopediabutton.setOnClickListener(this::taboopediaListener);
        binding.tutorialbutton.setOnClickListener(this::tutorialListener);
    }

    private void tutorialListener(View v) {
        v.startAnimation(buttonClick);
        buttonSfx.start();
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    private void taboopediaListener(View v) {
        v.startAnimation(buttonClick);
        buttonSfx.start();
        Intent intent = new Intent(this, Taboopedia.class);
        startActivity(intent);
    }

    private void backListener(View v){
        v.startAnimation(buttonClick);
        backSfx.start();
        finish();
    }
    private void loginListener(View v){
        v.startAnimation(buttonClick);
        buttonSfx.start();
        //Login Activity, Add check for success in onResume maybe?
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void logoutListener(View v){
        v.startAnimation(buttonClick);
        buttonSfx.start();
        DialogFragment dialog = new ConfirmationDialog();
        dialog.show(getSupportFragmentManager(), "LogoutDialog");

    }
    private void eraseListener(View v){
        v.startAnimation(buttonClick);
        buttonSfx.start();
        DialogFragment dialog = new ConfirmationDialog();
        String tag = loggedIn ? "WipeDialog" : "EraseDialog";
        dialog.show(getSupportFragmentManager(), tag);
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
            case "WipeDialog":{
                //TODO: If loggedIn, logout and delete data from server
                if(loggedIn){
                    loggedIn = false;
                    mDataViewModel.logout();

                    RealmHandler.deleteAccount(
                            binding.usernameTxt.getText().toString().replace("Name: ", ""),
                            binding.emailTxt.getText().toString()
                    );
                }
                //Always delete data
                mDataViewModel.deleteData();
                finish();
                break;
            }
            case "LogoutDialog":{
                //LOGOUT
                loggedIn = false;
                mDataViewModel.logout();
                mDataViewModel.deleteData();//DELETE DATA ON LOGOUT
                finish();
                break;
            }
        }
    }

    @Override
    public void onNo(DialogFragment dialog, String tag) {

    }
}


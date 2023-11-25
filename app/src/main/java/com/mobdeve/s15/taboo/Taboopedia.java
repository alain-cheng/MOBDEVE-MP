package com.mobdeve.s15.taboo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobdeve.s15.taboo.databinding.ActivityTaboopediaBinding;

// Taboopedia Activity
public class Taboopedia extends AppCompatActivity {
    ActivityTaboopediaBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
    private DataViewModel mDataViewModel;
    private MediaPlayer backSfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaboopediaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        backSfx = MediaPlayer.create(this.getBaseContext(), R.raw.button_press_2);

        mDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        initListeners();

        binding.taboopediaContentsIv.setImageBitmap(getBitmapFromResources(
                getResources(),
                R.drawable.taboopedia_contents_1
        ));
    }

    private void initListeners() {
        binding.taboopediaBackIbtn.setOnClickListener(this::backListener);
    }

    private void backListener(View v) {
        v.startAnimation(buttonClick);
        backSfx.start();
        finish();
    }

    private Bitmap getBitmapFromResources(Resources res, int resImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 1;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeResource(res, resImage, options);
    }
}

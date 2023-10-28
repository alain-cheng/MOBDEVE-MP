package com.mobdeve.s15.taboo;

import android.os.Bundle;

import org.godotengine.godot.GodotActivity;

public class GameActivity extends GodotActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
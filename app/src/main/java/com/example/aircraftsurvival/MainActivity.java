package com.example.aircraftsurvival;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //connects to log in screen
    //public void loginClick(View v){
    //}

    //connects new game button to game activity
    public void newClick(View v){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    //connects load game button to game activity
    public void loadlick(View v){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    //connects leaderboards button to leaderboards activity
    public void leaderboardsClick(View v){
        Intent intent = new Intent(MainActivity.this, LeaderboardsActivity.class);
        startActivity(intent);
    }

    //connects settings button to settings activity
    public void settingsClick(View v){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}

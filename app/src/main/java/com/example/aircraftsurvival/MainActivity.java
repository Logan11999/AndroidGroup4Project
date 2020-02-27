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

    //connects new game button to game activity
    public void newClick(View v){
        Intent intent = new Intent(MainActivity.this, gameActivity.class);
        startActivity(intent);
    }

    //connects load game button to game activity
    public void loadlick(View v){
        Intent intent = new Intent(MainActivity.this, gameActivity.class);
        startActivity(intent);
    }

    //connects leaderboards button to leaderboards activity
    public void leaderboardsClick(View v){
        Intent intent = new Intent(MainActivity.this, leaderboardsActivity.class);
        startActivity(intent);
    }

    //connects settings button to settings activity
    public void settingsClick(View v){
        Intent intent = new Intent(MainActivity.this, settingsActivity.class);
        startActivity(intent);
    }
}

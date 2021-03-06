package com.example.aircraftsurvival;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //read local highscore
        SharedPreferences prefs = getSharedPreferences("game",MODE_PRIVATE);
        TextView textView = findViewById(R.id.nameTV);
        textView.setText("Your Highscore is "+ prefs.getInt("highscore",0));

        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
    //connects to log in dialogue
    public void loginClick(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loginlayout);
        EditText usernameET = (EditText) dialog.findViewById(R.id.usernameET);
        EditText passwordET = (EditText) dialog.findViewById(R.id.passwordET);
        Button loginBTN = (Button) dialog.findViewById(R.id.enterBTN);
        Button setupBTN = (Button) dialog.findViewById(R.id.newUserBTN);
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //will verify login possibly on backendless
    public void existingLogin(){
    }
    //same for creating a user
    public void newLogin(){
    }

    //connects new game button to game activity
    public void newClick(View v){
        Intent intent = new Intent(MainActivity.this, InstructionActivity.class);
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

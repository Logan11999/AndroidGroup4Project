package com.example.aircraftsurvival;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instroction);



    }

    public void clickskip(View v){
        Intent in = new Intent(this,GameActivity.class);
        startActivity(in);


    }

}

package com.example.aircraftsurvival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.aircraftsurvival.GameView.screenRatioX;
import static com.example.aircraftsurvival.GameView.screenRatioY;

public class Enemy {
    public int speed = 20;
    public boolean wasshot = true;
    int x,y,width,height;
    Bitmap enemy;

    Enemy(Resources res){
        enemy = BitmapFactory.decodeResource(res,R.drawable.enemy);

        width = enemy.getWidth();
        height = enemy.getHeight();

        width /= 2;
        height /=2;

        width *=(int)screenRatioX;
        height *= (int)screenRatioY;

        y = -height;

    }

    Bitmap getEnemy(){
        return enemy;
    }

    Rect getCollision(){
        return new Rect(x,y,x+width,y+height);
    }
}

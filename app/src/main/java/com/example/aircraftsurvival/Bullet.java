package com.example.aircraftsurvival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.aircraftsurvival.GameView.screenRatioX;
import static com.example.aircraftsurvival.GameView.screenRatioY;

public class Bullet {
    int x,y,width,height;
    Bitmap bullet;

    Bullet(Resources res){
        bullet = BitmapFactory.decodeResource(res,R.drawable.bullet);
        width = bullet.getWidth();
        height = bullet.getHeight();



        width *= (int)screenRatioX;
        height *= (int)screenRatioY;

        bullet = Bitmap.createScaledBitmap(bullet,width,height,false);
    }

    Rect getCollision(){
        return new Rect(x,y,x+width,y+height);
    }
}

package com.example.aircraftsurvival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.aircraftsurvival.GameView.screenRatioX;
import static com.example.aircraftsurvival.GameView.screenRatioY;

public class Flight {
    public boolean isGoingUp = false;
    public int toShoot = 0;
    int x,y,width,height;
    Bitmap flight,crashed;
    private GameView gameView;

    Flight(GameView gameView, int screenY, Resources res){
        this.gameView = gameView;
        flight = BitmapFactory.decodeResource(res,R.drawable.flight);
        width = flight.getWidth();
        height = flight.getHeight();

        width /= 2;
        height /= 2;

        width *= (int)screenRatioX;
        height *= (int)screenRatioY;

        flight = Bitmap.createScaledBitmap(flight,width,height,false);

        crashed = BitmapFactory.decodeResource(res,R.drawable.crashed);
        crashed = Bitmap.createScaledBitmap(crashed,width,height,false);


        y = screenY/2;
        x = (int )(64*screenRatioX);
    }

    Bitmap getFlight(){
        if(toShoot != 0){
            toShoot--;
            gameView.newBullet();
        }


        return flight;
    }

    Rect getCollision(){
        return new Rect(x,y,x+width,y+height);
    }

    Bitmap getCrashed(){
        return crashed;
    }

}

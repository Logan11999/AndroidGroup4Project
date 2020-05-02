package com.example.aircraftsurvival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Backgroud {
    int x=0,y=0;
    Bitmap bg;

    Backgroud(int screenX, int screenY, Resources res){
        bg = BitmapFactory.decodeResource(res,R.drawable.backroundhd);
        bg = Bitmap.createScaledBitmap(bg,screenX,screenY,false);
    }
}

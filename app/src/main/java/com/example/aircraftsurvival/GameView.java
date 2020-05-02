package com.example.aircraftsurvival;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private  boolean isPlaying;
    private int screenX,screenY;
    private Backgroud background1, background2;
    private Paint paint;
    private float screenRatioX,screenRatioY;


    public GameView(Context context, int screenX, int screenY){
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f/screenX;
        screenRatioY = 1920f/screenY;
        background1 = new Backgroud(screenX,screenY,getResources());
        background2 = new Backgroud(screenX,screenY,getResources());

        background2.x = screenX;
        paint = new Paint();
    }

    @Override
    public void run() {
        while(isPlaying){
            update();
            draw();
            sleep();
        }
    }

    public void update(){
        background1.x -= 10*screenRatioX;
        background2.x -= 10*screenRatioX;

        if(background1.x + background1.bg.getWidth() < 0){
            background1.x = screenX;
        }
        if(background2.x + background2.bg.getWidth() < 0){
            background2.x = screenX;
        }
    }

    public void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.bg,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.bg,background2.x,background2.y,paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void sleep(){
        try {
            Thread.sleep(17);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

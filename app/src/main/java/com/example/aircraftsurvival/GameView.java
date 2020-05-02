package com.example.aircraftsurvival;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private  boolean isPlaying, isGameOver = false;
    private int screenX,screenY;
    private Backgroud background1, background2;
    private Paint paint;
    private Flight flight;
    public static float screenRatioX,screenRatioY;
    private List<Bullet> bullets;

    //enemy
    private  Enemy[] enemy;
    private Random random;


    public GameView(Context context, int screenX, int screenY){
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f/screenX;
        screenRatioY = 1080f/screenY;
        background1 = new Backgroud(screenX,screenY,getResources());
        background2 = new Backgroud(screenX,screenY,getResources());

        flight = new Flight(this,screenY,getResources());

        bullets = new ArrayList<>();

        background2.x = screenX;
        paint = new Paint();
        enemy = new Enemy[4];
        for(int i=0;i<4;i++){
            Enemy e = new Enemy(getResources());
            enemy[i]=e;
        }
        random = new Random();
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

        //speed of moving up and down
        if(flight.isGoingUp){
            flight.y -= 30*screenRatioY;
        }else {
            flight.y += 30*screenRatioY;
        }

        //constrain the flight in the screen
        if (flight.y<0){
            flight.y = 0;
        }
        if (flight.y > screenY - flight.height){
            flight.y = screenY - flight.height;
        }

        //bullets
        List<Bullet> trash = new ArrayList<>();
        for(Bullet b: bullets){
            if(b.x > screenX){
                trash.add(b);
            }
            b.x += 50 * screenRatioX;

            for(Enemy e:enemy){
                if(Rect.intersects(e.getCollision(),b.getCollision())){
                    e.x = -500;
                    b.x = screenX+ 500;
                    e.wasshot = true;
                }
            }
        }
        for (Bullet b : trash){
            bullets.remove(b);
        }

        for(Enemy e:enemy){
            e.x -= e.speed;
            if(e.x + e.width<0){
                if(!e.wasshot){
                    isGameOver = true;
                    return;
                }

                 int bound = 30* (int)screenRatioX;
                 e.speed = random.nextInt(bound);
                 if(e.speed<10*screenRatioX){
                     e.speed = 10*(int)screenRatioX;
                 }
                 e.x = screenX;
                 e.y = random.nextInt(screenY-e.height);

                 e.wasshot = false;
            }
            if (Rect.intersects(e.getCollision(),flight.getCollision())){
                isGameOver = true;
            }
        }
    }

    public void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.bg,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.bg,background2.x,background2.y,paint);
            if(isGameOver){
                isPlaying = false;
                canvas.drawBitmap(flight.getCrashed(),flight.x,flight.y,paint);
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }
            //draw flight
            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);
            //draw bullets
            for (Bullet b:bullets){
                canvas.drawBitmap(b.bullet,b.x,b.y,paint);
            }
            //draw enemy
            for(Enemy e: enemy){
                canvas.drawBitmap(e.enemy,e.x,e.y,paint);
            }

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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX/2){
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() > screenX / 2){
                    flight.toShoot++;
                }
                break;

        }

        return true;
    }

    public void newBullet() {
        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + flight.height/2;
        bullets.add(bullet);

    }
}

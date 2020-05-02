package com.example.aircraftsurvival;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private GameActivity activity;
    private  boolean isPlaying, isGameOver = false;
    private int screenX,screenY;
    private Backgroud background1, background2;
    private Paint paint;
    private Flight flight;
    public static float screenRatioX,screenRatioY;
    private List<Bullet> bullets;
    private SoundPool soundPool;
    private int sound;

    //enemy
    private  Enemy[] enemy;
    private Random random;

    //score
    private int score = 0;
    private SharedPreferences prefs;


    public GameView(GameActivity activity, int screenX, int screenY){
        super(activity);
        this.activity = activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sound = soundPool.load(activity,R.raw.laser,1);

        prefs = activity.getSharedPreferences("game",activity.MODE_PRIVATE);
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
        paint.setTextSize(128);
        paint.setColor(Color.BLUE);

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
            try {
                draw();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                    score++;
                    e.x = -500;
                    b.x = screenX+ 500;
                    e.wasshot = true;
                    e.getCrashed();
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

    public void draw() throws InterruptedException {
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.bg,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.bg,background2.x,background2.y,paint);

            canvas.drawText(score+"",screenX/2, 164,paint);
            if(isGameOver){
                isPlaying = false;
                canvas.drawBitmap(flight.getCrashed(),flight.x,flight.y,paint);
                saveIfHighScore();
                waitBeforeExit();
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
                if(e.wasshot){
                    canvas.drawBitmap(e.getCrashed(),e.x,e.y,paint);
                }
            }
            getHolder().unlockCanvasAndPost(canvas);
        }


    }

    private void waitBeforeExit() throws InterruptedException {
        Thread.sleep(3000);
        activity.startActivity(new Intent(activity,MainActivity.class));
        activity.finish();
    }

    private void saveIfHighScore() {
        if(prefs.getInt("highscore",0)<score){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore",score);
            editor.apply();
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
        soundPool.play(sound,1,1,0,0,1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + flight.height/2;
        bullets.add(bullet);

    }
}

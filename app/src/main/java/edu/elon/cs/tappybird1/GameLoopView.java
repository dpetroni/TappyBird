package edu.elon.cs.tappybird1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by DavidPetroni on 10/21/15.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback{

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private Bitmap bitmap;
    private int screenWidth,screenHeight;
    private double birdTime;
    protected MediaPlayer mp1;
    protected MediaPlayer mp2;
    protected MediaPlayer mp3;
    protected MediaPlayer mp4;
    private int count = 1;


    // Touch location for the gun shot
    private float touchX, touchY;



    public GameLoopView(Context context, AttributeSet attrs) {







        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // Remember the context for finding resources
        this.context = context;

        // Want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Game loop thread
        thread = new GameLoopThread();

        mp1 = MediaPlayer.create(context, R.raw.shot);
        mp2 = MediaPlayer.create(context, R.raw.shot);
        mp3 = MediaPlayer.create(context, R.raw.shot);
        mp4 = MediaPlayer.create(context, R.raw.shot);
    }


    public void startEndScreen(){

        Intent intent = new Intent(context, EndScreenActivity.class);
        context.startActivity(intent);
    }

    // SurfaceHolder.Callback methods:
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Thread exists, but is in terminated state
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameLoopThread();
        }

        // Start the game loop
        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    // touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // remember the last touch point
        if(MotionEvent.ACTION_DOWN == event.getAction()) {
            touchX = event.getX();
            touchY = event.getY();

            if(count == 1){
                mp1.start();
                count++;
            }
            if(count == 2){
                mp2.start();
                count++;
            }
            if(count == 3){
                mp3.start();
                count++;
            }
            if(count == 4){
                mp4.start();
                count = 1;
            }
        }
        if(MotionEvent.ACTION_MOVE == event.getAction()){
            touchX = -10;
            touchY = -10;

        }
        return true;
    }


    // Game Loop Thread
    private class GameLoopThread extends Thread {

        private boolean isRunning = false;
        private long lastTime;

        // the bird sprite
        //private Bird bird;
        private ArrayList<Bird> birds;
        private int NUM_BIRDS = 2;


        // the clouds
        private final int NUM_CLOUDS = 3;
        private ArrayList<Cloud> clouds;

        // frames per second calculation
        private int frames;
        private long nextUpdate;

        public GameLoopThread() {
            //bird = new Bird(context);

            birds = new ArrayList<Bird>();
            for (int i = 0; i < NUM_BIRDS; i++) {
                birds.add(new Bird(context));
            }

            clouds = new ArrayList<Cloud>();
            for (int i = 0; i < NUM_CLOUDS; i++) {
                clouds.add(new Cloud(context));
            }
//
//            touchX = bird.x;
//            touchY = bird.y;
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {

            lastTime = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();
                    double elapsed = (now - lastTime) / 1000.0;
                    birdTime = elapsed;
                    lastTime = now;

                    // update/draw
                    doUpdate(elapsed);
                    doDraw(canvas);


                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

            }

        }



         /* THE GAME */

        // move all objects in the game
        private void doUpdate(double elapsed) {

            if(birdTime%3 == 0){
                NUM_BIRDS++;
            }

            if(Bird.livesRemaining == 0){

                startEndScreen();
                return;
            }
            for (Cloud cloud : clouds) {
                cloud.doUpdate(elapsed);
            }
            for (Bird bird : birds) {

                bird.doUpdate(elapsed, touchX, touchY);
            }

        }

        // draw all objects in the game
        private void doDraw(Canvas canvas) {







            canvas.drawBitmap(bitmap,
                    null,
                    new Rect(0,0,screenWidth,screenHeight),
                    null);


            // draw the background
            //canvas.drawColor(Color.argb(255, 126, 192, 238));

            for (Cloud cloud : clouds) {
                cloud.doDraw(canvas);
            }
            for (Bird bird : birds) {
                bird.doDraw(canvas);

            }




            //bird.doDraw(canvas);
        }
    }

}

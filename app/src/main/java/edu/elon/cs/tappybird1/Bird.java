package edu.elon.cs.tappybird1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by DavidPetroni on 10/21/15.
 */

public class Bird extends Activity{
    private  float speed;
    protected float x, y;
    private float width, height;
    private Bitmap birdBitmap;
    private Bitmap bloodBitmap;
    protected static int livesRemaining = 3;
    private static int score = 0;
    public boolean showBlood = false;
    private Context context;
    protected static double bloodLength = .3;

    private double bloodTime = 0.0;


    //private final float SCALE = 1.0f;
    private final float MAX_SPEED = 14.0f;
    private final float MIN_SPEED = 5.0f;

    private int screenWidth, screenHeight;

    private final float SCALE = 0.1f;

    public Bird(Context context) {

        this.context = context;

        // get the image
        birdBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.swan);
        bloodBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood);
        // scale the size
        width = birdBitmap.getWidth() * SCALE;
        height = birdBitmap.getHeight() * SCALE;

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // start in random spot
        //x = (float) (Math.random() * screenWidth);
        x = 0;
        y = (float) (Math.random() * (screenHeight));

        speed = (float) (Math.random() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED);
    }

    public void doDraw(Canvas canvas) {
        // draw the bird
        if(showBlood == false) {
            canvas.drawBitmap(birdBitmap,
                    null,
                    new Rect((int) (x - width / 2), (int) (y - height / 2),
                            (int) (x + width / 2), (int) (y + height / 2)),
                    null);
        }
        else{
            canvas.drawBitmap(bloodBitmap,
                    null,
                    new Rect((int) (x - width / 2), (int) (y - height / 2),
                            (int) (x + width / 2), (int) (y + height / 2)),
                    null);

        }

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


        paint.setColor(Color.RED);
        paint.setTextSize(50);

        canvas.drawText("LIVES REMAINING: ", 10, 50, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawText(Integer.toString(livesRemaining), 450, 50, paint);

        canvas.drawText("SCORE: ", 850, 50, paint);

        canvas.drawText(Integer.toString(score), 1100, 50, paint);


    }


//    public void doUpdate(double elapsed, float touchX ,float touchY) {
//        // easing based on touch point
//        x = (float) (x + ((touchX - x) * elapsed *2));
//        y = (float) (y + ((touchY - y) * elapsed * 2));
//    }
public void doUpdate(double elapsed, float touchX, float touchY) {

    if (showBlood) {
        bloodTime += elapsed;
        if(bloodTime > bloodLength) {
            x = -width / 2;
            y = (float) (Math.random() * screenHeight);
            speed = (float) (Math.random() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED);
            showBlood = false;
            bloodTime = 0.0;
        }
    } else {

        // easing based on touch point
        x = (x + speed);
        // Is the bird off the screen?
        if (x - width / 2 > screenWidth) {
            livesRemaining--;
            // make it look like a new bird appears

            x = -width / 2;

            y = (float) (Math.random() * screenHeight);
            speed = (float) (Math.random() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED);
        }

    }
    if(livesRemaining == 0){

    }




    if (       touchX > (x-width/2)
            && touchX < (x+width/2)
            && touchY > (y-(height/2))
            && touchY < (y+height/2) ){

        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(100);



        score++;


        showBlood = true;

    }


}
}

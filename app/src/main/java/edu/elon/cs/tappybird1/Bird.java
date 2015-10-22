package edu.elon.cs.tappybird1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by DavidPetroni on 10/21/15.
 */
public class Bird {
    private  float speed;
    protected float x, y;
    private float width, height;
    private Bitmap bitmap;

    //private final float SCALE = 1.0f;
    private final float MAX_SPEED = 14.0f;
    private final float MIN_SPEED = 5.0f;

    private int screenWidth, screenHeight;

    private final float SCALE = 0.1f;

    public Bird(Context context) {

        // get the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jack);

        // scale the size
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

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
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y- height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }


//    public void doUpdate(double elapsed, float touchX ,float touchY) {
//        // easing based on touch point
//        x = (float) (x + ((touchX - x) * elapsed *2));
//        y = (float) (y + ((touchY - y) * elapsed * 2));
//    }
public void doUpdate(double elapsed) {
    // easing based on touch point
    x = (x + speed);

    // Is the cloud off the screen?
    if (x - width/2 > screenWidth) {
        // make it look like a new cloud appears

        x = -width/2;
        y = (float) (Math.random() * screenHeight);
        speed = (float) (Math.random() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED);
    }
}
}

package com.techexchange.mobileapps.lab14;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class GravityBouncingBallView extends View {

    private static final String TAG = "GravityBouncingBallView";

    private static final long DRAW_DELAY = 30;
    private static final float TIME_STEP = (float) DRAW_DELAY/1000;
    private static int speedX = 100;
    private static int speedY = 100;

    private Paint ovalColor = new Paint();
    private int ovalX = 100;
    private int ovalY = 100;
    private int ovalD = 200;
    private int screenW;
    private int screenH;

    private int ACCEL_GRAVITY = 600;

    // Having this constructor is necessary.
    public GravityBouncingBallView(Context context) {
        super(context);
        ovalColor.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // This is where we'll be drawing things...
        update();
        canvas.drawOval(ovalX, ovalY,ovalX+ovalD, ovalY+ovalD, ovalColor);

        try{
            Thread.sleep(DRAW_DELAY);
        } catch (InterruptedException ex){
            Log.e(TAG, "Sleep interrupted!", ex);
        }

        invalidate(); // Force a redraw
    }

    private void update() {
        // Perform physics simulations here...
        speedY += ACCEL_GRAVITY;

        ovalX += speedX * TIME_STEP;
        ovalY += speedY * TIME_STEP;

        if (ovalX >= screenW - ovalD){ // Right penetration
            speedX = -speedX;
            ovalX = ovalX - 2 * ((ovalX+ovalD) - screenW);
        } else if (ovalX <= 0){ // Left penetration
            speedX = -speedX;
            ovalX = -ovalX;
        }

        if (ovalY >= screenH - ovalD){ //Bottom penetration
            speedY -= ACCEL_GRAVITY;
            speedY = -speedY;
            ovalY = ovalY - 2 * ((ovalY+ovalD) - screenH);
        } else if (ovalY <= 0){ // Top penetration
            speedY = -speedY;
            ovalY = -ovalY;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
        Log.d(TAG, "onSizeChanged: screenW: "+screenW);
        Log.d(TAG, "onSizeChanged: screenH: "+screenH);
    }
}
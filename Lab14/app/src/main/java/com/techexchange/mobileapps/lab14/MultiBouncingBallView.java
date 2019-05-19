package com.techexchange.mobileapps.lab14;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MultiBouncingBallView extends View {

    private static final String TAG = "MultiBouncingBallView";

    private static final int NUM_BALLS = 20;
    private ArrayList<Ball> ball = new ArrayList<>();

    private static final long DRAW_DELAY = 30;
    private static final float TIME_STEP = (float) DRAW_DELAY/1000;

    int screenH;
    int screenW;

    int ovalD = 200;

    public MultiBouncingBallView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // This is where we'll be drawing things...

        for (Ball currBall: ball){
            update(currBall);
            canvas.drawOval(currBall.ovalX, currBall.ovalY, currBall.ovalX+currBall.ovalD, currBall.ovalY+currBall.ovalD, currBall.ovalColor);
        }

        try{
            Thread.sleep(DRAW_DELAY);
        } catch (InterruptedException ex){
            Log.e(TAG, "Sleep interrupted!", ex);
        }

        invalidate(); // Force a redraw
    }

    private void update(Ball currBall){
        // Perform physics simulations here...
        currBall.ovalX += currBall.speedX * TIME_STEP;
        currBall.ovalY += currBall.speedY * TIME_STEP;

        if (currBall.ovalX >= screenW - currBall.ovalD) { // Right penetration
            currBall.speedX = -currBall.speedX;
            currBall.ovalX = currBall.ovalX - 2 * ((currBall.ovalX + currBall.ovalD) - screenW);
        } else if (currBall.ovalX <= 0) { // Left penetration
            currBall.speedX = -currBall.speedX;
            currBall.ovalX = -currBall.ovalX;
        }
        if (currBall.ovalY >= screenH - currBall.ovalD) { // Bottom penetration
            currBall.speedY = -currBall.speedY;
            currBall.ovalY = currBall.ovalY - 2 * ((currBall.ovalY + currBall.ovalD) - screenH);
        } else if (currBall.ovalY <= 0) { // Top penetration
            currBall.speedY = -currBall.speedY;
            currBall.ovalY = -currBall.ovalY;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;

        for (int i = 0; i < NUM_BALLS; i++) {
            int posX = (int) (Math.random() * (screenW - ovalD));
            int posY = (int) (Math.random() * (screenH - ovalD));
            int speedX = (int) ((Math.random()-0.5) * 1000);
            int speedY = (int) ((Math.random()-0.5) * 1000);
            ball.add(new Ball(posX, posY, ovalD, speedX, speedY));
        }
    }
}

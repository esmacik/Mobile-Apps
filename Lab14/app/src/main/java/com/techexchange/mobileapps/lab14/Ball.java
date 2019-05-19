package com.techexchange.mobileapps.lab14;

import android.graphics.Color;
import android.graphics.Paint;

public class Ball{

    private static final String TAG = "Ball";

    Paint ovalColor = new Paint();

    int ovalX;
    int ovalY;
    int ovalD;

    int speedX;
    int speedY;

    public Ball(int ovalX, int ovalY, int ovalD, int speedX, int speedY) {
        int r = (int)(Math.random()*256);
        int g = (int)(Math.random()*256);
        int b = (int)(Math.random()*256);
        ovalColor.setColor(Color.rgb(r, g, b));
        this.ovalX = ovalX;
        this.ovalY = ovalY;
        this.ovalD = ovalD;
        this.speedX = speedX;
        this.speedY = speedY;
    }
}

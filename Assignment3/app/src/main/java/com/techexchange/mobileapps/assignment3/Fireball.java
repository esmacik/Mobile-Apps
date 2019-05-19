package com.techexchange.mobileapps.assignment3;

import android.graphics.RectF;

public class Fireball {

    private static final String TAG = "Fireball";

    public static int SPEED = 45;

    int index;
    int direction;
    RectF location;
    int frameStep;

    private Fireball(){ }

    public Fireball(int index, int direction, RectF location) {
        this.index = index;
        this.direction = direction;
        this.location = location;
        this.frameStep = 0;

        float inset = (location.right-location.left)/3;

        location.inset(inset, inset);
    }

    public void moveFireball(){
        switch (direction){
            case Direction.UP:
                location.set(location.left, location.top - SPEED, location.right,location.bottom - SPEED);
                break;
            case Direction.LEFT:
                location.set(location.left - SPEED, location.top, location.right - SPEED, location.bottom);
                break;
            case Direction.DOWN:
                location.set(location.left, location.top + SPEED, location.right, location.bottom + SPEED);
                break;
            case Direction.RIGHT:
                location.set(location.left + SPEED, location.top, location.right + SPEED, location.bottom);
                break;
            default:
                break;
        }
    }
}
package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

public class Tank {

    private static final String TAG = "Tank";

    public static int SPEED = 15;

    int identifier;
    Bitmap[] tankFrames;
    Rect tankLocation;
    boolean isMoving;
    int currAnimationFrame;
    int facingDirection;
    int index;
    int frameStep;
    boolean actionCancelled;
    Fireball fireball;

    public Tank(int identifier, Bitmap[] tankFrames, Rect tankLocation, int index) {
        this.identifier = identifier;
        this.tankFrames = tankFrames;
        this.tankLocation = tankLocation;
        this.isMoving = false;
        this.currAnimationFrame = 0;
        this.facingDirection = Direction.RIGHT;
        this.index = index;
        this.frameStep = 0;
        this.actionCancelled = false;
        this.fireball = null;
    }

    public void shootFireball(RectF fireballLocation){
        fireball = new Fireball(GameView.desiredIndex(index, facingDirection), facingDirection, fireballLocation);
    }

    public void rotateRight(){
        for (int i = 0; i < tankFrames.length; i++) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            tankFrames[i] = Bitmap.createBitmap(tankFrames[i], 0, 0, tankFrames[i].getWidth(), tankFrames[i].getHeight(), matrix, true);
        }
        facingDirection = (facingDirection+1) % Direction.NUM_DIRECTIONS;
    }

    public void move(int swipeDirection){
        switch (swipeDirection){
            case Direction.UP:
                tankLocation.set(tankLocation.left, tankLocation.top - SPEED, tankLocation.right,tankLocation.bottom - SPEED);
                break;
            case Direction.LEFT:
                tankLocation.set(tankLocation.left - SPEED, tankLocation.top, tankLocation.right - SPEED, tankLocation.bottom);
                break;
            case Direction.DOWN:
                tankLocation.set(tankLocation.left, tankLocation.top + SPEED, tankLocation.right, tankLocation.bottom + SPEED);
                break;
            case Direction.RIGHT:
                tankLocation.set(tankLocation.left + SPEED, tankLocation.top, tankLocation.right + SPEED, tankLocation.bottom);
                break;
            default:
                break;
        }
        forwardFrame();
    }

    private void forwardFrame(){
        if (currAnimationFrame == 0)
            currAnimationFrame = tankFrames.length - 1;
        else
            currAnimationFrame--;
    }
}
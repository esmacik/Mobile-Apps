package com.techexchange.mobileapps.slidingnumbers;

import android.graphics.Bitmap;

public class GameTile {

    public int tileValue;

    public Bitmap blueTile, greenTile;

    public GameTile(int tileValue, Bitmap blueTile, Bitmap greenTile){
        this.tileValue = tileValue;
        this.blueTile = blueTile;
        this.greenTile = greenTile;
    }

    public GameTile(){
        this.tileValue = 0;
    }
}
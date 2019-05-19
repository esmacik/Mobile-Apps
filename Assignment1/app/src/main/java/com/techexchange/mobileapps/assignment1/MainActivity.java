package com.techexchange.mobileapps.assignment1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //tileGrid hols current positions of all game pieces
    private GameTile[][] tileGrid = new GameTile[3][3];

    //imageView holds an array of bitmaps that corresponds with tileGrid
    private ImageView[][] tileView = new ImageView[3][3];

    //Bitmap arrays of blue and green tile bitmap resources
    private Bitmap[] blueTile = new Bitmap[9];
    private Bitmap[] greenTile = new Bitmap[9];

    //onCreate sets up the application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap tileImage = BitmapFactory.decodeResource(getResources(), R.drawable.numbers_sprite_100);
        getTiles(tileImage);
        //getBlueTiles(tileImage);
        //getGreenTiles(tileImage);

        ImageView[] arrayOfViewIDs = {findViewById(R.id.pos00), findViewById(R.id.pos01), findViewById(R.id.pos02),
                                      findViewById(R.id.pos10), findViewById(R.id.pos11), findViewById(R.id.pos12),
                                      findViewById(R.id.pos20), findViewById(R.id.pos21), findViewById(R.id.pos22)};

        int n = 0;
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[0].length; j++) {
                if (n == 0) {
                    tileGrid[0][0] = new GameTile();
                    tileView[0][0] = arrayOfViewIDs[n];
                    final int y = 0;
                    final int x = 0;
                    tileView[0][0].setOnClickListener(v -> onijPressed(y, x));
                } else {
                    tileGrid[i][j] = new GameTile(n, blueTile[n], greenTile[n]);
                    tileView[i][j] = arrayOfViewIDs[n];
                    tileView[i][j].setImageBitmap(tileGrid[i][j].blueTile);
                    final int y = i;
                    final int x = j;
                    tileView[i][j].setOnClickListener(v -> onijPressed(y, x));
                }
                n++;
            }
        }
        shuffleTiles(200);
    }

    //swapTiles will take the tile in position (i0, j0) and swap it with tile at position (i1, j1)
    //if statement guarantees that the two attemped swaps are adjacent
    private void swapTiles(int i0, int j0, int i1, int j1) {
        if ((Math.abs(i0 - i1) == 1 && j0 == j1) || (Math.abs(j0 - j1) == 1) && i0 == i1) {
            GameTile tempGT = tileGrid[i0][j0];
            tileGrid[i0][j0] = tileGrid[i1][j1];
            tileGrid[i1][j1] = tempGT;

            tileView[i0][j0].setImageBitmap(tileGrid[i0][j0].blueTile);
            tileView[i1][j1].setImageBitmap(tileGrid[i1][j1].blueTile);
        }
    }

    private void getTiles(Bitmap tileImage){
        Bitmap[][] bitmaps = new Bitmap[3][9];
        int widthj, heighti;
        widthj = tileImage.getWidth() / 10;
        heighti = tileImage.getHeight() / 3;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                bitmaps[i][j] = Bitmap.createBitmap(tileImage, j * widthj, i * heighti, widthj, heighti);
            }
        }
        blueTile = bitmaps[0];
        greenTile = bitmaps[2];
    }


    //Method that returns the coordinates of the current whitespace in an array of length 2
    private int[] findWhiteSpace(){
        int[] whiteSpaceCoordinates = {-1, -1};
        for (int i = 0; i < tileGrid.length; i++){
            for (int j = 0; j < tileGrid[0].length; j++){
                if (tileGrid[i][j].tileValue == 0){
                    whiteSpaceCoordinates[0] = i;
                    whiteSpaceCoordinates[1] = j;
                    return whiteSpaceCoordinates;
                }
            }
        }
        return whiteSpaceCoordinates;
    }

    //Method that performs n shuffles on game start. Guarantees every puzzle is solvable
    private void shuffleTiles(int n){
        if (n > 0){
            int[] whiteSpace = findWhiteSpace();
            //If white space coordinate is a 1, wither move it to coordinate 0 or coordinate 1
            int change;
            if (Math.random() < 0.5){
                change = 0;
            } else {
                change = 2;
            }
            //If less than 0.5, change i. If greater than .5, change j.
            if (Math.random() < 0.5){
                switch (whiteSpace[0]) {
                    case 0:
                        swapTiles(whiteSpace[0], whiteSpace[1], 1, whiteSpace[1]);
                        break;
                    case 1:
                        swapTiles(whiteSpace[0], whiteSpace[1], change, whiteSpace[1]);
                        break;
                    case 2:
                        swapTiles(whiteSpace[0], whiteSpace[1], 1, whiteSpace[1]);
                        break;
                    default:
                }
            } else {
                switch (whiteSpace[1]) {
                    case 0:
                        swapTiles(whiteSpace[0], whiteSpace[1], whiteSpace[0], 1);
                        break;
                    case 1:
                        swapTiles(whiteSpace[0], whiteSpace[1], whiteSpace[0], change);
                        break;
                    case 2:
                        swapTiles(whiteSpace[0], whiteSpace[1], whiteSpace[0], 1);
                        break;
                    default:
                }
            }
            shuffleTiles(n-1);
        }
    }

    //Method to determine if the game has been won by checking to see if each tile is in the
    //correct position
    private boolean gameWon(){
        int n = 1;
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[0].length; j++) {
                if (n == 9)
                    n = 0;
                if (tileGrid[i][j].tileValue != n)
                    return false;
                n++;
            }
        }
        return true;
    }

    //Called once the game is won. Changes each sprite to its corresponding green sprite and
    //disables any further moves
    private void setToGreenAndDisable(){
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[0].length; j++) {
                tileView[i][j].setImageBitmap(tileGrid[i][j].greenTile);
                tileView[i][j].setClickable(false);
            }
        }
        Toast.makeText(MainActivity.this, "You won! 😄", Toast.LENGTH_SHORT).show();
    }

    //Method that is called on each click. Finds whitespace, swaps if the move is legal, checks if
    //the game has been won, and sets sprites to green and disables them if the game has been won.
    private void onijPressed(int i, int j){
        int[] whiteSpaceCoordinates = findWhiteSpace();
        swapTiles(i, j, whiteSpaceCoordinates[0], whiteSpaceCoordinates[1]);
        if(gameWon())
            setToGreenAndDisable();
    }
}
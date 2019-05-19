package com.techexchange.mobileapps.assignment3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

    private static final String TAG = "GameView";

    private Context context;
    private boolean client;

    private static final int DRAW_DELAY = 10;
    static int ROWS;
    static int COLUMNS = 8;

    int screenHeight;
    int screenWidth;

    private Bitmap brick;
    private Paint darkShade = new Paint();
    private Paint redPaint = new Paint();
    private Paint textPaint = new Paint();

    private Bitmap[][] tanks;

    static int[] gameBoard;
    private int cellSize;

    private int TANK_FRAME_STEPS;
    private int FIREBALL_FRAME_STEPS;

    private Tank playerTank;
    private Tank opponentTank;

    private int playerSwipeDirection = Direction.NONE;
    private int opponentSwipeDirection = Direction.NONE;

    int playerScore = 0;
    int opponentScore = 0;
    public GameView(Context context, boolean client) {
        super(context);
        this.context = context;
        this.client = client;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < gameBoard.length; i++){
            if (gameBoard[i] == 2){
                canvas.drawBitmap(brick, null, getDrawRect(i), null);
            } else if (gameBoard[i] == 1){
                canvas.drawBitmap(brick, null, getDrawRect(i), darkShade);
            }
        }

        if (playerTank.fireball != null)
            canvas.drawOval(playerTank.fireball.location, redPaint);

        updatePlayerTank(playerTank);

        canvas.drawBitmap(opponentTank.tankFrames[opponentTank.currAnimationFrame], null, opponentTank.tankLocation, null);
        canvas.drawBitmap(playerTank.tankFrames[playerTank.currAnimationFrame], null, playerTank.tankLocation, null);

        try {
            Thread.sleep(DRAW_DELAY);
        } catch (InterruptedException ex) {
            Log.e(TAG, "Sleep interrupted!", ex);
        }

        invalidate();
    }

    private void updatePlayerTank(Tank tank){
        if (playerSwipeDirection == Direction.TAP) {
            int fireballStart = desiredIndex(playerTank.index, playerTank.facingDirection);
            if (onScreen(fireballStart, playerTank.facingDirection) && playerTank.fireball == null) {
                playerTank.shootFireball(getFireballRectF(playerTank.index));
            }
            playerSwipeDirection = Direction.NONE;
        }

        //If player tank is not moving
        if (!tank.isMoving) {
            if (tank.facingDirection == playerSwipeDirection) {
                tank.isMoving = true;
            } else if (playerSwipeDirection != Direction.NONE) {
                while (tank.facingDirection != playerSwipeDirection)
                    tank.rotateRight();
                playerSwipeDirection = Direction.NONE;
            }
        //If player tank is moving
        } else {
            if (tank.frameStep < TANK_FRAME_STEPS) {
                if (playerSwipeDirection == Direction.opposite(tank.facingDirection)) {
                    while (tank.facingDirection != playerSwipeDirection){
                        tank.rotateRight();
                    }
                    tank.frameStep = TANK_FRAME_STEPS - tank.frameStep;
                    tank.actionCancelled = true;
                } else {
                    if (validMove(tank.index, tank.facingDirection) || tank.actionCancelled) {
                        tank.move(tank.facingDirection);
                        tank.frameStep++;
                    } else {
                        tank.isMoving = false;
                        tank.frameStep = 0;
                        playerSwipeDirection = -1;
                    }
                }
            } else {
                if (!tank.actionCancelled) {
                    int desiredIndex = desiredIndex(tank.index, tank.facingDirection);
                    gameBoard[desiredIndex] = tank.identifier;
                    gameBoard[tank.index] = 0;
                    tank.index = desiredIndex;
                }
                playerSwipeDirection = -1;
                tank.isMoving = false;
                tank.frameStep = 0;
                tank.actionCancelled = false;

                logGameBoard();
            }
        }

        if (tank.fireball != null){
            if (!onScreen(tank.fireball.index, tank.fireball.direction)){
                tank.fireball = null;
                return;
            }
            if (gameBoard[tank.fireball.index] == 2 || gameBoard[tank.fireball.index] == 1){
                gameBoard[tank.fireball.index]--;
                tank.fireball = null;
                return;
            } else if (gameBoard[tank.fireball.index] == opponentTank.identifier){
                playerScore++;
                initializeGameBoard();
            }

            if (tank.fireball.frameStep < FIREBALL_FRAME_STEPS) {
                tank.fireball.frameStep++;
                tank.fireball.moveFireball();
            } else {
                int fireballDesiredIndex = desiredIndex(tank.fireball.index, tank.fireball.direction);
                if (!onScreen(fireballDesiredIndex, tank.fireball.direction)){
                    tank.fireball = null;
                    return;
                }
                tank.fireball.index = fireballDesiredIndex;
                tank.fireball.frameStep = 0;
            }
        }
    }

    private boolean validMove(int source, int direction){
        int desiredIndex = desiredIndex(source, direction);

        switch (direction){
            case Direction.UP:
                if (desiredIndex >= 0 && gameBoard[desiredIndex] == 0)
                    return true;
                break;
            case Direction.RIGHT:
                if (desiredIndex > 0 && desiredIndex < gameBoard.length && gameBoard[desiredIndex] == 0 && desiredIndex % COLUMNS != 0)
                    return true;
                break;
            case Direction.DOWN:
                if (desiredIndex < gameBoard.length && gameBoard[desiredIndex] == 0)
                    return true;
                break;
            case Direction.LEFT:
                if (desiredIndex >= 0 && desiredIndex < gameBoard.length && gameBoard[desiredIndex] == 0 && desiredIndex % COLUMNS != COLUMNS - 1)
                    return true;
                break;
        }
        return false;
    }

    public static int desiredIndex(int source, int direction){
        switch (direction){
            case Direction.UP:
                return source - COLUMNS;
            case Direction.RIGHT:
                return source + 1;
            case Direction.DOWN:
                return source + COLUMNS;
            case Direction.LEFT:
                return source - 1;
            default:
                return Integer.MIN_VALUE;
        }
    }

    private static boolean onScreen(int index, int direction) {
        switch (direction){
            case Direction.UP:
                if (index >= 0)
                    return true;
                break;
            case Direction.RIGHT:
                if (index < gameBoard.length && index % COLUMNS != 0)
                    return true;
                break;
            case Direction.DOWN:
                if (index < gameBoard.length)
                    return true;
                break;
            case Direction.LEFT:
                if (index >= 0 && index % COLUMNS != COLUMNS - 1)
                    return true;
                break;
        }
        return false;
    }

    private int x1, y1, x2, y2;
    private static final int MIN_SWIPE_DISTANCE = 50;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = (int) event.getX();
                y1 = (int) event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                x2 = (int) event.getX();
                y2 = (int) event.getY();

                int xChange = x1 - x2;
                int yChange = y1 - y2;

                if (Math.abs(xChange) >= MIN_SWIPE_DISTANCE || Math.abs(yChange) >= MIN_SWIPE_DISTANCE) {
                    if (Math.abs(xChange) > Math.abs(yChange)) {
                        if (xChange > 0) {
                            //Left playerSwipeDirection
                            playerSwipeDirection = Direction.LEFT;
                            opponentSwipeDirection = (int) (Math.random() * Direction.NUM_DIRECTIONS);
                        } else {
                            //Right playerSwipeDirection
                            playerSwipeDirection = Direction.RIGHT;
                            opponentSwipeDirection = (int) (Math.random() * Direction.NUM_DIRECTIONS);
                        }
                    } else {
                        if (yChange > 0) {
                            //Up playerSwipeDirection
                            playerSwipeDirection = Direction.UP;
                            opponentSwipeDirection = (int) (Math.random() * Direction.NUM_DIRECTIONS);
                        } else {
                            //Down playerSwipeDirection
                            playerSwipeDirection = Direction.DOWN;
                            opponentSwipeDirection = (int) (Math.random() * Direction.NUM_DIRECTIONS);
                        }
                    }
                } else {
                    Log.d(TAG, "onTouchEvent: tap");
                    playerSwipeDirection = Direction.TAP;
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void readBitmaps() {
        Resources resources = getResources();
        brick = BitmapFactory.decodeResource(resources, R.drawable.brick);

        Bitmap tanksBitmap = BitmapFactory.decodeResource(resources, R.drawable.multicolortanks);
        tanks = new Bitmap[8][8];
        int heightWidth = tanksBitmap.getHeight() / 8;
        for (int i = 0; i < tanks.length; i++) {
            for (int j = 0; j < tanks[0].length; j++) {
                tanks[j][i] = Bitmap.createBitmap(tanksBitmap, i * heightWidth, j * heightWidth, heightWidth, heightWidth);
            }
        }
    }

    private Rect getDrawRect(int index){
        int[] coordinates = new int[4];
        int colNum = index % COLUMNS;
        int rowNum = index / COLUMNS;
        coordinates[0] = colNum * cellSize;
        coordinates[1] = rowNum * cellSize;
        coordinates[2] = (colNum * cellSize) + cellSize;
        coordinates[3] = (rowNum * cellSize) + cellSize;
        return new Rect(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    private RectF getFireballRectF(int index){
        int[] coordinates = new int[4];
        int colNum = index % COLUMNS;
        int rowNum = index / COLUMNS;
        coordinates[0] = colNum * cellSize;
        coordinates[1] = rowNum * cellSize;
        coordinates[2] = (colNum * cellSize) + cellSize;
        coordinates[3] = (rowNum * cellSize) + cellSize;
        return new RectF(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenHeight = h;
        screenWidth = w;

        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);
        darkShade.setColorFilter(filter);

        redPaint.setColor(Color.RED);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(w/3);

        cellSize = w / COLUMNS;

        ROWS = h / cellSize;

        TANK_FRAME_STEPS = cellSize / Tank.SPEED;
        FIREBALL_FRAME_STEPS = cellSize / Fireball.SPEED;

        Log.d(TAG, "onSizeChanged: cellHeight: "+cellSize);
        Log.d(TAG, "onSizeChanged: cellSize: "+ cellSize);

        initializeGameBoard();
    }

    private void initializeGameBoard(){
        readBitmaps();

        gameBoard = new int[ROWS * COLUMNS];

        int[] tankColors = twoUniqueIndices(8);

        int playerIndex = 0;
        Rect playerBitmapLocation = getDrawRect(playerIndex);
        playerTank = new Tank(-1, tanks[tankColors[0]], playerBitmapLocation, playerIndex);
        while (playerTank.facingDirection != Direction.RIGHT)
            playerTank.rotateRight();

        int opponentIndex = gameBoard.length - 1;
        Rect opponentBitmapLocation = getDrawRect(opponentIndex);
        opponentTank = new Tank(-2, tanks[tankColors[1]], opponentBitmapLocation, opponentIndex);
        while (opponentTank.facingDirection != Direction.RIGHT)
            playerTank.rotateRight();

        gameBoard[playerIndex] = playerTank.identifier;
        gameBoard[opponentIndex] = opponentTank.identifier;

        for (int i = 0; i < 25; i++){
            int randIndex = (int) (Math.random() * ROWS * COLUMNS);
            if (gameBoard[randIndex] == 0){
                gameBoard[randIndex] = 2;
            }
        }

        logGameBoard();

        Toast.makeText(context, "Score: "+playerScore+" - "+opponentScore, Toast.LENGTH_SHORT).show();
    }

    private int[] twoUniqueIndices(int top){
        int[] indicies = new int[2];
        int firstNum = (int) (Math.random() * top);
        int secondNum = (int) (Math.random() * top);
        while (secondNum == firstNum)
            secondNum = (int) (Math.random() * top);
        indicies[0] = firstNum;
        indicies[1] = secondNum;
        return indicies;
    }

    private void logGameBoard() {
        Log.d(TAG, "logGameBoard: ");
        for (int i = 0; i < ROWS; i++) {
            StringBuilder rowString = new StringBuilder("ROW "+i+"- ");
            for (int j = 0; j < COLUMNS; j++) {
                rowString.append(gameBoard[COLUMNS * i + j] + " ");
            }
            Log.d(TAG, "logGameBoard: " + rowString.toString());
        }
        Log.d(TAG, "logGameBoard: ");
    }
}
package com.techexchange.mobileapps.assignment3;

public final class Direction {

    public static final int NONE = -1;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int NUM_DIRECTIONS = 4;
    public static final int TAP = 5;

    private Direction(){ }

    public static int opposite(int direction){
        switch (direction){
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                return RIGHT;
        }
    }
}
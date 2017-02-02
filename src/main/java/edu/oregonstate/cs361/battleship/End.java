package edu.oregonstate.cs361.battleship;

/**
 * Created by casters on 2/1/17.
 * Same as Start class but only used in Ship objects
 */
public class End {
    public int Across = 0;
    public int Down = 0;
    public void setEnd(int x, int y){
        Across = x;
        Down = y;
    }
}

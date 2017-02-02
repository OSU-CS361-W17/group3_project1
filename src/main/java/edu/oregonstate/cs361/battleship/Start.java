package edu.oregonstate.cs361.battleship;

/**
 * Created by casters on 2/1/17.
 * Holds an x,y coordinate in the form of an ordered keyed pair, x being indicated by "Across:" y being indicated by "Down".
 * Held by Ship class and all children
 * Also used in newModel() as coordinates for hits and misses
 */
public class Start {
    public int Across = 0;
    public int Down = 0;
    public void setStart(int x, int y) {
        Across = x;
        Down = y;
    }
}

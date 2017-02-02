package edu.oregonstate.cs361.battleship;

/**
 * Created by casters on 2/1/17.
 * Parent class of all ships, passes on start and end coordinates that get set by newModel() or placeShip(), if they're computer or
 * player respectively. Name and length declared in child classes.
 */
public class Ship {
    Start start = new Start();
    End end = new End();
}

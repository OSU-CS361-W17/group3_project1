package edu.oregonstate.cs361.battleship;

/**
 * Created by casters on 2/1/17.
 * Holds ship objects and arrays of objects containing x,y coordinates
 */
public class BattleshipModel {
    AircraftCarrier aircraftCarrier = new AircraftCarrier();
    Battleship battleship = new Battleship();
    Cruiser cruiser = new Cruiser();
    Destroyer destroyer = new Destroyer();
    Submarine submarine = new Submarine();
    Computer_AircraftCarrier computer_aircraftCarrier = new Computer_AircraftCarrier();
    Computer_Battleship computer_battleship = new Computer_Battleship();
    Computer_Cruiser computer_cruiser = new Computer_Cruiser();
    Computer_Destroyer computer_destroyer = new Computer_Destroyer();
    Computer_Submarine computer_submarine = new Computer_Submarine();
    //Array of start objects used for coordinates, because client side expects them in ordered paris with "Across" and "Down" keys 
    Start[] playerHits = new Start[0];
    Start[] playerMisses = new Start[0];
    Start[] computerHits = new Start[0];
    Start[] computerMisses = new Start[0];
}

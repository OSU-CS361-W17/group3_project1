package edu.oregonstate.cs361.battleship;

/**
 * Created by casters on 2/1/17.
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
    Start[] playerHits = new Start[20];
    Start[] playerMisses = new Start[100];
    Start[] computerHits = new Start[20];
    Start[] computerMisses = new Start[100];
}

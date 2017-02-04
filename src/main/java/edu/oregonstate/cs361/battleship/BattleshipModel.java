package edu.oregonstate.cs361.battleship;

/**
 * Created by michaelhilton on 1/26/17.
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

    //Returns an array of Start objects with the coordinates of every occupied square on the board of the computer or player, depending on the whichBoard argument

    public Start[] occupiedSquares(BattleshipModel theBoard, String whichBoard){
        Start[] retSquares = new Start[16];
        Start[] tempSquares = new Start[5];
        int retSize = 0;
        if(whichBoard=="computer"){
            System.out.println("computer");
            //occHelper makes an array the size of concerned ship, addSquares appends the coordinates to the array to be returned by occupiedSquares
            tempSquares = occHelper(theBoard.computer_aircraftCarrier, 5);
            addSquares(retSquares, tempSquares, 0, 5);
            tempSquares = occHelper(theBoard.computer_battleship, 4);
            addSquares(retSquares, tempSquares, 5, 4);
            tempSquares = occHelper(theBoard.computer_cruiser, 3);
            addSquares(retSquares, tempSquares, 9, 3);
            tempSquares = occHelper(theBoard.computer_destroyer, 2);
            addSquares(retSquares, tempSquares, 12, 2);
            tempSquares = occHelper(theBoard.computer_submarine, 2);
            addSquares(retSquares, tempSquares, 14, 2);
        } else {
            System.out.println("player");
            tempSquares = occHelper(theBoard.aircraftCarrier, 5);
            addSquares(retSquares, tempSquares, 0, 5);
            tempSquares = occHelper(theBoard.battleship, 4);
            addSquares(retSquares, tempSquares, 5, 4);
            tempSquares = occHelper(theBoard.cruiser, 3);
            addSquares(retSquares, tempSquares, 9, 3);
            tempSquares = occHelper(theBoard.destroyer, 2);
            addSquares(retSquares, tempSquares, 12, 2);
            tempSquares = occHelper(theBoard.submarine, 2);
            addSquares(retSquares, tempSquares, 14, 2);
        }
        return retSquares;
    }
    public Start[] occHelper(Ship current, int shipLen){
        Start tempStart = new Start();
        tempStart.setStart(0,0);
        Start[] retStarts = new Start[16];
        for(int a = 0; a<16; a++){
            retStarts[a] = new Start();
        }

        int startX = current.start.Across;
        int endX = current.end.Across;
        int startY = current.start.Down;
        int endY = current.end.Down;
        int retSize = 0;
        int x;
        int y;
        if(startX==endX){
            for(int a = 0; a<(endY - startY); a++){
                y = startY + a;
                x = startX;
                tempStart.setStart(x, y);
                retStarts[retSize].setStart(x,y);
                retSize++;
            }
        } else {
            for(int a = 0; a<(endX - startX); a++){
                y = startY;
                x = startX + a;
                tempStart.setStart(x,y);
                retStarts[retSize].setStart(x,y);
                retSize++;
            }
        }
        return retStarts;
    }
    public void addSquares(Start[] retSquares, Start[] tempSquares, int retBeg, int tempSize){
        for(int a = retBeg; a<(retBeg + tempSize); a++){
            retSquares[a] = tempSquares[a-retBeg];
        }
    }
}
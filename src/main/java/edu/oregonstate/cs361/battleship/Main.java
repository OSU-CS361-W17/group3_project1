package edu.oregonstate.cs361.battleship;

import com.google.gson.Gson;
import spark.Request;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Main {

    public static void main(String[] args) {
        //This will allow us to server the static pages such as index.html, app.js, etc.
        staticFiles.location("/public");

        //This will listen to GET requests to /model and return a clean new model
        get("/model", (req, res) -> newModel());
        //This will listen to POST requests and expects to receive a game model, as well as location to fire to
        post("/fire/:row/:col", (req, res) -> fireAt(req));
        //This will listen to POST requests and expects to receive a game model, as well as location to place the ship
        post("/placeShip/:id/:row/:col/:orientation", (req, res) -> placeShip(req));
    }

    //This function should return a new model
    static String newModel() {
        //creates a new board with all ships starting and ending at (0,0)
        BattleshipModel theBoard = new BattleshipModel();
        //only sets start and end coordinates for computer


        theBoard.computer_aircraftCarrier.start.setStart(2,2);
        theBoard.computer_aircraftCarrier.end.setEnd(2,7);
        theBoard.computer_battleship.start.setStart(2,8);
        theBoard.computer_battleship.end.setEnd(6,8);
        theBoard.computer_cruiser.start.setStart(4,1);
        theBoard.computer_cruiser.end.setEnd(4,4);
        theBoard.computer_destroyer.start.setStart(7,3);
        theBoard.computer_destroyer.end.setEnd(7,5);
        theBoard.computer_submarine.start.setStart(9,6);
        theBoard.computer_submarine.end.setEnd(9,8);
        /*
        IMPORTANT: this is how occupiedSquares() is called, it's picky.
        Start[] compSquares = new Start[16];
        for(int a = 0; a<16; a++){
            compSquares[a] = new Start();
        }
        compSquares = theBoard.occupiedSquares(theBoard, "computer");
        for(int a = 0; a<16; a++){
            System.out.print(compSquares[a].Across);
            System.out.print(",");
            System.out.println(compSquares[a].Down);
        }
        */

        //Makes a gson obj to take in java objects
        Gson retobj = new Gson();
        //puts gson obj in string for return to GET
        String retstring = retobj.toJson(theBoard);
        return retstring;
    }

    //This function should accept an HTTP request and deserialize it into an actual Java object.

    protected static BattleshipModel getModelFromReq(Request req){

        Gson gson = new Gson();                                                                //creates a new Gson class variable
        BattleshipModel battleshipmodel = gson.fromJson(req.body(), BattleshipModel.class);    //parses game model from json to a java object
        return battleshipmodel;
    }



   protected static String placeShip(Request req) {

        //Gets all the information from the user in a form that the function can use
        //shiptype is the type of ship the user wants to place example:aircraftCarrier
        String shiptype = req.params(":id");
        //row and col are the variables where the start cordatates are stored
        int row = Integer.parseInt(req.params(":row"));
       // System.out.println(row);
        int col = Integer.parseInt(req.params(":col"));
        // Ore is where the orientation of the ship is stored
        String ore = req.params(":orientation");



        //mine gets the game board model using the getModelFromReq function
        BattleshipModel mine = getModelFromReq(req);

        // endrow (end row) and endcol (end col) are variables used to find the end point for the ship
        int endrow = row;
        int endcol = col;
        //lorr (left or right) is used to make sure a ship does not go off the board
        int lorr = 1;


        if (shiptype.equals("aircraftCarrier")) {
            int leng = mine.aircraftCarrier.length;
            //System.out.println(leng);
            if ((row + leng == 10)&(ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10)&(ore.equals("horizontal"))) {

                lorr = -1;
            }
            leng = leng * lorr;
            //System.out.println(leng);
            if (ore.equals("horizontal")) {
                endcol = col + leng;
                endrow = row;
                //System.out.println("in h");
            } else {
                endrow = row + leng;
                endcol = col;
            }
            mine.aircraftCarrier.start.setStart(col, row);
            //System.out.println(mine.aircraftCarrier.start.Across);
            mine.aircraftCarrier.end.setEnd(endcol,endrow);
        } else if (shiptype.equals("battleship")) {
            int leng = mine.battleship.length;
            /*if (ore.equals("vertical")) {
                int myhold = leng + row;
                if (10 <= myhold) {
                    lorr = -1;
                }
            } else if (ore.equals("horizontal")) {
                if (col + leng >= 10) {
                    lorr = -1;
                }
            }*/
            //leng = leng * lorr;
            if (ore.equals("horizontal")) {
                endcol = col + leng;
                endrow = row;
            } else {
                endrow = row + leng;
                endcol = col;
            }
            //System.out.println(lorr);
            /*if (lorr == -1) {
                int hold = endrow;
                endrow = row;
                row = hold;
                hold = endcol;
                endcol = col;
                col = hold;
            }*/
            mine.battleship.start.setStart(col, row);
            mine.battleship.end.setEnd(endcol, endrow);
        } else if (shiptype.equals("cruiser")) {
            int leng = mine.cruiser.length;
            if ((row + leng == 10)&(ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10)&(ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore.equals("horizontal")) {
                endcol = col + leng;
                endrow = row;
            } else {
                endrow = row + leng;
                endcol = col;
            }
            mine.cruiser.start.setStart(col, row);
            mine.cruiser.end.setEnd(endcol, endrow);
        } else if (shiptype.equals("destroyer")) {
            int leng = mine.destroyer.length;
            if ((row + leng == 10)&(ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10)&(ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore.equals("horizontal")) {
                endcol = col + leng;
                endrow = row;
            } else {
                endrow = row + leng;
                endcol = col;
            }
            mine.destroyer.start.setStart(col, row);
            mine.destroyer.end.setEnd(endcol, endrow);
        } else  {                                      //if (shiptype == "Submarine")
            int leng = mine.submarine.length;
            if ((row + leng == 10)&(ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10)&(ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore.equals("horizontal")) {
                endcol = col + leng;
                endrow = row;
            } else {
                endrow = row + leng;
                endcol = col;
            }
            mine.submarine.start.setStart(col, row);
            mine.submarine.end.setEnd(endcol, endrow);

        }
        //System.out.println(shiptype);
        //System.out.println(ore);
        // test

       Gson mygson = new Gson();
       //puts gson obj in string for return to GET
       String myreturn = mygson.toJson(mine);
       return myreturn;
    }

    //Called from fireAt, calls 2 more functions to check for a hit
    public static int check_ship_for_hit(BattleshipModel theBoard, Ship ship1, int shot_x, int shot_y){
        //System.out.println(ship1.start.Across + " " + ship1.start.Down);
        //System.out.println(ship1.end.Across + " " + ship1.end.Down);

        int ship_end_x = ship1.end.Across;
        int ship_end_y = ship1.end.Down;
        int ship_start_x = ship1.start.Across;
        int ship_start_y = ship1.start.Down;

        int fire_status;  //fire_hit is 1 for hit, 0 for miss
        fire_status = check_ship_ore(ship_start_x, ship_start_y, ship_end_x, ship_end_y, shot_x, shot_y);
        return fire_status;
    }

    //middle function, figures out orientation of ship from coordinates
    public static int check_ship_ore(int start_x, int start_y, int end_x, int end_y, int shot_x, int shot_y) {
        if (end_x - start_x != 0) { //Checks which way ship is oriented
            int status = check_if_hit(start_x, end_x, start_y, shot_x, shot_y); //calls next function
            return status;          // status is either a 1 for a hit or 0 for a miss
        }
        else if (end_y - start_y != 0) {
            int status = check_if_hit(start_y, end_y, start_x, shot_x, shot_y);
            return status;
        }
        return 0; //Error
    }

    //third function in the chain to be called. Actually compares ship squares to shot square
    public static int check_if_hit(int ship_start, int ship_end, int ship_pos, int shot_x, int shot_y) {
        if (ship_end > ship_start) { //If the end is larger than the start
            int ship_min = ship_start; //then the start is the min
            int ship_max = ship_end;
            System.out.println(ship_min);
            System.out.println(ship_max);
            for (int b = ship_min; b < ship_max + 1; b++) { //loop through all the coordinates occupied by ship
                //System.out.println(b + " " + ship_pos);
                if (shot_x == b && shot_y == ship_pos) { //if the coordinates match...
                    System.out.println("HIT"); //its a hit
                    return 1;
                }
            }
        } else if (ship_end < ship_start) {
            int ship_min = ship_end;
            int ship_max = ship_start;
            System.out.println(ship_min);
            System.out.println(ship_max);
            for (int b = ship_min; b < ship_max + 1; b++) {
                //System.out.println(b + " " + ship_pos);
                if (shot_x == b && shot_y == ship_pos) {
                    System.out.println("HIT");
                    return 1;
                }
            }
        }
        System.out.println("MISS");
        return 0;
    }

    public static BattleshipModel update_Player_Miss(int hit, BattleshipModel theBoard, int x, int y){
        if (hit == 0) { //If it was a miss...
            int playermisses_length = theBoard.playerMisses.length;
            Start[] newplayermisses = new Start[playermisses_length + 1];
            for (int i = 0; i < playermisses_length; i++) {
                newplayermisses[i] = new Start();
                newplayermisses[i] = theBoard.playerMisses[i]; //copy playermisses to new array
            }
            newplayermisses[playermisses_length] = new Start(); //add one more member to new playermisses array
            newplayermisses[playermisses_length].setStart(x, y);
            theBoard.playerMisses = newplayermisses; //set old array to new array
            System.out.println("playermisses array:");
            for (int j = 0; j < (playermisses_length + 1); j++) { //print out the array
                System.out.println(j + ":" + "[" + theBoard.playerMisses[j].Across + " " + theBoard.playerMisses[j].Down + "]");
            }
            return theBoard;
        }
        if (hit == 1) {
            int playerhits_length = theBoard.playerHits.length;     //gets length of playermisses
            Start[] newplayerhits = new Start[playerhits_length + 1];
            for (int i = 0; i < playerhits_length; i++) {
                newplayerhits[i] = new Start();
                newplayerhits[i] = theBoard.playerHits[i]; //copy playermisses to new array
            }
            newplayerhits[playerhits_length] = new Start();//add one more member to new playermisses array
            newplayerhits[playerhits_length].setStart(x, y);
            theBoard.playerHits = newplayerhits; //set old array to new array
            System.out.println("playerHits array:");
            for (int j = 0; j < (playerhits_length + 1); j++) {//print out the array
                System.out.println(j + ":" + "[" + theBoard.playerHits[j].Across + " " + theBoard.playerHits[j].Down + "]");

            }
            return theBoard;
        }
        return theBoard;
    }

    //Similar to placeShip, but with firing.
    protected static String fireAt(Request req) {
        BattleshipModel theBoard = getModelFromReq( req );

        int fire_row = Integer.parseInt(req.params(":row"));
        int fire_col = Integer.parseInt(req.params(":col"));
        int fire_hit = 0;  //fire_hit is set to 1 if a hit is found

        //Checking each ship for a hit, assuming no ships overlap
        fire_hit += check_ship_for_hit(theBoard, theBoard.aircraftCarrier, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.cruiser, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.battleship, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.destroyer, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.submarine, fire_row, fire_col);

        //Updates appropriate arrays with coordinates of shot
        theBoard = update_Player_Miss(fire_hit, theBoard, fire_row, fire_col);

        Gson gson = new Gson();
        //puts gson obj in string for return to GET
        String temp = gson.toJson(theBoard);
        return temp;
  
    }
}
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
        theBoard.computer_aircraftCarrier.start.setStart(2, 2);
        theBoard.computer_aircraftCarrier.end.setEnd(2, 7);
        theBoard.computer_battleship.start.setStart(2, 8);
        theBoard.computer_battleship.end.setEnd(6, 8);
        theBoard.computer_cruiser.start.setStart(4, 1);
        theBoard.computer_cruiser.end.setEnd(4, 4);
        theBoard.computer_destroyer.start.setStart(7, 3);
        theBoard.computer_destroyer.end.setEnd(7, 5);
        theBoard.computer_submarine.start.setStart(9, 6);
        theBoard.computer_submarine.end.setEnd(9, 8);
        //Makes a gson obj to take in java objects
        Gson retobj = new Gson();
        //puts gson obj in string for return to GET
        String retstring = retobj.toJson(theBoard);
        return retstring;
    }

    //This function should accept an HTTP request and deserialize it into an actual Java object.
    private static BattleshipModel getModelFromReq(Request req) {
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
        int col = Integer.parseInt(req.params(":col"));
        // Ore is where the orientation of the ship is stored
        String ore = req.params(":orientation");
        //mine gets the game board model using the getModelFromReq function
        BattleshipModel mine = getModelFromReq(req);


        // thex (the x) and they (the y) are variables used to find the end point for the ship
        int thex = row;
        int they = col;
        //lorr (left or right) is used to make sure a ship does not go off the board
        int lorr = 1;

        if (shiptype.equals("aircraftCarrier")) {
            int leng = mine.aircraftCarrier.length;
            //System.out.println(leng);
            if ((row + leng == 10) & (ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10) & (ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            //System.out.println(leng);
            if (ore == "horizontal") {
                thex = col + leng - 1;
                they = row;
            } else {
                they = row + leng - 1;
                thex = col;
            }
            mine.aircraftCarrier.start.setStart(row, col);
            //System.out.println(mine.aircraftCarrier.start.Across);
            mine.aircraftCarrier.end.setEnd(they, thex);
        } else if (shiptype.equals("battleship")) {
            int leng = mine.battleship.length;
            if ((row + leng == 10) & (ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10) & (ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore == "horizontal") {
                thex = col + leng - 1;
                they = row;
            } else {
                they = row + leng - 1;
                thex = col;
            }
            mine.battleship.start.setStart(row, col);
            mine.battleship.end.setEnd(they, thex);
        } else if (shiptype.equals("cruiser")) {
            int leng = mine.cruiser.length;
            if ((row + leng == 10) & (ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10) & (ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore == "horizontal") {
                thex = col + leng - 1;
                they = row;
            } else {
                they = row + leng - 1;
                thex = col;
            }
            mine.cruiser.start.setStart(row, col);
            mine.cruiser.end.setEnd(they, thex);
        } else if (shiptype.equals("destroyer")) {
            int leng = mine.destroyer.length;
            if ((row + leng == 10) & (ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10) & (ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore == "horizontal") {
                thex = col + leng - 1;
                they = row;
            } else {
                they = row + leng - 1;
                thex = col;
            }
            mine.destroyer.start.setStart(row, col);
            mine.destroyer.end.setEnd(they, thex);
        } else {                                      //if (shiptype == "Submarine")
            int leng = mine.submarine.length;
            if ((row + leng == 10) & (ore.equals("vertical"))) {
                lorr = -1;
            } else if ((col + leng == 10) & (ore.equals("horizontal"))) {
                lorr = -1;
            }
            leng = leng * lorr;
            if (ore == "horizontal") {
                thex = col + leng - 1;
                they = row;
            } else {
                they = row + leng - 1;
                thex = col;
            }
            mine.submarine.start.setStart(row, col);
            mine.submarine.end.setEnd(they, thex);
        }
        //System.out.println(shiptype);
        //System.out.println(ore);
        // test
        Gson mygson = new Gson();
        //puts gson obj in string for return to GET
        String myreturn = mygson.toJson(mine);
        return myreturn;
    }

    public static int check_ship_for_hit(BattleshipModel theBoard, Ship ship1, int shot_x, int shot_y){
        System.out.print("HERE");
        System.out.println(ship1.start.Across + " " + ship1.start.Down);
        System.out.println(ship1.end.Across + " " + ship1.end.Down);

        int ship_end_x = ship1.end.Across;
        int ship_end_y = ship1.end.Down;
        int ship_start_x = ship1.start.Across;
        int ship_start_y = ship1.start.Down;

        int fire_status;  //fire_hit
        fire_status = check_ship_ore(ship_start_x, ship_start_y, ship_end_x, ship_end_y, shot_x, shot_y);

        System.out.println("Fire Status");
        System.out.println(fire_status);

        return fire_status;
    }

    public static int check_ship_ore(int start_x, int start_y, int end_x, int end_y, int shot_x, int shot_y) {
        if (end_x - start_x != 0) {
            int status = check_if_hit(start_x, end_x, start_y, shot_x, shot_y);
            System.out.println("Status:");
            System.out.println(status);
            return status;
        } else if (end_y - start_y != 0) {
            int status = check_if_hit(start_y, end_y, start_x, shot_x, shot_y);
            System.out.println("Status:");
            System.out.println(status);
            return status;
        }
        return 0; //Error
    }

    public static int check_if_hit(int ship_start, int ship_end, int ship_pos, int shot_x, int shot_y) {
        if (ship_end > ship_start) { //If the end is larger than the start
            int ship_min = ship_start;
            int ship_max = ship_end;
            System.out.println(ship_min);
            System.out.println(ship_max);
            for (int b = ship_min; b < ship_max + 1; b++) {
                System.out.println(b + " " + ship_pos);
                if (shot_x == b && shot_y == ship_pos) {
                    System.out.println("HIT");
                    return 1;
                }
            }
        } else if (ship_end < ship_start) {
            int ship_min = ship_end;
            int ship_max = ship_start;
            System.out.println(ship_min);
            System.out.println(ship_max);
            for (int b = ship_min; b < ship_max + 1; b++) {
                System.out.println(b + " " + ship_pos);
                if (shot_x == b && shot_y == ship_pos) {
                    System.out.println("HIT");
                    return 1;
                }
            }
        }
        return 0;
    }

    public static BattleshipModel update_Player_Miss(int hit, BattleshipModel theBoard, int x, int y){
        if (hit == 0) {
            int playermisses_length = theBoard.playerMisses.length;     //gets length of playermisses
            Start[] newplayermisses = new Start[playermisses_length + 1];
            for (int i = 0; i < playermisses_length; i++) {
                newplayermisses[i] = new Start();
                newplayermisses[i] = theBoard.playerMisses[i];
            }
            newplayermisses[playermisses_length] = new Start();
            newplayermisses[playermisses_length].setStart(x, y);
            theBoard.playerMisses = newplayermisses;
            System.out.println("playermisses array:");
            for (int j = 0; j < (playermisses_length + 1); j++) {
                System.out.println(j + ":" + "[" + theBoard.playerMisses[j].Across + " " + theBoard.playerMisses[j].Down + "]");
            }
            return theBoard;
        }
        if (hit == 1) {
            int playerhits_length = theBoard.playerHits.length;     //gets length of playermisses
            Start[] newplayerhits = new Start[playerhits_length + 1];
            for (int i = 0; i < playerhits_length; i++) {
                newplayerhits[i] = new Start();
                newplayerhits[i] = theBoard.playerHits[i];
            }
            newplayerhits[playerhits_length] = new Start();
            newplayerhits[playerhits_length].setStart(x, y);
            theBoard.playerHits = newplayerhits;
            System.out.println("playerHits array:");
            for (int j = 0; j < (playerhits_length + 1); j++) {
                System.out.println(j + ":" + "[" + theBoard.playerHits[j].Across + " " + theBoard.playerHits[j].Down + "]");

            }
            return theBoard;
        }
        return theBoard;
    }

    //Similar to placeShip, but with firing.
    private static String fireAt(Request req) {
        BattleshipModel theBoard = getModelFromReq(req);
        int fire_row = Integer.parseInt(req.params(":row"));
        int fire_col = Integer.parseInt(req.params(":col"));
        int fire_hit = 0;  //fire_hit

        fire_hit += check_ship_for_hit(theBoard, theBoard.aircraftCarrier, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.cruiser, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.battleship, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.destroyer, fire_row, fire_col);
        fire_hit += check_ship_for_hit(theBoard, theBoard.submarine, fire_row, fire_col);

        System.out.println("Fire Hit");
        System.out.println(fire_hit);
        theBoard = update_Player_Miss(fire_hit, theBoard, fire_row, fire_col);

        Gson gson = new Gson();
        //puts gson obj in string for return to GET
        String temp = gson.toJson(theBoard);
        return temp;


        //return "Error";
    }
}
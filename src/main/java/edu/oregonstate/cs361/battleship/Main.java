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
        //Makes a gson obj to take in java objects
        Gson retobj = new Gson();
        //puts gson obj in string for return to GET
        String retstring = retobj.toJson(theBoard);
        return retstring;
    }

    //This function should accept an HTTP request and deserialize it into an actual Java object.
    private static BattleshipModel getModelFromReq(Request req){
        Gson gson = new Gson();                                                                //creates a new Gson class variable
        BattleshipModel battleshipmodel = gson.fromJson(req.body(), BattleshipModel.class);    //parses game model from json to a java object
        return battleshipmodel;
    }

   private static String placeShip(Request req) {
        String shiptype = req.params(":id");
        int row = Integer.parseInt(req.params(":row"));
        int col = Integer.parseInt(req.params(":col"));
        String ore = req.params(":orientation");

        BattleshipModel mine = getModelFromReq(req);
        System.out.println(shiptype);
        System.out.println(ore);
        // test
        return "1";
    }

    //Similar to placeShip, but with firing.
    private static String fireAt(Request req) {
        BattleshipModel theBoard = getModelFromReq( req );
        int fire_row = Integer.parseInt(req.params(":row"));
        int fire_col = Integer.parseInt(req.params(":col"));

        Start fire_coord = new Start();                 //Creating a pair to insert into playerhits/playermisses
        fire_coord.setStart(fire_row, fire_col);        //Sets across and down in fire_coord
        int playermisses_length = theBoard.playerMisses.length;     //gets length of playermisses
        //theBoard.playerMisses[4].setStart(fire_row, fire_col);    tries to set playermisses, creates error
        System.out.println("Player Misses Length is");
        System.out.println(playermisses_length);

        System.out.println("Fired at row");
        System.out.println(fire_row);
        System.out.println("column");
        System.out.println(fire_col);

        return "1";
    }

}
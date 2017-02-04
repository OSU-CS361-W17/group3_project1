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

    //Similar to placeShip, but with firing.
    protected static String fireAt(Request req) {
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
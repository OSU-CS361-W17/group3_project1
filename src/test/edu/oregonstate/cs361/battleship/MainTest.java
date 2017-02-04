package edu.oregonstate.cs361.battleship;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static spark.Spark.awaitInitialization;


/**
 * Created by michaelhilton on 1/26/17.
 */
class MainTest {

    @BeforeAll
    public static void beforeClass() {
        Main.main(null);
        awaitInitialization();
    }

    @AfterAll
    public static void afterClass() {
        Spark.stop();
    }

    @Test
    public void testNewModel() { //tests newModel() and therein Sean's user story, which revolves around the user getting a board at start of game
        String fctString = Main.newModel();
        Gson gson = new Gson();
        BattleshipModel theBoard = new BattleshipModel();
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
        String boardString = gson.toJson(theBoard);
        assertEquals(fctString, boardString);
    }
    @Test
    public void testOccSquares(){
        Start[] testArr = new Start[16];
        Start[] boardArr = new Start[16];
        for(int a = 0; a<16; a++){
            testArr[a] = new Start();
            boardArr[a] = new Start();
            testArr[a].setStart(0,0);
        }
        String boardString = Main.newModel();
        Gson gson = new Gson();
        BattleshipModel theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.aircraftCarrier.start.setStart(1,1);
        theBoard.aircraftCarrier.end.setEnd(6, 1);
        boardArr = theBoard.occupiedSquares(theBoard, "player");
        //tests for {Across: 0 Down: 0} 16 times, as should be since player ships all get constructed with start and end having {Across: 0 Down: 0}
        for(int a = 0; a<16; a++){
            if(a<5){
                //checks for precise correctness of coordinates of one ship, makes sure the rest (which are still unplaced) generate 0's
                System.out.println(boardArr[a].Across);
                assertEquals(boardArr[a].Across, (a+1));
            } else {
                assertEquals(testArr[a].Across, boardArr[a].Across);
                assertEquals(testArr[a].Down, boardArr[a].Down);
            }
        }
        //tests that occupiedSquares() only returns {Across: 0 Down: 0}'s when it actually should.
        boardArr = theBoard.occupiedSquares(theBoard, "computer");
        for(int a = 0; a<16; a++){
            assertTrue(testArr[a].Across < boardArr[a].Across);
            assertTrue(testArr[a].Down < boardArr[a].Down);
        }
    }

    /*
    @Test
    public void testFireAt(){
        TestResponse res = request("POST", "/fire/1/1");

        String boardString = Main.newModel();
        Gson gson = new Gson();
        BattleshipModel Board = gson.fromJson(boardString, BattleshipModel.class);
        Board.playerMisses[0].Across= 1;
        Board.playerMisses[0].Down = 1;
        boardString = gson.toJson(Board);
        assertEquals(boardString, res.body);
    }
    */

    @Test
    public void TestgetModelFromReq() {
        TestResponse test = request("POST", "/placeShip/aircraftCarrier/2/2/horizontal");
        String boardString = Main.newModel();
        Gson gson = new Gson();
        BattleshipModel board = gson.fromJson(boardString, BattleshipModel.class);
        board.aircraftCarrier.start.Across = 2;
        board.aircraftCarrier.start.Down = 2;
        board.aircraftCarrier.end.Across = 7;
        board.aircraftCarrier.end.Down = 2;
        boardString = gson.toJson(board);
        assertEquals(boardString,test.body);
    }


    @Test
    public void testPlaceShip() {
        TestResponse res = request("POST", "/placeShip/aircraftCarrier/1/1/horizontal");

        String boardString = Main.newModel();
        Gson gson = new Gson();
        BattleshipModel theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.aircraftCarrier.start.Across = 1;
        theBoard.aircraftCarrier.start.Down = 1;
        theBoard.aircraftCarrier.end.Across = 6;
        theBoard.aircraftCarrier.end.Down = 1;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res.body);

        TestResponse res2 = request("POST", "/placeShip/aircraftCarrier/1/1/vertical");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.aircraftCarrier.start.Across = 1;
        theBoard.aircraftCarrier.start.Down = 1;
        theBoard.aircraftCarrier.end.Across = 1;
        theBoard.aircraftCarrier.end.Down = 6;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res2.body);

        TestResponse res3 = request("POST", "/placeShip/submarine/1/1/vertical");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.submarine.start.Across = 1;
        theBoard.submarine.start.Down = 1;
        theBoard.submarine.end.Across = 1;
        theBoard.submarine.end.Down = 3;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res3.body);

        TestResponse res4 = request("POST", "/placeShip/submarine/1/1/horizontal");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.submarine.start.Across = 1;
        theBoard.submarine.start.Down = 1;
        theBoard.submarine.end.Across = 3;
        theBoard.submarine.end.Down = 1;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res4.body);

        TestResponse res5 = request("POST", "/placeShip/battleship/1/1/vertical");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.battleship.start.Across = 1;
        theBoard.battleship.start.Down = 1;
        theBoard.battleship.end.Across = 1;
        theBoard.battleship.end.Down = 5;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res5.body);

        TestResponse res6 = request("POST", "/placeShip/battleship/1/1/horizontal");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.battleship.start.Across = 1;
        theBoard.battleship.start.Down = 1;
        theBoard.battleship.end.Across = 5;
        theBoard.battleship.end.Down = 1;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res6.body);

        TestResponse res7 = request("POST", "/placeShip/destroyer/1/1/vertical");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.destroyer.start.Across = 1;
        theBoard.destroyer.start.Down = 1;
        theBoard.destroyer.end.Across = 1;
        theBoard.destroyer.end.Down = 3;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res7.body);

        TestResponse res8 = request("POST", "/placeShip/destroyer/1/1/horizontal");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.destroyer.start.Across = 1;
        theBoard.destroyer.start.Down = 1;
        theBoard.destroyer.end.Across = 3;
        theBoard.destroyer.end.Down = 1;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res8.body);

        TestResponse res9 = request("POST", "/placeShip/cruiser/1/1/vertical");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.cruiser.start.Across = 1;
        theBoard.cruiser.start.Down = 1;
        theBoard.cruiser.end.Across = 1;
        theBoard.cruiser.end.Down = 4;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res9.body);

        TestResponse res10 = request("POST", "/placeShip/cruiser/1/1/horizontal");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.cruiser.start.Across = 1;
        theBoard.cruiser.start.Down = 1;
        theBoard.cruiser.end.Across = 4;
        theBoard.cruiser.end.Down = 1;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res10.body);

        /*TestResponse res11 = request("POST", "/placeShip/battleship/10/10/vertical");

        boardString = Main.newModel();
        theBoard = gson.fromJson(boardString, BattleshipModel.class);
        theBoard.cruiser.start.Across = 10;
        theBoard.cruiser.start.Down = 10;
        theBoard.cruiser.end.Across = 7;
        theBoard.cruiser.end.Down = 10;
        boardString = gson.toJson(theBoard);
        //assertEquals(200, res.status);
        assertEquals(boardString,res11.body);*/
    }

    private TestResponse request(String method, String path) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            String boardString = Main.newModel();
            Gson gson = new Gson();
            BattleshipModel theBoard = gson.fromJson(boardString, BattleshipModel.class);
            connection.setDoOutput(true);
            if(theBoard != null) { connection.setDoInput(true);

                java.io.OutputStream os =  connection.getOutputStream();

                byte [] outputInBytes = boardString.getBytes("UTF-8");

                os.write(outputInBytes);

            }
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Map<String,String> json() {
            return new Gson().fromJson(body, HashMap.class);
        }
    }


}
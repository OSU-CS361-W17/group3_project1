package edu.oregonstate.cs361.battleship;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    public void testGetModel() {
        //BattleshipModel mytest = newModel();
        //TestResponse res = request("GET", "/model");
        //BattleshipModel theshould = {"aircraftCarrier":{"name":"AircraftCarrier","length":5,"start":{"Across":0,"Down":0},"end":{"Across":0,"Down":0}},"battleship":{"name":"Battleship","length":4,"start":{"Across":0,"Down":0},"end":{"Across":0,"Down":0}},"cruiser":{"name":"Cruiser","length":3,"start":{"Across":0,"Down":0},"end":{"Across":0,"Down":0}},"destroyer":{"name":"Destroyer","length":2,"start":{"Across":0,"Down":0},"end":{"Across":0,"Down":0}},"submarine":{"name":"Submarine","length":2,"start":{"Across":0,"Down":0},"end":{"Across":0,"Down":0}},"computer_aircraftCarrier":{"name":"Computer_AircraftCarrier","length":5,"start":{"Across":2,"Down":2},"end":{"Across":2,"Down":7}},"computer_battleship":{"name":"Computer_Battleship","length":4,"start":{"Across":2,"Down":8},"end":{"Across":6,"Down":8}},"computer_cruiser":{"name":"Computer_Cruiser","length":3,"start":{"Across":4,"Down":1},"end":{"Across":4,"Down":4}},"computer_destroyer":{"name":"Computer_Destroyer","length":2,"start":{"Across":7,"Down":3},"end":{"Across":7,"Down":5}},"computer_submarine":{"name":"Computer_Submarine","length":2,"start":{"Across":9,"Down":6},"end":{"Across":9,"Down":8}},"playerHits":[],"playerMisses":[],"computerHits":[],"computerMisses":[]};
        //assertEquals(200, res.status);
        //assertEquals(theshould,res.body);
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
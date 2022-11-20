package org.udger.parser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.udger.parser.UdgerParser.ParserDbData;

public class UdgerPerformanceTest {

    private static JsonArray jsonArray;
    private static UdgerParser[] POOL;

    private static void createPool() {
        POOL = new UdgerParser[10];
        for (int i=0; i<=9; i++) {
            POOL[i] = new UdgerParser(new UdgerParser.ParserDbData("udgerdb_v4_" + i + ".dat"));
        }
    }

    private static void closePool() throws IOException {
        for (int i=0; i<=9; i++) {
            if (POOL[i] != null) {
                POOL[i].close();
            }
        }
    }

    public static void main(String args[]) {
        InputStream is = UdgerUaTest.class.getResourceAsStream("test_ua.json");
        JsonReader jsonReader = javax.json.Json.createReader(is);
        jsonArray = jsonReader.readArray();
        UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData("udgerdb_v4.dat");
        for (int i=0; i<10; i++) {
            System.out.println("### Test : " + (i+1));
            testSerial(parserDbData);
        }
    }

    private static void testSerial(ParserDbData parserDbData) {
        UdgerParser up = null;
        try {
            up = new UdgerParser(parserDbData);
            long tm = 0;
            for (int j=0; j<100; j++) {
                for (int i=0; i < jsonArray.size(); i++) {
                    JsonObject jar = jsonArray.getJsonObject(i);
                    String query = jar.getJsonObject("test").getString("teststring");
                    try {
                        long prev = System.nanoTime();
                        UdgerUaResult ret = up.parseUa(query);
                        tm += System.nanoTime() - prev;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            long numQueries = 100 * jsonArray.size();
            System.out.println("TOTAL Queries: " + numQueries + " time : " + tm / 1000000 + "ms AVG : " + 1000000000 * numQueries / (float) tm + "/s");
        } finally {
            if (up != null) {
                try {
                    up.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void testParallel() {
        try {
            createPool();
            long tm = 0;
            System.out.println("TOTAL Queries: " + 100 * jsonArray.size() + " time : " + tm + " AVG : " + 1000 * 100 * jsonArray.size() / (float) tm + "/s");
        } finally {
                try {
                    closePool();
                } catch (IOException e) {
                }
        }
    }

}

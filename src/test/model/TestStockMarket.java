package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestStockMarket {
    private static final String S1_TICKER = "testS1";
    private static final String S1_NAME = "test stock 1";
    private static final int S1_PRICE = 10;
    private static final double S1_GROWTH = 1.2;

    private static final String S2_TICKER = "testS2";
    private static final String S2_NAME = "test stock 2";
    private static final int S2_PRICE = 50;
    private static final double S2_GROWTH = 1.3;

    private StockMarket testSM;
    Stock testS1 = new Stock(S1_TICKER, S1_NAME, S1_PRICE, S1_GROWTH);
    Stock testS2 = new Stock(S2_TICKER, S2_NAME, S2_PRICE, S2_GROWTH);

    @BeforeEach
    public void setup() {
        testSM = new StockMarket();
    }

    @Test
    public void testGetMarket() {
        // Show get market returns the ArrayList<stock>
        ArrayList<Stock> testMarket = testSM.getMarket();
        assertTrue(testMarket.isEmpty());

        testSM.addStock(testS1);
        assertFalse(testMarket.isEmpty());
        assertEquals(1, testMarket.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(testSM.isEmpty());

        testSM.addStock(testS1);
        assertFalse(testSM.isEmpty());

        testSM.addStock(testS2);
        assertFalse(testSM.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, testSM.size());

        testSM.addStock(testS1);
        assertEquals(1, testSM.size());

        testSM.addStock(testS2);
        assertEquals(2, testSM.size());
    }

    @Test
    public void testAddStock() {
        assertNull(testSM.getStock(S1_TICKER));

        testSM.addStock(testS1);
        assertEquals(1, testSM.size());

        assertEquals(testS1, testSM.getStock(S1_TICKER));

        testSM.addStock(testS2);
        assertEquals(2, testSM.size());
        assertEquals(testS1, testSM.getStock(S1_TICKER));
        assertEquals(testS2, testSM.getStock(S2_TICKER));
    }

    @Test
    public void testGetStock() {
        assertNull(testSM.getStock(S1_TICKER));
        assertNull(testSM.getStock(S2_TICKER));

        testSM.addStock(testS1);
        testSM.addStock(testS2);
        assertEquals(testS1, testSM.getStock(S1_TICKER));
        assertEquals(testS2, testSM.getStock(S2_TICKER));
    }

    @Test
    public void testUpdateAllPrices() {
        testSM.addStock(testS1);
        testSM.addStock(testS2);

        int priceBid1 = testS1.getBid();
        int priceAsk1 = testS1.getAsk();
        int priceBid2 = testS1.getBid();
        int priceAsk2 = testS1.getAsk();

        testSM.updateAllPrices();
        assertNotEquals(testS1.getBid(), priceBid1);
        assertNotEquals(testS1.getAsk(), priceAsk1);
        assertNotEquals(testS2.getBid(), priceBid2);
        assertNotEquals(testS2.getAsk(), priceAsk2);
    }

    @Test
    public void testToJson(){
        // Test with 1 stock
        testSM.addStock(testS1);
        JSONObject testJson = testSM.toJson();
        assertFalse(testJson.getJSONArray("allStocks").isEmpty());

        JSONObject testJsonElement = testJson.getJSONArray("allStocks").getJSONObject(0);
        assertEquals(testJsonElement.getString("ticker"),S1_TICKER);
        assertEquals(testJsonElement.getString("name"),S1_NAME);
        assertEquals(testJsonElement.getInt("bidPrice"),S1_PRICE);
        assertEquals(testJsonElement.getDouble("growth"),S1_GROWTH);
    }

    @Test
    public void testAllStocksToJson(){
        testSM.addStock(testS1);

        JSONArray testJson = testSM.allStocksToJson();
        assertFalse(testJson.isEmpty());
        JSONObject testJsonElement = testJson.getJSONObject(0);
        assertEquals(testJsonElement.getString("ticker"),S1_TICKER);
        assertEquals(testJsonElement.getString("name"),S1_NAME);
        assertEquals(testJsonElement.getInt("bidPrice"),S1_PRICE);
        assertEquals(testJsonElement.getDouble("growth"),S1_GROWTH);
    }

}
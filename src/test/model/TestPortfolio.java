package model;

import exceptions.InsufficientQuantityException;
import exceptions.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPortfolio {
    private static final int CASH = 100;
    private Portfolio testP;

    @BeforeEach
    public void setup() {
        testP = new Portfolio(CASH);
    }

    @Test
    public void testConstructor() {
        assertEquals(CASH, testP.getCash());
        assertTrue(testP.getHoldings().isEmpty());
        assertEquals(0, testP.getHoldings().size());
    }

    @Test
    public void testGetCash() {
        assertEquals(CASH, testP.getCash());
    }

    @Test
    public void testGetHoldings() {
        //Test Empty
        assertTrue(testP.getHoldings().isEmpty());
        assertEquals(0, testP.getHoldings().size());
        //Add a holding
        try {
            assertTrue(testP.addStock("AAA", 10, 10));
        } catch (InvalidInputException e) {
            fail();
        }
        assertFalse(testP.getHoldings().isEmpty());
        assertEquals(1, testP.getHoldings().size());
    }

    @Test
    public void testIsCashSufficient() {
        assertTrue(testP.isCashSufficient(50));
        assertTrue(testP.isCashSufficient(99));
        assertTrue(testP.isCashSufficient(100));
        assertFalse(testP.isCashSufficient(101));
        assertFalse(testP.isCashSufficient(250));
    }

    @Test
    public void testDeductCash() {
        int currentCash = CASH;

        testP.deductCash(1);
        currentCash -= 1;
        assertEquals(currentCash, testP.getCash());

        testP.deductCash(1);
        currentCash -= 1;
        assertEquals(currentCash, testP.getCash());

        testP.deductCash(50);
        currentCash -= 50;
        assertEquals(currentCash, testP.getCash());
    }

    @Test
    public void testAddStockInsufficientCash() {
        try {
            assertFalse(testP.addStock("AAA", 10, 100));
            assertFalse(testP.addStock("AAA", 101, 1));
        } catch (InvalidInputException e) {
            fail();
        }
    }

    @Test
    public void testAddStockNew() {
        int currentCash = CASH;
        // Stock 1
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;
        int cost = price * quantity;
        // Stock 2
        String ticker2 = "BBB";
        int price2 = 12;
        int quantity2 = 5;
        int cost2 = price2 * quantity2;

        // Add 2 different stocks
        try {
            assertTrue(testP.addStock(ticker, quantity, price));
            assertTrue(testP.addStock(ticker2, quantity2, price2));
        } catch (InvalidInputException e) {
            fail();
        }
        // Test cash
        currentCash = currentCash - cost - cost2;
        assertEquals(currentCash, testP.getCash());

        // Test stock 1
        Holding testH = testP.getHolding(ticker);
        assertEquals(ticker, testH.getStockTicker());
        assertEquals(price, testH.getBuyPrice());
        assertEquals(quantity, testH.getQuantity());

        // Test stock 2
        Holding testH2 = testP.getHolding(ticker2);
        assertEquals(ticker2, testH2.getStockTicker());
        assertEquals(price2, testH2.getBuyPrice());
        assertEquals(quantity2, testH2.getQuantity());
    }

    @Test
    public void testAddStockExisting() {
        try {
            // First buy
            String ticker = "AAA";
            int price = 3;
            int quantity = 5;
            int cost = price * quantity;
            int currentCash = CASH;

            testP.addStock(ticker, quantity, price);
            currentCash -= cost;

            Holding testH = testP.getHolding(ticker);

            //Second buy
            int price2 = 10;
            int quantity2 = 5;
            int cost2 = price2 * quantity2;
            currentCash -= cost2;
            int newQuantity = quantity + quantity2;
            int newPrice = (cost + cost2) / newQuantity;

            testP.addStock(ticker, quantity2, price2);
            assertEquals(currentCash, testP.getCash());
            assertNotNull(testH);
            assertEquals(ticker, testH.getStockTicker());
            assertEquals(newPrice, testH.getBuyPrice());
            assertEquals(newQuantity, testH.getQuantity());
        } catch (InvalidInputException e) {
            fail();
        }

    }

    @Test
    public void testAddStockInvalidInputException() {
        String ticker = "AAA";

        //Invalid Price
        int price = -3;
        int quantity = 5;
        try{
            testP.addStock(ticker, quantity, price);
            fail();
        } catch (InvalidInputException e) {
            //Expected
        }

        //Invalid Quantity
        price = 3;
        quantity = -5;
        try{
            testP.addStock(ticker, quantity, price);
            fail();
        } catch (InvalidInputException e) {
            //Expected
        }
    }

    @Test
    public void testSellStock() {
        int testPrice;
        int testQuantity;

        String ticker = "AAA";
        int initPrice = 3;
        int initQuantity = 5;
        int cash = CASH;


        // Test sell 1
        testPrice = 1;
        testQuantity = 2;
        try {
            testP.addStock(ticker, initQuantity, initPrice);
            cash -= initPrice * initQuantity;
            testP.sellStock(ticker, testQuantity, testPrice);
        } catch (InvalidInputException e) {
            fail("Invalid quantity or price input");
        } catch (InsufficientQuantityException e) {
            fail("Insufficient holding in portfolio");
        }
        cash += testPrice * testQuantity;
        assertEquals(cash, testP.getCash());
        assertEquals(3, testP.getHolding(ticker).getQuantity());

        // Test sell all
        testPrice = 1;
        testQuantity = 3;
        try {
            testP.sellStock(ticker, testQuantity, testPrice);
        } catch (InvalidInputException e) {
            fail("Invalid quantity or price input");
        } catch (InsufficientQuantityException e) {
            fail("Insufficient holding in portfolio");
        }
        cash += testPrice * testQuantity;
        assertEquals(cash, testP.getCash());
        assertTrue(testP.getHoldings().isEmpty());
    }

    @Test
    public void testInsufficientQuantityException() {
        int testPrice;
        int testQuantity;

        String ticker = "AAA";
        int initPrice = 3;
        int initQuantity = 1;
        int cash = CASH;

        // Test sell 1
        testPrice = 1;
        testQuantity = 2;
        try {
            testP.addStock(ticker, initQuantity, initPrice);
            cash -= initPrice * initQuantity;
            testP.sellStock(ticker, testQuantity, testPrice);
            fail();
        } catch (InvalidInputException e) {
            fail();
        } catch (InsufficientQuantityException e) {
            // Expected
        }
        assertEquals(cash, testP.getCash());
        assertEquals(initQuantity, testP.getHolding(ticker).getQuantity());
    }

    @Test
    public void testInvalidInputQuantityException() {
        int testPrice;
        int testQuantity;

        String ticker = "AAA";
        int initPrice = 3;
        int initQuantity = 1;
        int cash = CASH;

        // Test sell 0
        testPrice = 1;
        testQuantity = 0;
        try {
            testP.addStock(ticker, initQuantity, initPrice);
            cash -= initPrice * initQuantity;
            testP.sellStock(ticker, testQuantity, testPrice);
            fail();
        } catch (InvalidInputException e) {
            // Expected
        } catch (InsufficientQuantityException e) {
            fail();
        }
        assertEquals(cash, testP.getCash());
        assertEquals(initQuantity, testP.getHolding(ticker).getQuantity());
    }

    @Test
    public void testInvalidInputPriceException() {
        int testPrice;
        int testQuantity;

        String ticker = "AAA";
        int initPrice = 3;
        int initQuantity = 1;
        int cash = CASH;

        // Test sell 0
        testPrice = 0;
        testQuantity = 1;
        try {
            testP.addStock(ticker, initQuantity, initPrice);
            cash -= initPrice * initQuantity;
            testP.sellStock(ticker, testQuantity, testPrice);
            fail();
        } catch (InvalidInputException e) {
            // Expected
        } catch (InsufficientQuantityException e) {
            fail();
        }
        assertEquals(cash, testP.getCash());
        assertEquals(initQuantity, testP.getHolding(ticker).getQuantity());
    }

    @Test
    public void testRemoveFromPortfolio() {
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;

        try {
            testP.addStock(ticker, quantity, price);
            assertNotNull(testP.getHolding(ticker));

            // Test it is removed
            Holding testHolding = testP.getHolding(ticker);
            testP.removeFromPortfolio(testHolding);
            assertNull(testP.getHolding(ticker));
        } catch (InvalidInputException e) {
            fail();
        }
    }

    @Test
    public void testIsQuantitySufficient() {
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;
        try {
            // Test empty
            assertFalse(testP.isQuantitySufficient("dummy", 1));

            // True case
            testP.addStock(ticker, quantity, price);
            assertTrue(testP.isQuantitySufficient(ticker, quantity - 3));
            assertTrue(testP.isQuantitySufficient(ticker, quantity - 1));
            assertTrue(testP.isQuantitySufficient(ticker, quantity));

            // False case
            assertFalse(testP.isQuantitySufficient(ticker, quantity + 1));
            assertFalse(testP.isQuantitySufficient(ticker, quantity + 3));
        } catch (InvalidInputException e) {
            fail();
        }
    }

    @Test
    public void testSetCash() {
        testP.setCash(999);
        assertEquals(testP.getCash(), 999);
    }

    @Test
    public void testToJson() {
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;
        try {
            testP.addStock(ticker, quantity, price);
            JSONObject testJson = testP.toJson();
            assertEquals(testJson.getInt("cash"), CASH - price * quantity);

            JSONObject testJsonElement = testJson.getJSONArray("holdings").getJSONObject(0);
            assertEquals(testJsonElement.getString("stockTicker"), ticker);
            assertEquals(testJsonElement.getInt("quantity"), quantity);
            assertEquals(testJsonElement.getInt("buyPrice"), price);
        } catch (InvalidInputException e) {
            fail();
        }
    }

    @Test
    public void testHoldingsToJson() {
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;
        try {
            testP.addStock(ticker, quantity, price);

            JSONArray testJson = testP.holdingsToJson();
            assertFalse(testJson.isEmpty());
            JSONObject testJsonElement = testJson.getJSONObject(0);
            assertEquals(testJsonElement.getString("stockTicker"), ticker);
            assertEquals(testJsonElement.getInt("quantity"), quantity);
            assertEquals(testJsonElement.getInt("buyPrice"), price);
        } catch (InvalidInputException e) {
            fail();
        }
    }
}


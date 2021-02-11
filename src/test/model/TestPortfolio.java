package model;

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
        assertTrue(testP.addStock("AAA", 10, 10));

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
        assertFalse(testP.addStock("AAA", 10, 100));
        assertFalse(testP.addStock("AAA", 101, 1));
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
        assertTrue(testP.addStock(ticker, quantity, price));
        assertTrue(testP.addStock(ticker2, quantity2, price2));

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
    }

    @Test
    public void testSellStock() {
        int testPrice;
        int testQuantity;

        String ticker = "AAA";
        int initPrice = 3;
        int initQuantity = 5;
        int cash = CASH;

        // Setup, add a holding
        testP.addStock(ticker, initQuantity, initPrice);
        cash -= initPrice * initQuantity;

        // Test sell 1
        testPrice = 1;
        testQuantity = 2;
        testP.sellStock(ticker, testQuantity, testPrice);
        cash += testPrice * testQuantity;
        assertEquals(cash, testP.getCash());
        assertEquals(3, testP.getHolding(ticker).getQuantity());

        // Test sell all
        testPrice = 1;
        testQuantity = 3;
        testP.sellStock(ticker, testQuantity, testPrice);
        cash += testPrice * testQuantity;
        assertEquals(cash, testP.getCash());
        assertTrue(testP.getHoldings().isEmpty());
    }

    @Test
    public void testRemoveFromPortfolio() {
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;

        // Setup, add a holding
        testP.addStock(ticker, quantity, price);
        assertNotNull(testP.getHolding(ticker));

        // Test it is removed
        Holding testHolding = testP.getHolding(ticker);
        testP.removeFromPortfolio(testHolding);
        assertNull(testP.getHolding(ticker));
    }

    @Test
    public void testIsQuantitySufficient(){
        String ticker = "AAA";
        int price = 3;
        int quantity = 5;

        // Test empty
        assertFalse(testP.isQuantitySufficient("dummy", 1));

        // True case
        testP.addStock(ticker, quantity, price);
        assertTrue(testP.isQuantitySufficient(ticker, quantity -3));
        assertTrue(testP.isQuantitySufficient(ticker, quantity -1));
        assertTrue(testP.isQuantitySufficient(ticker, quantity));

        // False case
        assertFalse(testP.isQuantitySufficient(ticker, quantity+1));
        assertFalse(testP.isQuantitySufficient(ticker, quantity+3));
    }
}


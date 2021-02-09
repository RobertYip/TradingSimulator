package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void setup() {
        testSM = new StockMarket();
    }

    @Test
    void testIsEmpty() {
        assertTrue(testSM.isEmpty());

        testSM.addStock(testS1);
        assertFalse(testSM.isEmpty());

        testSM.addStock(testS2);
        assertFalse(testSM.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, testSM.size());

        testSM.addStock(testS1);
        assertEquals(1, testSM.size());

        testSM.addStock(testS2);
        assertEquals(2, testSM.size());
    }

    @Test
    void testAddStock() {
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
    void testupdateAllPrices() {

    }


}

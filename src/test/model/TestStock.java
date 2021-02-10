package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStock {
    private static final String TEST_TICKER = "AAA";
    private static final String TEST_NAME = "AAA Inc.";
    private static final int TEST_PRICE = 10;
    private static final double TEST_GROWTH = 1.2;
    private Stock testStock;

    @BeforeEach
    public void setup() {
        testStock = new Stock(TEST_TICKER, TEST_NAME, TEST_PRICE, TEST_GROWTH);
    }

    @Test
    public void testConstructor() {
        assertEquals(TEST_TICKER, testStock.getTicker());
        assertEquals(TEST_NAME, testStock.getName());
        assertEquals(TEST_PRICE, testStock.getBid());
        assertEquals(TEST_GROWTH, testStock.getGrowth());
    }

    @Test
    public void testGetters() {
        assertEquals(TEST_TICKER, testStock.getTicker());
        assertEquals(TEST_NAME, testStock.getName());
        assertEquals(TEST_PRICE, testStock.getBid());
        assertEquals(TEST_PRICE+testStock.getRandomNumber(),testStock.getAsk());
        assertEquals(TEST_GROWTH, testStock.getGrowth());
    }

    @Test
    public void testCalcGrowth() {
        int testResult = (int) (TEST_PRICE * TEST_GROWTH);

        assertEquals(TEST_PRICE, testStock.getBid());
        testStock.applyGrowth();

        assertEquals(testResult, testStock.getBid());
    }

    @Test
    public void testCalcRandomNumber() {
        int minRange = 0;
        int maxRange = 10;  //Based on SPREAD
        int testNumber;
        // Test random number is between 0 to 10 through loop
        for (int i = 0; i < 100; i++){
            testNumber = testStock.getRandomNumber();
            assertTrue(minRange <= testNumber && testNumber <= maxRange);
        }
    }

    @Test
    public void testCalcSpread() {

    }
}


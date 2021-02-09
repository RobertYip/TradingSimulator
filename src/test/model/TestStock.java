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
    void setup() {
        testStock = new Stock(TEST_TICKER, TEST_NAME, TEST_PRICE, TEST_GROWTH);
    }

    @Test
    void testGetters(){
        assertEquals(TEST_TICKER,testStock.getTicker());
        assertEquals(TEST_NAME,testStock.getName());
        assertEquals(TEST_PRICE,testStock.getBid());
        //assertEquals(TEST_TICKER,testStock.getAsk());
        assertEquals(TEST_GROWTH,testStock.getGrowth());
    }
    @Test
    void testConstructor() {
        assertEquals(TEST_TICKER, testStock.getTicker());
        assertEquals(TEST_NAME, testStock.getName());
        //assertEquals(TEST_PRICE, testStock.getBid()); calc new daily added random
        assertEquals(TEST_GROWTH, testStock.getGrowth());
    }

    @Test
    void testCalcGrowth() {
        int testResult = (int) (TEST_PRICE * TEST_GROWTH);

        assertEquals(TEST_PRICE, testStock.getBid());
        testStock.applyGrowth();

        assertEquals(testResult, testStock.getBid());
    }

    @Test
    void testCalcRandom(){

    }

    @Test
    void testCalcSpread(){

    }
}


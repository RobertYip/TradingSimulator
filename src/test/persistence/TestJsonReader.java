package persistence;

import model.StockMarket;
import model.Portfolio;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonReader {
    private static final String pSource = "./data/testPortfolio.json";
    private static final String smSource = "./data/testStockmarket.json";
    private static final String dSource = "./data/testDays.json";
    private static final String brokenSource = "./data/dne.json";

    private String testPortfolioTicker = "testP";
    private int testPortfolioCash = 50;
    private int testPortfolioQuantity = 1;
    private int testPortfolioPrice = 20;
    private String testSmTicker = "testSM";
    private int testSmPrice = 25;
    private int testDays = 3;

    @Test
    public void testReaderNonExistentPortfolio(){
        JsonReader reader = new JsonReader(brokenSource, smSource, dSource);
        try {
            Portfolio p = reader.readPortfolio();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
    @Test
    public void testReaderNonExistentStockMarket(){
        JsonReader reader = new JsonReader(pSource, brokenSource, dSource);
        try {
            StockMarket sm = reader.readStockMarket();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
    @Test
    public void testReaderNonExistentDaysSource(){
        JsonReader reader = new JsonReader(pSource, smSource, brokenSource);
        try {
            int d = reader.readDays();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralCase() {
        JsonReader reader = new JsonReader(pSource, smSource, dSource);

        try {
            Portfolio p = reader.readPortfolio();
            StockMarket sm = reader.readStockMarket();
            int d = reader.readDays();

            assertEquals(p.getCash(), testPortfolioCash-testPortfolioQuantity*testPortfolioPrice);
            assertEquals(p.getHoldings().size(),1);
            assertEquals(p.getHoldings().get(0).getStockTicker(),testPortfolioTicker);

            assertEquals(sm.getMarket().size(), 1);
            assertEquals(sm.getMarket().get(0).getTicker(), testSmTicker);
            assertEquals(d, testDays);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}

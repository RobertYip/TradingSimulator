package persistence;

import model.Stock;
import model.StockMarket;
import model.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class testJsonWriter {
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

    Portfolio p = new Portfolio(testPortfolioCash);
    Stock testStock = new Stock(testSmTicker, "test", testSmPrice, 1);
    StockMarket sm = new StockMarket();
    int days;


    @BeforeEach
    void setup() {

    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0illegal:fileName.json", smSource, dSource);
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterGeneralCase() {
        try {
            p.addStock(testPortfolioTicker, testPortfolioQuantity, testPortfolioPrice);
            sm.addStock(testStock);
            days = 3;

            JsonWriter writer = new JsonWriter(pSource, smSource, dSource);
            writer.open();
            writer.write(p, sm, days);
            writer.close();

            JsonReader reader = new JsonReader(pSource, smSource, dSource);
            Portfolio p2 = reader.readPortfolio();
            StockMarket sm2 = reader.readStockMarket();
            int days2 = reader.readDays();

            assertEquals(p.getCash(), p2.getCash());
            assertEquals(p.getHoldings().get(0).getStockTicker(), p2.getHoldings().get(0).getStockTicker());
            assertEquals(sm.getMarket().size(), sm2.getMarket().size());
            assertEquals(days, days2);


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}

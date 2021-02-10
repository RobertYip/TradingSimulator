package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestHolding {
    private static final String TEST_TICKER = "AAA";
    private static final int TEST_QUANTITY = 10;
    private static final int TEST_PRICE = 10;
    private Holding testH;

    @BeforeEach
    public void setup() {
        testH = new Holding(TEST_TICKER, TEST_QUANTITY, TEST_PRICE);
    }

    @Test
    public void testGetStockTicker() {
        assertEquals(TEST_TICKER, testH.getStockTicker());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(TEST_QUANTITY, testH.getQuantity());
    }

    @Test
    public void testGetBuyPrice() {
        assertEquals(TEST_PRICE, testH.getBuyPrice());
    }

    @Test
    public void testAddQuantity() {
        int currentQuantity = TEST_QUANTITY;

        testH.addQuantity(1);
        currentQuantity += 1;
        assertEquals(currentQuantity, testH.getQuantity());

        testH.addQuantity(1);
        currentQuantity += 1;
        assertEquals(currentQuantity, testH.getQuantity());

        testH.addQuantity(15);
        currentQuantity += 15;
        assertEquals(currentQuantity, testH.getQuantity());
    }

    @Test
    public void testRemoveQuantity() {
        int currentQuantity = TEST_QUANTITY;

        testH.removeQuantity(1);
        currentQuantity -= 1;
        assertEquals(currentQuantity, testH.getQuantity());

        testH.removeQuantity(5);
        currentQuantity -= 5;
        assertEquals(currentQuantity, testH.getQuantity());

        // Fail case
        testH.removeQuantity(10);
        assertEquals(currentQuantity, testH.getQuantity());

        // Zero case
        testH.removeQuantity(4);
        currentQuantity -= 4;
        assertEquals(currentQuantity, testH.getQuantity());
    }

    @Test
    public void testIsQuantitySufficient() {
        assertTrue(testH.isQuantitySufficient(TEST_QUANTITY));
        assertTrue(testH.isQuantitySufficient(TEST_QUANTITY - 1));
        assertFalse(testH.isQuantitySufficient(TEST_QUANTITY + 1));
    }

    @Test
    public void testUpdateHolding() {
        int currentPrice = TEST_PRICE;
        int currentQuantity = TEST_QUANTITY;
        int testBookValue;
        int addPrice;
        int addQuantity;
        int newBookValue;
        int newPrice;

        // 1st update
        testBookValue = currentPrice * currentQuantity;
        addPrice = 15;
        addQuantity = 10;
        newBookValue = testBookValue + addPrice * addQuantity;
        currentQuantity += addQuantity;
        newPrice = newBookValue / (currentQuantity);

        testH.updateHolding(addPrice, addQuantity);
        assertEquals(newPrice, testH.getBuyPrice());

        // 2nd update
        currentPrice = newPrice;
        testBookValue = currentPrice * currentQuantity;
        addPrice = 1;
        addQuantity = 1;
        newBookValue = testBookValue + addPrice * addQuantity;
        currentQuantity += addQuantity;
        newPrice = newBookValue / (currentQuantity);

        testH.updateHolding(addPrice, addQuantity);
        assertEquals(newPrice, testH.getBuyPrice());
    }
}

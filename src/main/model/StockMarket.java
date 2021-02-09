package model;

import java.util.ArrayList;

public class StockMarket {
    ArrayList<Stock> allStocks;

    // EFFECTS: StockMarket constructor, loads available stocks.
    public StockMarket() {
        allStocks = new ArrayList<>();
    }

    // EFFECTS: return true if stock market is empty
    public Boolean isEmpty() {
        return allStocks.isEmpty();
    }

    // EFFECTS: return number of stocks in stock market
    public int size() {
        return allStocks.size();
    }

    // MODIFIES: this
    // EFFECTS: add a stock to the stock market
    public void addStock(Stock stock) {
        allStocks.add(stock);
    }

    // EFFECTS: print out stock information
    public void displayStocks() {
        System.out.println("\nDisplaying list of stocks:");
        for (Stock s : allStocks) {
            System.out.printf("\t%-15s \t%-25s \t%-10s \t%-10s\n",
                    "Ticker: " + s.getTicker(),
                    "Name: " + s.getName(),
                    "Bid: " + s.getBid(),
                    "Ask: " + s.getAsk());
        }
    }

    // EFFECTS: returns the stock if the ticker is found in stockMarket, otherwise return null
    public Stock getStock(String ticker) {
        for (Stock s : allStocks) {
            if (s.getTicker().equals(ticker)) {
                return s;
            }
        }
        return null;
    }

    // MODIFIES: Stock
    // EFFECTS: update all stock prices from new day
    public void updateAllPrices() {
        for (Stock s : allStocks) {
            s.calcNewDaily();
        }
    }
}

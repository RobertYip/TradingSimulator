package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Stock market that holds a list of individual stocks
public class StockMarket implements Writable {
    private ArrayList<Stock> allStocks = new ArrayList<>();

    // EFFECTS: return the stock market ArrayList
    public ArrayList<Stock> getMarket() {
        return allStocks;
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
    // EFFECTS: update all stock prices to new day
    public void updateAllPrices() {
        for (Stock s : allStocks) {
            s.calcNewDaily();
        }
    }

    @Override
    // EFFECTS: create json file for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("allStocks", allStocksToJson());
        return json;
    }

    // EFFECTS: returns stock in stocks as a JSON array
    private JSONArray allStocksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Stock s : allStocks) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

}

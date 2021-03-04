package persistence;

import model.Portfolio;
import model.StockMarket;
import model.Stock;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

/*
 * Represents a reader that reads portfolio and stock market from JSON data stored in file
 * Citation: code obtained from JsonSerializationDemo
 * URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

public class JsonReader {
    private String portfolioSource;
    private String stockMarketSource;
    private String daysSource;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String portfolio, String stockMarket, String days) {
        this.portfolioSource = portfolio;
        this.stockMarketSource = stockMarket;
        this.daysSource = days;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: reads portfolio from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Portfolio readPortfolio() throws IOException {
        String jsonData = readFile(portfolioSource);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: parses Portfolio from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        int cash = jsonObject.getInt("cash");

        Portfolio portfolio = new Portfolio(cash);
        addHoldings(portfolio, jsonObject);
        portfolio.setCash(cash);
        return portfolio;
    }

    // MODIFIES: Portfolio
    // EFFECTS: parses holdings from JSON object and adds them to Portfolio
    private void addHoldings(Portfolio p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("holdings");
        for (Object json : jsonArray) {
            JSONObject nextHolding = (JSONObject) json;
            addHolding(p, nextHolding);
        }
    }

    // MODIFIES: Portfolio
    // EFFECTS: parses holding from JSON object and adds it to holdings
    private void addHolding(Portfolio p, JSONObject jsonObject) {
        String stockTicker = jsonObject.getString("stockTicker");
        int quantity = jsonObject.getInt("quantity");
        int price = jsonObject.getInt("buyPrice");
        p.addStock(stockTicker, quantity, price);
    }

    // EFFECTS: reads stock market from file and returns it;
    // throws IOException if an error occurs reading data from file
    public StockMarket readStockMarket() throws IOException {
        String jsonData = readFile(stockMarketSource);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseStockMarket(jsonObject);
    }

    // EFFECTS: parses stock market from JSON object and returns it
    private StockMarket parseStockMarket(JSONObject jsonObject) {
        StockMarket stockMarket = new StockMarket();
        addStocks(stockMarket, jsonObject);
        return stockMarket;
    }

    // MODIFIES: stockMarket
    // EFFECTS: parses allStocks from JSON object and adds them to stock market
    private void addStocks(StockMarket sm, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("allStocks");
        for (Object json : jsonArray) {
            JSONObject nextStock = (JSONObject) json;
            addStock(sm, nextStock);
        }
    }

    // MODIFIES: stockMarket
    // EFFECTS: parses stock from JSON object and adds it to allStocks
    private void addStock(StockMarket sm, JSONObject jsonObject) {
        String ticker = jsonObject.getString("ticker");
        String name = jsonObject.getString("name");
        int bidPrice = jsonObject.getInt("bidPrice");
        double growth = jsonObject.getDouble("growth");
        Stock s = new Stock(ticker, name, bidPrice, growth);
        sm.addStock(s);
    }

    // EFFECTS: reads days from file and returns it;
    // throws IOException if an error occurs reading data from file
    public int readDays() throws IOException {
        String jsonData = readFile(daysSource);
        JSONObject jsonObject = new JSONObject(jsonData);

        return jsonObject.getInt("days");
    }


}

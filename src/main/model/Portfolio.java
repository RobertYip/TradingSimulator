package model;

import java.util.ArrayList;

public class Portfolio {
    private int cash;
    private ArrayList<Holding> holdings;

    // REQUIRES: initialCash > 0
    // MODIFIES: this
    // EFFECTS: constructs a portfolio for the player with initialCash and empty holdings
    public Portfolio(int initialCash) {
        this.cash = initialCash;
        this.holdings = new ArrayList<>();
    }

    // EFFECTS: return cash
    public int getCash() {
        return cash;
    }

    // EFFECTS: prints portfolio: Cash and holdings
    public void displayPortfolio(StockMarket market) {
        int stockAtMarketPrice;
        int totalPortfolioValue;
        int totalHoldingsValue = 0;

        System.out.println("\nDisplaying portfolio:");
        System.out.println("\tCash: " + cash);

        for (Holding h : holdings) {
            stockAtMarketPrice = market.getStock(h.getStockTicker()).getBid();
            System.out.printf("\t%-15s \t%-20s \t%-20s \t%-25s\n",
                    "Stock: " + h.getStockTicker(),
                    "Quantity: " + h.getQuantity(),
                    "Buy Price: " + h.getBuyPrice(),
                    "Market Price: " + stockAtMarketPrice);
            totalHoldingsValue += stockAtMarketPrice * h.getQuantity();
        }

        totalPortfolioValue = cash + totalHoldingsValue;
        System.out.println("Total Portfolio value: " + totalPortfolioValue);
    }


    // REQUIRES: quantity > 0, price > 0
    // MODIFIES: this
    // EFFECTS: return false if not enough cash on hand.
    //          return true if sufficient cash
    public Boolean isCashSufficient(int cost) {
        return cash >= cost;
    }

    // REQUIRES: cost > 0, isCashSufficient is true
    // MODIFIES: this
    // EFFECTS: reduces cash by cost amount
    public void deductCash(int cost) {
        cash -= cost;
    }

    // REQUIRES: quantity > 0, price > 0
    // MODIFIES: this
    // EFFECTS: updates holding if exist in holdings, else add new holding to holdings
    public Boolean addStock(String ticker, int quantity, int price) {
        Holding foundHolding = getHolding(ticker);
        int cost = quantity * price;

        if (isCashSufficient(cost)) {
            deductCash(cost);

            if (foundHolding != null) {
                foundHolding.updateHolding(price, quantity);
            } else {
                holdings.add(new Holding(ticker, quantity, price));
            }
            return true;
        }
        return false;
    }

    // EFFECTS: returns the holding if the ticker is found in holdings
    public Holding getHolding(String ticker) {
        for (Holding h : holdings) {
            if (h.getStockTicker().equals(ticker)) {
                return h;
            }
        }
        return null;
    }

    // REQUIRES: quantity > 0, price > 0
    // MODIFIES: this
    // EFFECTS: return false if stock not in stocks portfolio.
    //          return true if cash is in portfolio and update quantities and cash
    public void sellStock(String ticker, int quantity, int price) {
        Holding selectedHolding = getHolding(ticker);

        cash += quantity * price;
        selectedHolding.removeQuantity(quantity);

        if (selectedHolding.getQuantity() == 0) {
            removeFromPortfolio(selectedHolding);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes holding from holdings
    public void removeFromPortfolio(Holding holding) {
        //int index = holdings.indexOf(holding);
        holdings.remove(holding);
    }

    // REQUIRES: quantity > 0
    // MODIFIES: this
    // EFFECTS: return true if sufficient quantity in portfolio to sell
    //          return false if stock not in portfolio or not enough quantity in portfolio
    public Boolean isQuantitySufficient(String ticker, int quantity) {
        Holding holding = getHolding(ticker);

        if (holding != null) {
            return holding.isQuantitySufficient(quantity);
        }
        System.out.println("Stock is not in portfolio");
        return false;
    }
}


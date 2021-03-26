package model;

import exceptions.InsufficientQuantityException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// A user's portfolio that holds a collection of stock holdings
public class Portfolio implements Writable {
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

    // EFFECTS: return holdings object
    public ArrayList<Holding> getHoldings() {
        return holdings;
    }


    // REQUIRES: quantity > 0, price > 0
    // MODIFIES: this
    // EFFECTS: return false if not enough cash on hand.
    //          return true if sufficient cash
    public boolean isCashSufficient(int cost) {
        return cash >= cost;
    }

    // MODIFIES: this
    // EFFECTS: sets cash to parameter
    public void setCash(int cash) {
        this.cash = cash;
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
    public boolean addStock(String ticker, int quantity, int price) {
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
    //          return null if not found
    public Holding getHolding(String ticker) {
        for (Holding h : holdings) {
            if (h.getStockTicker().equals(ticker)) {
                return h;
            }
        }
        return null;
    }

    // REQUIRES: quantity > 0 && quantity <= holdings quantity
    //           price > 0
    //           holding exist in holdings
    // MODIFIES: this
    // EFFECTS: remove quantity of holding from portfolio and update cash.
    //          if quantity of holdings = 0, remove it from holdings
    public void sellStock(String ticker, int quantity, int price) throws InsufficientQuantityException {
        if (!isQuantitySufficient(ticker, quantity)) {
            throw new InsufficientQuantityException();
        }
        Holding selectedHolding = getHolding(ticker);
        cash += quantity * price;
        selectedHolding.removeQuantity(quantity);

        if (selectedHolding.getQuantity() == 0) {
            removeFromPortfolio(selectedHolding);
        }
    }

    // REQUIRES: holding exists in portfolio
    // MODIFIES: this
    // EFFECTS: removes holding from holdings
    public void removeFromPortfolio(Holding holding) {
        holdings.remove(holding);
    }

    // REQUIRES: quantity > 0
    // MODIFIES: this
    // EFFECTS: return true if sufficient quantity in portfolio to sell
    //          return false if stock not in portfolio or not enough quantity in portfolio
    public boolean isQuantitySufficient(String ticker, int quantity) {
        Holding holding = getHolding(ticker);

        if (holding != null) {
            return holding.isQuantitySufficient(quantity);
        }
        return false;
    }

    @Override
    // EFFECTS: create json file for this
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("cash", cash);
        json.put("holdings", holdingsToJson());
        return json;
    }

    // EFFECTS: returns holdings as a JSON array
    public JSONArray holdingsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Holding h : holdings) {
            jsonArray.put(h.toJson());
        }

        return jsonArray;
    }
}


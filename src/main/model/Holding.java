package model;

// A single stock holding in the user's portfolio
public class Holding {
    private String stockTicker;
    private int quantity;
    private int buyPrice;

    // REQUIRES: quantity > 0, price > 0
    // EFFECTS: Construct a stock that the user holds, including shares owned and buy price
    public Holding(String ticker, int quantity, int price) {
        this.stockTicker = ticker;
        this.quantity = quantity;
        this.buyPrice = price;
    }

    // EFFECTS: returns stockTicker
    public String getStockTicker() {
        return stockTicker;
    }

    // EFFECTS: returns quantity
    public int getQuantity() {
        return quantity;
    }

    // EFFECTS: returns buyPrice
    public int getBuyPrice() {
        return buyPrice;
    }

    // REQUIRES: quantity > 0
    // MODIFIES: this
    // EFFECTS: adds quantity to this
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    // REQUIRES: quantity > 0
    // MODIFIES: this
    // EFFECTS: remove quantity from this if sufficient quantity
    public void removeQuantity(int quantity) {
        if (isQuantitySufficient(quantity)) {
            this.quantity -= quantity;
        }
    }

    // REQUIRES: quantity > 0
    // MODIFIES: this
    // EFFECTS: return true if sufficient quantity in holding
    public boolean isQuantitySufficient(int quantity) {
        return this.quantity >= quantity;
    }

    // REQUIRES: price > 0, quantity > 0
    // MODIFIES: this
    // EFFECTS: calculates new average price of holding
    public void updateHolding(int price, int quantity) {
        int currentBookValue = this.buyPrice * this.quantity;
        currentBookValue += price * quantity;
        addQuantity(quantity);
        this.buyPrice = currentBookValue / this.quantity;
    }

}

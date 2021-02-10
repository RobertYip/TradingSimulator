package model;

public class Stock {
    private static final int SPREAD = 10;

    private String ticker;
    private String name;
    private int bidPrice;
    private int askPrice;
    private int randomNumber;
    private double growth;

    // EFFECTS: creates stock with ticker, name, prices, and growth rate
    public Stock(String ticker, String name, int initialPrice, double growth) {
        this.ticker = ticker;
        this.name = name;
        this.bidPrice = initialPrice;
        this.growth = growth;
        calcRandom();
        calcSpread();
    }

    // EFFECTS: returns ticker
    public String getTicker() {
        return ticker;
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns ask price
    public int getAsk() {
        return askPrice;
    }

    // EFFECTS: returns bid price
    public int getBid() {
        return bidPrice;
    }

    // EFFECTS: returns growth rate
    public double getGrowth() {
        return growth;
    }

    // EFFECTS: returns random spread number
    public int getRandomNumber() {
        return randomNumber;
    }

    // MODIFIES: this
    // EFFECTS: Calculates new daily prices
    public void calcNewDaily() {
        applyGrowth();
        calcRandom();
        calcSpread();
    }

    // MODIFIES: this
    // EFFECTS: Calculates daily growth
    public void applyGrowth() {
        this.bidPrice = (int) (this.bidPrice * growth);
    }

    // MODIFIES: this
    // EFFECTS: Calculates a random number to be used for the spread
    public void calcRandom() {
        this.randomNumber = (int) (Math.random() * SPREAD);
    }

    // MODIFIES: this
    // EFFECTS: Calculates the random bid-ask spread
    public void calcSpread() {
        this.askPrice = this.bidPrice + this.randomNumber;
    }

}

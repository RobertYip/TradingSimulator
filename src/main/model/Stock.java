package model;

// A single stock in the stock market
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
        calcAskPrice();
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
        calcAskPrice();
    }

    // MODIFIES: this
    // EFFECTS: Calculates daily growth
    public void applyGrowth() {
        this.bidPrice = (int) (this.bidPrice * growth);
    }

    // MODIFIES: this
    // EFFECTS: Calculates a random number (between 1 and SPREAD)
    public void calcRandom() {
        int generatedNumber = (int) (Math.random() * SPREAD);
        this.randomNumber = Math.max(generatedNumber,1);
    }

    // MODIFIES: this
    // EFFECTS: Calculates ask price based on spread
    public void calcAskPrice() {
        this.askPrice = this.bidPrice + this.randomNumber;
    }

}

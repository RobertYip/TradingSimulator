package model;

public class Stock {
    private String ticker;
    private String name;
    private int bidPrice;
    private int askPrice;
    private int randomNumber;
    private double growth;

    public Stock(String ticker, String name, int initialPrice, double growth) {
        this.ticker = ticker;
        this.name = name;
        this.bidPrice = initialPrice;
        this.growth = growth;
        calcRandom();
        calcSpread();
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    public int getAsk() {
        return askPrice;
    }

    public int getBid() {
        return bidPrice;
    }

    public double getGrowth() {
        return growth;
    }

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
        this.randomNumber = (int) (Math.random() * 10);
    }

    // MODIFIES: this
    // EFFECTS: Calculates the random bid-ask spread
    public void calcSpread() {
        this.askPrice = this.bidPrice + this.randomNumber;
    }

}

package ui;

import model.Holding;
import model.Portfolio;
import model.Stock;
import model.StockMarket;

import java.util.Scanner;

public class TradingApp {
    private static final int INITIAL_CASH = 100;
    private static final int WIN_CONDITION = 1000;

    private Portfolio portfolio;
    private StockMarket stockMarket;
    private Scanner input;
    private int days = 1;

    // EFFECTS: runs trading simulator
    public TradingApp() {
        runSimulator();
    }

    // MODIFIES: this
    // EFFECTS: initializes simulator; processes user inputs and win condition if achieved
    public void runSimulator() {
        boolean gameRunning = true;
        String command;

        init();

        while (gameRunning) {
            if (portfolio.getCash() >= WIN_CONDITION) {
                System.out.println("\nYou win! You reached " + portfolio.getCash() + " in " + days + " days!");
                break;
            }

            displayMenu();
            command = input.next();
            command = command.toUpperCase();

            if (command.equals("Q")) {
                gameRunning = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Thanks for playing!");
    }

    // MODIFIES: this
    // EFFECTS: initializes player's portfolio and market
    private void init() {
        portfolio = new Portfolio(INITIAL_CASH);
        stockMarket = new StockMarket();
        input = new Scanner(System.in);
        initStockMarket();
    }

    // EFFECTS: load stocks into allStocks
    public void initStockMarket() {
        Stock apl = new Stock("APL", "Aple Inc.", 13, 1.1);
        Stock gms = new Stock("GMS", "GameShop", 30, 1.2);
        Stock dmc = new Stock("DMC", "DMC Inc.", 12, 1.1);
        Stock cmp = new Stock("CMP", "Computer Shop", 20, 1.3);

        stockMarket.addStock(apl);
        stockMarket.addStock(gms);
        stockMarket.addStock(dmc);
        stockMarket.addStock(cmp);
    }

    // EFFECTS: display user commands
    private void displayMenu() {
        System.out.println("\n Today is day " + days + ". Here are your commands:");
        System.out.println("\tL -> List stocks in stock market");
        System.out.println("\tC -> Check your portfolio");
        System.out.println("\tB -> Buy stocks from stock market");
        System.out.println("\tS -> Sell stocks from portfolio");
        System.out.println("\tN -> Advance to next day for new prices");
        System.out.println("\tQ -> Quit game");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "L":
                displayStocks();
                break;
            case "C":
                displayPortfolio();
                break;
            case "B":
                buyStock();
                break;
            case "S":
                sellTicker();
                break;
            case "N":
                nextDay();
                break;
            default:
                System.out.println("Selection not valid.");
                break;
        }
    }

    // MODIFIES: Portfolio
    // EFFECTS: goes through the process of buying the stock, if the entries are valid.
    private void buyStock() {
        boolean waitingCommand = true;
        String command;

        while (waitingCommand) {
            buyMessage();

            command = input.next();
            command = command.toUpperCase();

            if (command.equals("Q")) {
                waitingCommand = false;
            } else if (stockMarket.getStock(command) != null) {
                waitingCommand = !buyProcessed(command);
            } else {
                System.out.println("Stock does not exist, try again.");
            }
        }
    }

    // EFFECTS: display a message when buying stocks
    private void buyMessage() {
        displayStocks();
        System.out.println("Cash on hand: " + portfolio.getCash());
        System.out.println("\nWhich stock do you want to buy? Enter ticker. Type 'Q' to go back.");
    }

    // MODIFIES: Portfolio
    // EFFECTS: returns true if buying stock is successful, false if fails
    private Boolean buyProcessed(String ticker) {
        int quantityToBuy;
        int stockPrice;

        quantityToBuy = isPositiveInteger();
        stockPrice = stockMarket.getStock(ticker).getAsk();

        if (portfolio.addStock(ticker, quantityToBuy, stockPrice)) {
            System.out.println("Stock has been successfully added. Bought " + quantityToBuy
                    + " shares of " + ticker + " for " + stockPrice * quantityToBuy + ".");
            return true;
        }
        System.out.println("Failed to add stock, insufficient cash. Try again.");
        return false;
    }

    // EFFECTS: returns a valid quantity from user. Quantity is a positive integer.
    private int isPositiveInteger() {
        int quantity;
        System.out.println("Enter Quantity:");
        while (input.hasNext()) {
            if (input.hasNextInt()) {
                quantity = input.nextInt();
                if (quantity > 0) {
                    return quantity;
                }
            }
            System.out.println("Invalid quantity, enter a positive integer.");
        }
        return -1;
    }

    // MODIFIES: Portfolio
    // EFFECTS: goes through the process of selling the stock, if the entries are valid.
    private void sellTicker() {
        boolean waitingCommand = true;
        String command;

        while (waitingCommand) {
            sellMessage();

            command = input.next();
            command = command.toUpperCase();

            if (command.equals("Q")) {
                waitingCommand = false;
            } else if (portfolio.getHolding(command) != null) {
                waitingCommand = !sellQuantity(command);
            } else {
                System.out.println("Stock does not exist, try again.");
            }
        }
    }

    // EFFECTS: display a message when selling stocks
    private void sellMessage() {
        displayPortfolio();
        System.out.println("\nWhich stock do you want to sell? Enter ticker. Type 'Q' to go back.");
    }

    // MODIFIES: Portfolio
    // EFFECTS: returns true if selling stock is successful, false if fails
    private Boolean sellQuantity(String ticker) {
        int quantityToSell;
        int marketPrice;
        marketPrice = stockMarket.getStock(ticker).getAsk();

        // Verify quantity is positive integer
        quantityToSell = isPositiveInteger();

        if (portfolio.isQuantitySufficient(ticker, quantityToSell)) {
            portfolio.sellStock(ticker, quantityToSell, marketPrice);
            System.out.println("Stock has been successfully sold.");
            return true;
        }
        System.out.println("Insufficient quantity. Try again.");
        return false;
    }

    // MODIFIES: this
    // EFFECTS: adds one to day and updates stockMarket prices
    private void nextDay() {
        days += 1;
        stockMarket.updateAllPrices();
    }

    // EFFECTS: print out stock information
    public void displayStocks() {
        System.out.println("\nDisplaying list of stocks:");
        for (Stock s : stockMarket.getMarket()) {
            System.out.printf("\t%-15s \t%-25s \t%-10s \t%-10s\n",
                    "Ticker: " + s.getTicker(),
                    "Name: " + s.getName(),
                    "Bid: " + s.getBid(),
                    "Ask: " + s.getAsk());
        }
    }

    // EFFECTS: prints portfolio: Cash and holdings
    public void displayPortfolio() {
        int stockAtMarketPrice;
        int totalPortfolioValue;
        int totalHoldingsValue = 0;
        int cash = portfolio.getCash();

        System.out.println("\nDisplaying portfolio:");
        System.out.println("\tCash: " + cash);

        for (Holding h : portfolio.getHoldings()) {
            stockAtMarketPrice = stockMarket.getStock(h.getStockTicker()).getBid();
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

}


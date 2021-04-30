package ui;

import exceptions.InsufficientQuantityException;
import exceptions.InvalidInputException;
import model.Holding;
import model.Portfolio;
import model.Stock;
import model.StockMarket;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.*;
import javax.swing.ImageIcon;

// Main panel for trading
public class MainPanel extends JPanel implements ActionListener {
    private static final int INITIAL_CASH = 100;
    private int days = 1;
    private StockMarket stockMarket = new StockMarket();
    private Portfolio portfolio = new Portfolio(INITIAL_CASH);

    //MEDIA sources
    // source: https://freesound.org/people/grunz/sounds/109662/
    private final String successSound = "./data/success.wav";
    // source: https://www.myinstants.com/instant/final-fantasy-victory-fanfare/
    private final String victorySound = "./data/victory.wav";
    // source: https://memestocks.org/
    private final ImageIcon stocksIcon = new ImageIcon("./data/stocks.jpg");

    private static final int WIDTH = 800;
    private static final int HEIGHT = 350;
    private static final int BUTTON_HEIGHT = 40;

    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane tableContainer;
    private JPanel actionContainer;
    private JLabel dayLabel;
    private JLabel infoLabel;
    private JLabel instructionsLabel;

    private JButton actionButton;
    private JSpinner spinner;

    private Object[][] stockMarketTable;
    private Object[][] portfolioTable;

    private static final String JSON_STORE_PORTFOLIO = "./data/portfolio.json";
    private static final String JSON_STORE_STOCKMARKET = "./data/stockmarket.json";
    private static final String JSON_STORE_DAYS = "./data/days.json";

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: Constructor, sets up main area with table and action buttons
    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GRAY);

        initVariables();
        initStockMarket();
        initSaveLoad();

        setLayout(new FlowLayout(FlowLayout.LEFT));

        createTable();
        checkPortfolioScreen();
        addActionContainer();
    }

    // MODIFIES: this
    // EFFECTS: adds action container to panel
    public void addActionContainer() {
        actionContainer.add(dayLabel);
        actionContainer.add(instructionsLabel);
        actionContainer.add(spinner);
        actionContainer.add(actionButton);
        actionContainer.add(infoLabel);
        actionContainer.add(new JLabel(stocksIcon));
        add(actionContainer);
        actionContainer.setPreferredSize(new Dimension(180, 300));
    }

    // EFFECTS: show portfolio screen
    public void checkPortfolioScreen() {
        table.setModel(loadPortfolio());
        setActionsContainer("Select a stock and quantity to sell.",
                "Cash: " + portfolio.getCash(),
                "Sell");
    }

    // EFFECTS: show buy screen
    public void buyStockScreen() {
        table.setModel(loadMarket());
        setActionsContainer("Select a stock and quantity to buy.",
                "Cash: " + portfolio.getCash(),
                "Buy");
    }

    // EFFECTS: Initiate panel objects
    public void initVariables() {
        actionContainer = new JPanel();
        dayLabel = new JLabel();
        updateDayLabel();
        infoLabel = new JLabel();
        instructionsLabel = new JLabel();
        actionButton = new JButton("");
        actionButton.addActionListener(this);
        setSpinner();
    }

    // MODIFIES: this
    // EFFECTS: Update action container objects to parameters
    public void setActionsContainer(String instruction, String info, String buttonText) {
        setInstructionsLabel(instruction);
        setInfoLabel(info);
        updateDayLabel();
        createButton(buttonText, buttonText);
    }

    // MODIFIES: this
    // EFFECTS: Updates day label
    public void updateDayLabel() {
        dayLabel.setText("<html><p>Day: " + days + "</p></html>");
        dayLabel.setPreferredSize(new Dimension(170, 20));
    }

    // MODIFIES: this
    // EFFECTS: Sets info label
    public void setInfoLabel(String infoMsg) {
        infoLabel.setText("<html><p>" + infoMsg + "</p></html>");
        infoLabel.setPreferredSize(new Dimension(170, 20));
    }

    // MODIFIES: this
    // EFFECTS: Sets instructions label
    public void setInstructionsLabel(String instructionsMsg) {
        instructionsLabel.setText("<html><p>" + instructionsMsg + "</p></html>");
        instructionsLabel.setPreferredSize(new Dimension(170, 40));
    }


    // EFFECTS: Sets spinner parameter
    public void setSpinner() {
        int min = 0;
        int max = 100;
        int step = 1;
        int i = 1;
        SpinnerModel spinnerValue = new SpinnerNumberModel(i, min, max, step);
        spinner = new JSpinner(spinnerValue);
        spinner.setPreferredSize(new Dimension(50, 24));
    }


    // MODIFIES: this
    // EFFECTS: create main table area
    public void createTable() {
        tableModel = loadPortfolio();
        table = new JTable(tableModel);
        initTableSettings();
        tableContainer = new JScrollPane(table);
        add(tableContainer);
        tableContainer.setPreferredSize(new Dimension(600, 300));
    }

    // MODIFIES: this
    // EFFECTS: Settings to setup table
    public void initTableSettings() {
        TableColumnModel columnModel = table.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(250);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);

        // Align right for prices
        // src https://stackoverflow.com/questions/3467052/set-right-alignment-in-jtable-column
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        columnModel.getColumn(2).setCellRenderer(rightRenderer);
        columnModel.getColumn(3).setCellRenderer(rightRenderer);

        table.setRowHeight(25);
        table.setFont(new Font("Serif", Font.BOLD, 20));
    }

    // MODIFIES: StockMarket
    // EFFECTS: load stocks into allStocks
    public void initStockMarket() {
        Stock amc = new Stock("AMC", "AMC Entertainment", 5, 2);
        Stock amz = new Stock("AMZN", "Amazon", 3256, 1.1);
        Stock apl = new Stock("AAPL", "Apple Inc.", 13, 1.4);
        Stock bb = new Stock("BB", "BlackBerry", 7, 1.7);
        Stock fb = new Stock("FB", "FaceBook, Inc.", 300, 1.1);
        Stock gm = new Stock("GM", "General Motors", 49, 1.1);
        Stock gms = new Stock("GME", "GameStop", 5, 4.8);
        Stock ibc = new Stock("NOK", "Nokia", 2, 1.1);
        Stock msf = new Stock("MSFT", "MicroSoft", 221, 1.1);
        Stock shap = new Stock("SHOP", "Shopify", 1069, 1.1);
        Stock tsl = new Stock("TSLA", "Tesla", 657, 1.2);

        stockMarket.addStock(amc);
        stockMarket.addStock(amz);
        stockMarket.addStock(apl);
        stockMarket.addStock(bb);
        stockMarket.addStock(fb);
        stockMarket.addStock(gm);
        stockMarket.addStock(gms);
        stockMarket.addStock(ibc);
        stockMarket.addStock(msf);
        stockMarket.addStock(shap);
        stockMarket.addStock(tsl);
    }

    // EFFECTS: Initiates jsonWriter and jsonReader
    public void initSaveLoad() {
        jsonWriter = new JsonWriter(JSON_STORE_PORTFOLIO, JSON_STORE_STOCKMARKET, JSON_STORE_DAYS);
        jsonReader = new JsonReader(JSON_STORE_PORTFOLIO, JSON_STORE_STOCKMARKET, JSON_STORE_DAYS);
    }

    // MODIFIES: this
    // EFFECTS: loads stock market into table
    public DefaultTableModel loadMarket() {
        String[] headerNames = {"Stock Name", "Ticker", "Bid Price", "Ask Price"};
        ArrayList<Stock> marketData = stockMarket.getMarket();
        int marketSize = marketData.size();
        stockMarketTable = new Object[marketSize][4]; // 4: Name, ticker, bid, ask

        for (int i = 0; i < stockMarket.size(); i++) {
            Stock stockData = marketData.get(i);
            stockMarketTable[i][0] = stockData.getName();
            stockMarketTable[i][1] = stockData.getTicker();
            stockMarketTable[i][2] = stockData.getBid();
            stockMarketTable[i][3] = stockData.getAsk();
        }
        return new DefaultTableModel(stockMarketTable, headerNames);
    }

    // MODIFIES: this
    // EFFECTS: loads portfolio into table
    public DefaultTableModel loadPortfolio() {
        String[] headerNames = {"Ticker", "Quantity", "Buy Price", "Market Price"};
        ArrayList<Holding> portfolioData = portfolio.getHoldings();
        int portfolioSize = portfolioData.size();
        portfolioTable = new Object[portfolioSize][4]; // 4: ticker, quantity, buy price, sell price

        for (int i = 0; i < portfolioSize; i++) {
            Holding h = portfolioData.get(i);
            portfolioTable[i][0] = h.getStockTicker();
            portfolioTable[i][1] = h.getQuantity();
            portfolioTable[i][2] = h.getBuyPrice();
            portfolioTable[i][3] = stockMarket.getStock(h.getStockTicker()).getBid();
        }
        return new DefaultTableModel(portfolioTable, headerNames);
    }

    // MODIFIES: this
    // EFFECTS: creates button
    public void createButton(String text, String action) {
        actionButton.setText(text);
        actionButton.setPreferredSize(new Dimension(150, BUTTON_HEIGHT));
        actionButton.setVerticalTextPosition(AbstractButton.CENTER);
        actionButton.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        actionButton.setActionCommand(action);
    }

    // MODIFIES: this, portfolio
    // EFFECTS: Perform actions for the action button
    public void actionPerformed(ActionEvent e) {
        if ("Buy".equals(e.getActionCommand())) {
            try {
                buyStock();
            } catch (Exception exception) {
                messageBox("Please select a stock and enter a valid positive integer for quantity");
            }
        } else if ("Sell".equals(e.getActionCommand())) {
            try {
                sellTicker();
            } catch (Exception exception) {
                messageBox("Please select a stock and enter a valid positive integer for quantity");
            }
        }
    }

    // MODIFIES: this, Portfolio
    // EFFECTS: goes through the process of buying the stock, if the entries are valid.
    public void buyStock() {
        String ticker = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
        int stockPrice = stockMarket.getStock(ticker).getAsk();

        try {
            spinner.commitEdit();
        } catch (java.text.ParseException e) {
            messageBox("Unable to get quantity, try again");
        }
        int quantity = (Integer) spinner.getValue();
        try {
            if (portfolio.addStock(ticker, quantity, stockPrice)) {
                messageBox("Bought " + quantity + " " + ticker + " for $" + stockPrice * quantity + ".", successSound);
            } else {
                messageBox("Insufficient cash, try again.");
            }
        } catch (InvalidInputException e) {
            messageBox("Invalid quantity or price input. Please try again.");
        }
        setInfoLabel("Cash: " + portfolio.getCash());
    }

    // MODIFIES: this, Portfolio
    // EFFECTS: Sells a stock and updates portfolio
    private void sellTicker() {
        String ticker = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
        int stockPrice = stockMarket.getStock(ticker).getBid();

        try {
            spinner.commitEdit();
        } catch (java.text.ParseException e) {
            messageBox("Unable to get quantity, try again");
        }
        int quantity = (int) spinner.getValue();
        try {
            portfolio.sellStock(ticker, quantity, stockPrice);
            messageBox("Sold " + quantity + " " + ticker + " for $" + stockPrice * quantity + ".", successSound);
            setInfoLabel("Cash: " + portfolio.getCash());
        } catch (InvalidInputException e) {
            messageBox("Invalid quantity input or price. Please try again.");
        } catch (InsufficientQuantityException e) {
            messageBox("Insufficient quantity to sell.");
        }
        if (portfolio.getCash() > 1000) {
            messageBox("Congrats you beat the stock market, you now have $" + portfolio.getCash(), victorySound);
        }
        table.setModel(loadPortfolio());
    }

    // MODIFIES: this
    // EFFECTS: adds one to day and updates stockMarket prices
    public void nextDay() {
        days += 1;
        stockMarket.updateAllPrices();
        checkPortfolioScreen();
        updateDayLabel();
    }

    // EFFECTS: saves stock market and portfolio to file
    public void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(portfolio, stockMarket, days);
            jsonWriter.close();
            table.setModel(loadPortfolio());
            messageBox("Save complete", successSound);
        } catch (FileNotFoundException e) {
            messageBox("Unable to write to file");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads stock market and portfolio from file
    public void loadGame() {
        try {
            portfolio = jsonReader.readPortfolio();
            stockMarket = jsonReader.readStockMarket();
            days = jsonReader.readDays();
            checkPortfolioScreen();
            messageBox("Load complete", successSound);
        } catch (IOException e) {
            messageBox("Unable to read from file");
        }
    }

    // EFFECTS: Exit game; stop running application
    public void quitGame() {
        messageBox("Thanks for playing!");
        System.exit(1);
    }

    // EFFECTS: Custom message popup to save space
    public void messageBox(String msg) {
        JOptionPane.showMessageDialog(null,
                msg,
                "Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: Custom message popup to save space and plays a sound
    public void messageBox(String msg, String soundPath) {
        sound(soundPath);
        JOptionPane.showMessageDialog(null,
                msg,
                "Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: Play sounds at buttons
    // source: https://stackoverflow.com/questions/42955509/how-to-play-a-simple-audio-file-java
    // source: https://stackoverflow.com/questions/6045384/playing-mp3-and-wav-in-java
    public void sound(String soundPath) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(soundPath)));
            clip.start();
        } catch (Exception ex) {
            messageBox("Error loading sound");
        }
    }
}
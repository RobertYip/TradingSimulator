package persistence;

import model.Portfolio;
import model.StockMarket;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of Portfolio, StockMarket, and days to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writerP;
    private PrintWriter writerSM;
    private PrintWriter writerD;
    private String portfolioDestination;
    private String stockMarketDestination;
    private String daysDestination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String portfolioDestination, String stockMarketDestination, String daysDestination) {
        this.portfolioDestination = portfolioDestination;
        this.stockMarketDestination = stockMarketDestination;
        this.daysDestination = daysDestination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writerP = new PrintWriter(new File(portfolioDestination));
        writerSM = new PrintWriter(new File(stockMarketDestination));
        writerD = new PrintWriter(new File(daysDestination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of portfolio, stockmarket, days to file
    public void write(Portfolio p, StockMarket sm, int d) {
        JSONObject jsonP = p.toJson();
        JSONObject jsonSM = sm.toJson();
        JSONObject jsonD = new JSONObject();
        jsonD.put("days", d);

        saveToFile(jsonP.toString(TAB), jsonSM.toString(TAB), jsonD.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writerP.close();
        writerSM.close();
        writerD.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String jsonP, String jsonSM, String jsonD) {
        writerP.print(jsonP);
        writerSM.print(jsonSM);
        writerD.print(jsonD);
    }
}

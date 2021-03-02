package persistence;

import model.Portfolio;
import model.StockMarket;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writerP;
    private PrintWriter writerSM;
    private String portfolioDestination;
    private String stockMarketDestination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String portfolioDestination, String stockMarketDestination) {
        this.portfolioDestination = portfolioDestination;
        this.stockMarketDestination = stockMarketDestination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writerP = new PrintWriter(new File(portfolioDestination));
        writerSM = new PrintWriter(new File(stockMarketDestination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Portfolio p, StockMarket sm) {
        JSONObject jsonP = p.toJson();
        JSONObject jsonSM = sm.toJson();
        saveToFile(jsonP.toString(TAB), jsonSM.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writerP.close();
        writerSM.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String jsonP, String jsonSM) {
        writerP.print(jsonP);
        writerSM.print(jsonSM);
    }
}

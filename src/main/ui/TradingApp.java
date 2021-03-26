package ui;

import model.Holding;
import model.Portfolio;
import model.Stock;
import model.StockMarket;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TradingApp extends JFrame {
    // EFFECTS: runs trading simulator
    public TradingApp() {
        super("Trading App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel mainPanel = new MainPanel();
        CommandPanel cmdPanel = new CommandPanel(mainPanel);
        add(cmdPanel, BorderLayout.NORTH);
        add(mainPanel);
        setResizable(false);

        // Display Window
        pack();
        centreOnScreen();
        setVisible(true);
    }

    // Centres frame on desktop
    // MODIFIES: this
    // EFFECTS:  location of frame is set so frame is centred on desktop
    // Reference: SpaceInvaders
    private void centreOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    }


}


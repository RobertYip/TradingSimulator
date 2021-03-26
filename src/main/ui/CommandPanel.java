package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Panel for command buttons
public class CommandPanel extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 50;
    private static final int BUTTON_HEIGHT = 40;
    protected JButton portfolioButton;
    protected JButton buyButton;
    protected JButton nextButton;
    protected JButton saveButton;
    protected JButton loadButton;
    protected JButton quitButton;
    private MainPanel mainPanel;


    // EFFECTS: initializes command panel
    public CommandPanel(MainPanel mainPanel) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        initButtons();
        this.mainPanel = mainPanel;
    }

    // MODIFIES: this
    // EFFECTS: defines buttons
    public void initButtons() {
        portfolioButton = createButton("Check Portfolio", "checkPortfolio", 150);
        buyButton = createButton("Buy Stocks", "buyStocks", 150);
        nextButton = createButton("Next Day", "nextDay", 100);
        saveButton = createButton("Save", "save", 100);
        loadButton = createButton("Load", "load", 100);
        quitButton = createButton("Quit", "quit", 100);
    }

    // MODIFIES: this
    // EFFECTS: creates buttons
    public JButton createButton(String text, String action, int buttonWidth) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(buttonWidth, BUTTON_HEIGHT));
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        button.setActionCommand(action);
        button.addActionListener(this);
        add(button);
        return button;
    }

    // MODIFIES: mainPanel
    // EFFECTS: actions from buttons
    public void actionPerformed(ActionEvent e) {
        if ("checkPortfolio".equals(e.getActionCommand())) {
            mainPanel.checkPortfolioScreen();
        } else if ("buyStocks".equals(e.getActionCommand())) {
            mainPanel.buyStockScreen();
        } else if ("nextDay".equals(e.getActionCommand())) {
            mainPanel.nextDay();
        } else if ("save".equals(e.getActionCommand())) {
            mainPanel.saveGame();
        } else if ("load".equals(e.getActionCommand())) {
            mainPanel.loadGame();
        } else if ("quit".equals(e.getActionCommand())) {
            mainPanel.quitGame();
        }
    }
}
# Stock Trading Simulator

Buy and sell stocks to grow your portfolio!

## Phase 4: Task 2
The Portfolio class in the model package is robust. The Methods addStock and sellStock throws checked exceptions.

Test methods have been modified to test cases with and without catching exceptions. 

## Phase 4: Task 3
- I like the way my original models were setup, how Portfolio is associated with a collection of Holdings and
how StockMarket is associated with a collection of Stocks. Cohesion is high as each class has a single, focused purpose.
Coupling is low as changes in a class does not affect the other classes much. 
- Given more time, I would refactor the ui classes. Right now, a StockMarket and Portfolio object is instantiated
in the main panel, but it would make more sense to instantiate them in the TradingApp instead of the MainPanel. 
- From a cohesion standpoint, I would expect to find the Portfolio and StockMarket models to be objects of the main 
 TradingApp, rather than objects from a ui panel. 
- *Example: The model's init method for initial cash, stock prices, etc. makes more sense to be located in the TradingApp
 rather than the ui panel like it is now.*
- The model objects can then be passed into the ui panel for them to be
 modified.



## User Stories
- As a user, I want to see what stocks are in the stock market and buy stock.
- As a user, I want to sell stock.
- As a user, I want to see my portfolio and balance.
- As a user, I want to advance to the next day for new prices.
- As a user, I want to save my portfolio and stock market of a certain day 
- As a user, I want to load my portfolio and stock market to a certain day
## Actions

|Action|Description|
|---|---|
| "check" | Check your portfolio and sell stocks|
| "buy" | Buys the stock at the ask price (eg. "buy GME 100")|
| "next" | Advance to next day for new prices|
| "save" | Save game|
| "load" | Load game|
| "quit" | Quit the game|
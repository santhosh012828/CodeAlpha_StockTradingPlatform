package trading;

import java.util.*;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { // for simulating price updates
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + " ($" + price + ")";
    }
}

class Transaction {
    private String type; // "BUY" or "SELL"
    private Stock stock;
    private int quantity;
    private double totalPrice;

    public Transaction(String type, Stock stock, int quantity) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.totalPrice = stock.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return type + " " + quantity + " of " + stock.getSymbol() + " @ $" + stock.getPrice() + " (Total: $" + totalPrice + ")";
    }
}

class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private double cashBalance;

    public Portfolio(double initialCash) {
        this.cashBalance = initialCash;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cashBalance >= cost) {
            holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
            cashBalance -= cost;
            transactions.add(new Transaction("BUY", stock, quantity));
            System.out.println("✅ Bought " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("❌ Not enough balance to buy!");
        }
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.getSymbol(), 0);
        if (owned >= quantity) {
            holdings.put(stock.getSymbol(), owned - quantity);
            cashBalance += stock.getPrice() * quantity;
            transactions.add(new Transaction("SELL", stock, quantity));
            System.out.println("✅ Sold " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("❌ Not enough shares to sell!");
        }
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n📊 Portfolio Summary:");
        System.out.println("Cash Balance: $" + cashBalance);
        double totalValue = cashBalance;

        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            Stock stock = market.get(symbol);
            double value = stock.getPrice() * qty;
            System.out.println(symbol + ": " + qty + " shares @ $" + stock.getPrice() + " = $" + value);
            totalValue += value;
        }
        System.out.println("Total Portfolio Value: $" + totalValue);
    }

    public void showTransactions() {
        System.out.println("\n📝 Transaction History:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
}

public class TradingPlatform {
    private static Map<String, Stock> market = new HashMap<>();
    private static Portfolio portfolio;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        portfolio = new Portfolio(10000); // Start with $10,000

        // Initialize market with some stocks
        market.put("AAPL", new Stock("AAPL", 150));
        market.put("GOOG", new Stock("GOOG", 2800));
        market.put("TSLA", new Stock("TSLA", 700));
        market.put("AMZN", new Stock("AMZN", 3300));

        System.out.println("=== 📈 Stock Trading Platform ===");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("\n📌 Market Data:");
                    for (Stock s : market.values()) {
                        System.out.println(s);
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.next().toUpperCase();
                    if (market.containsKey(buySymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();
                        portfolio.buyStock(market.get(buySymbol), qty);
                    } else {
                        System.out.println("❌ Stock not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.next().toUpperCase();
                    if (market.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();
                        portfolio.sellStock(market.get(sellSymbol), qty);
                    } else {
                        System.out.println("❌ Stock not found!");
                    }
                    break;
                case 4:
                    portfolio.showPortfolio(market);
                    break;
                case 5:
                    portfolio.showTransactions();
                    break;
                case 6:
                    System.out.println("👋 Exiting... Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}

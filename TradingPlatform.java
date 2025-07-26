import java.util.*;

// Class for Stock
class Stock {
    String symbol;
    String companyName;
    double currentPrice;

    Stock(String symbol, String companyName, double currentPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
    }

    public void display() {
        System.out.printf("%-6s %-30s ₹%.2f\n", symbol, companyName, currentPrice);
    }
}

// Class for Transaction
class Transaction {
    String type;
    String symbol;
    int quantity;
    double price;

    Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return type + " " + symbol + " | Qty: " + quantity + " | Price: ₹" + price;
    }
}

// Class for Portfolio
class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    Map<String, Double> avgBuyPrice = new HashMap<>();

    public void buy(String symbol, int quantity, double price) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
        double currentAvg = avgBuyPrice.getOrDefault(symbol, 0.0);
        int currentQty = holdings.get(symbol);
        double newAvg = ((currentAvg * (currentQty - quantity)) + (price * quantity)) / currentQty;
        avgBuyPrice.put(symbol, newAvg);
        history.add(new Transaction("BUY", symbol, quantity, price));
    }

    public void sell(String symbol, int quantity, double price) {
        int owned = holdings.getOrDefault(symbol, 0);
        if (owned >= quantity) {
            holdings.put(symbol, owned - quantity);
            history.add(new Transaction("SELL", symbol, quantity, price));
        } else {
            System.out.println(" Not enough shares to sell.");
        }
    }

    public void showPortfolio(List<Stock> stockList) {
        System.out.println("\n Your Portfolio:");
        if (holdings.isEmpty()) {
            System.out.println("No holdings yet.");
        } else {
            double totalValue = 0;
            for (String symbol : holdings.keySet()) {
                int qty = holdings.get(symbol);
                double marketPrice = 0;
                for (Stock s : stockList) {
                    if (s.symbol.equals(symbol)) {
                        marketPrice = s.currentPrice;
                        break;
                    }
                }
                double cost = avgBuyPrice.get(symbol);
                double value = qty * marketPrice;
                double profit = (marketPrice - cost) * qty;
                totalValue += value;
                System.out.printf("%s -> %d shares | Market: ₹%.2f | Avg Buy: ₹%.2f | Profit/Loss: ₹%.2f\n",
                        symbol, qty, marketPrice, cost, profit);
            }
            System.out.printf(" Total Portfolio Value: ₹%.2f\n", totalValue);
        }
    }

    public void showHistory() {
        System.out.println("\n Transaction History:");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
    }
}

// Main class
public class TradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Portfolio portfolio = new Portfolio();

        // Predefined stocks
        List<Stock> market = Arrays.asList(
                new Stock("TCS", "Tata Consultancy Services", 3650.0),
                new Stock("INFY", "Infosys Ltd", 1490.0),
                new Stock("RELI", "Reliance Industries", 2825.0),
                new Stock("HDFC", "HDFC Bank", 1695.0),
                new Stock("ITC", "ITC Limited", 450.0)
        );

        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("   STOCK TRADING PLATFORM");
            System.out.println("==============================");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n Market Data:");
                    System.out.printf("%-6s %-30s %s\n", "Symbol", "Company", "Price");
                    System.out.println("---------------------------------------------");
                    for (Stock stock : market) {
                        stock.display();
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySym = sc.next().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();
                    boolean foundBuy = false;
                    for (Stock s : market) {
                        if (s.symbol.equals(buySym)) {
                            portfolio.buy(buySym, buyQty, s.currentPrice);
                            System.out.println(" Successfully bought!");
                            foundBuy = true;
                        }
                    }
                    if (!foundBuy) System.out.println(" Stock not found.");
                    break;

                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSym = sc.next().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();
                    boolean foundSell = false;
                    for (Stock s : market) {
                        if (s.symbol.equals(sellSym)) {
                            portfolio.sell(sellSym, sellQty, s.currentPrice);
                            System.out.println(" Sell operation completed.");
                            foundSell = true;
                        }
                    }
                    if (!foundSell) System.out.println(" Stock not found.");
                    break;

                case 4:
                    portfolio.showPortfolio(market);
                    break;

                case 5:
                    portfolio.showHistory();
                    break;

                case 6:
                    System.out.println(" Exiting platform. Goodbye!");
                    break;

                default:
                    System.out.println(" Invalid option. Try again.");
            }
        } while (choice != 6);

        sc.close();
    }
}

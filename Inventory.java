import java.time.LocalDate;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> items;
    private double totalSales;

    public Inventory() {
        items = new ArrayList<>();
        totalSales = 0.0;
        seedSampleData();
    }

    private void seedSampleData() {
        LocalDate today = LocalDate.now();
        items.add(new Item("Milk", 20, 50.0, today.plusDays(15), "10/10/2026 09:30 AM", 30));
        items.add(new Item("Bread", 15, 30.0, today.plusDays(10), "10/09/2026 02:00 PM", 25));
        items.add(new Item("Chips", 5, 25.0, today.plusDays(20), "10/08/2026 11:15 AM", 10));
        items.add(new Item("Yogurt", 12, 40.0, today.plusDays(3), "10/07/2026 08:45 AM", 20));
        items.add(new Item("Juice", 30, 60.0, today.plusDays(30), "10/11/2026 01:20 PM", 40));
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public double sellItem(int index, int qtyToSell) throws Exception {
        if (index < 0 || index >= items.size()) {
            throw new Exception("Please select a valid item from the table.");
        }
        Item item = items.get(index);
        item.deductStock(qtyToSell);
        double saleAmount = qtyToSell * item.getPrice();
        totalSales += saleAmount;
        return saleAmount;
    }

    public ArrayList<Item> getItems() { return items; }
    public double getTotalSales() { return totalSales; }

    public String checkAlerts() {
        StringBuilder alerts = new StringBuilder();
        for (Item item : items) {
            if (item.isExpired()) {
                alerts.append("• EXPIRED: ").append(item.getName())
                      .append(" (Expired on ").append(item.getExpirationDate()).append(")\n");
            } else if (item.isExpiringSoon()) {
                alerts.append("• EXPIRING SOON: ").append(item.getName())
                      .append(" (Expires on ").append(item.getExpirationDate()).append(")\n");
            }
            if (item.isLowStock()) {
                alerts.append("• LOW STOCK: ").append(item.getName())
                      .append(" (Only ").append(item.getQuantity()).append(" left)\n");
            }
        }
        return alerts.length() == 0 ? "All stock levels and expiration dates are healthy!" : alerts.toString();
    }
}

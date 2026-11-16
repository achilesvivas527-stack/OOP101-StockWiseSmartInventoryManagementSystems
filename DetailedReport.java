import java.util.ArrayList;

public class DetailedReport implements ReportGenerator {
    @Override
    public String getReportTitle() {
        return "Detailed Inventory & Valuation Report";
    }

    @Override
    public String generateReport(ArrayList<Item> items, double totalSales) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(getReportTitle()).append(" ===\n\n");
        double totalInventoryValue = 0;

        for (Item item : items) {
            double itemValue = item.getQuantity() * item.getPrice();
            totalInventoryValue += itemValue;
            sb.append(String.format("- %s | Qty: %d | Price: ₱%.2f | Value: ₱%.2f | Exp: %s | Status: %s\n",
                item.getName(), item.getQuantity(), item.getPrice(), itemValue,
                item.getExpirationDate(), item.getStatus()));
        }

        sb.append(String.format("\nTotal Inventory Asset Value: ₱%.2f\n", totalInventoryValue));
        sb.append(String.format("Total Accumulated Sales: ₱%.2f\n", totalSales));
        return sb.toString();
    }
}

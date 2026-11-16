import java.util.ArrayList;

public class SummaryReport implements ReportGenerator {
    @Override
    public String getReportTitle() {
        return "Summary Sales & Inventory Report";
    }

    @Override
    public String generateReport(ArrayList<Item> items, double totalSales) {
        int totalUnits = 0;
        int lowStockCount = 0;
        int expiredCount = 0;

        for (Item item : items) {
            totalUnits += item.getQuantity();
            if (item.isLowStock()) lowStockCount++;
            if (item.isExpired()) expiredCount++;
        }

        return String.format(
            "=== %s ===\n" +
            "Total Distinct Products: %d\n" +
            "Total Units in Stock: %d\n" +
            "Low Stock Alerts: %d\n" +
            "Expired Items: %d\n" +
            "Total Accumulated Sales: ₱%.2f\n",
            getReportTitle(), items.size(), totalUnits, lowStockCount, expiredCount, totalSales
        );
    }
}

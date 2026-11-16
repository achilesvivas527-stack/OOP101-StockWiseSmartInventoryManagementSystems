import java.util.ArrayList;

public interface ReportGenerator {
    String getReportTitle();
    String generateReport(ArrayList<Item> items, double totalSales);
}

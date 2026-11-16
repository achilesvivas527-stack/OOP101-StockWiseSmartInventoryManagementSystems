import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class StockWiseSIMS extends JFrame {
    private Inventory inventory;
    private User currentUser;
    private DefaultTableModel tableModel;
    private JTable inventoryTable;
    private JLabel lblTotalItems, lblLowStock, lblExpired, lblTotalSales;

    public StockWiseSIMS() {
        this(new AdminUser("admin"));
    }

    public StockWiseSIMS(User user) {
        this.currentUser = user;
        this.inventory = new Inventory();

        setTitle("StockWise - Smart Inventory Management System (" + currentUser.getRole() + ")");
        setSize(950, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();
        refreshDashboard();
        setVisible(true);
    }

    private void initUI() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 8, 8));
        sidebar.setBackground(new Color(24, 52, 94));
        sidebar.setBorder(new EmptyBorder(15, 15, 15, 15));
        sidebar.setPreferredSize(new Dimension(190, 0));

        JLabel lblBrand = new JLabel("STOCKWISE SIMS", SwingConstants.CENTER);
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnAddItem = createSidebarButton("Add Item");
        JButton btnSellItem = createSidebarButton("Sell Item");
        JButton btnAlerts = createSidebarButton("Check Alerts");
        JButton btnSummaryReport = createSidebarButton("Summary Report");
        JButton btnDetailedReport = createSidebarButton("Detailed Report");
        JButton btnLogout = createSidebarButton("Logout");

        btnAddItem.setEnabled(currentUser.canAddItems());

        sidebar.add(lblBrand);
        sidebar.add(btnAddItem);
        sidebar.add(btnSellItem);
        sidebar.add(btnAlerts);
        sidebar.add(btnSummaryReport);
        sidebar.add(btnDetailedReport);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        JPanel topPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        topPanel.setBorder(new EmptyBorder(15, 10, 5, 15));

        lblTotalItems = createStatCard(topPanel, "Total Products", new Color(33, 115, 200));
        lblLowStock = createStatCard(topPanel, "Low Stock", new Color(230, 126, 34));
        lblExpired = createStatCard(topPanel, "Expired Items", new Color(192, 57, 43));
        lblTotalSales = createStatCard(topPanel, "Total Sales", new Color(39, 174, 96));

        String[] columns = {"Item Name", "Quantity", "Price (₱)", "Expiry Date", "ETA", "Received", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(26);
        inventoryTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBorder(new EmptyBorder(5, 10, 15, 15));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(topPanel, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        btnAddItem.addActionListener(e -> handleAddItem());
        btnSellItem.addActionListener(e -> handleSellItem());
        btnAlerts.addActionListener(e -> JOptionPane.showMessageDialog(this, inventory.checkAlerts(), "Stock & Expiration Alerts", JOptionPane.WARNING_MESSAGE));
        btnSummaryReport.addActionListener(e -> showReport(new SummaryReport()));
        btnDetailedReport.addActionListener(e -> showReport(new DetailedReport()));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        return btn;
    }

    private JLabel createStatCard(JPanel parent, String title, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel lblValue = new JLabel("0");
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 20));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card);
        return lblValue;
    }

    private void refreshDashboard() {
        tableModel.setRowCount(0);
        int lowStock = 0;
        int expired = 0;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (Item item : inventory.getItems()) {
            if (item.isLowStock()) lowStock++;
            if (item.isExpired()) expired++;

            tableModel.addRow(new Object[]{
                item.getName(),
                item.getQuantity(),
                String.format("₱%.2f", item.getPrice()),
                item.getExpirationDate().format(fmt),
                item.getEta(),
                item.getAmountReceived() + " units",
                item.getStatus()
            });
        }

        lblTotalItems.setText(String.valueOf(inventory.getItems().size()));
        lblLowStock.setText(String.valueOf(lowStock));
        lblExpired.setText(String.valueOf(expired));
        lblTotalSales.setText(String.format("₱%.2f", inventory.getTotalSales()));
    }

    private void handleAddItem() {
        JTextField txtName = new JTextField();
        JTextField txtQty = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtExp = new JTextField(LocalDate.now().plusDays(14).toString());

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Item Name:"));
        form.add(txtName);
        form.add(new JLabel("Quantity:"));
        form.add(txtQty);
        form.add(new JLabel("Price (₱):"));
        form.add(txtPrice);
        form.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        form.add(txtExp);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Inventory Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = txtName.getText().trim();
                if (name.isEmpty()) throw new IllegalArgumentException("Item name cannot be empty.");
                int qty = Integer.parseInt(txtQty.getText().trim());
                double price = Double.parseDouble(txtPrice.getText().trim());
                LocalDate expDate = LocalDate.parse(txtExp.getText().trim());

                if (qty < 0 || price < 0) throw new IllegalArgumentException("Quantity and price must be positive.");

                inventory.addItem(new Item(name, qty, price, expDate, "Just Now", qty));
                refreshDashboard();
                JOptionPane.showMessageDialog(this, "Item added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Date Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSellItem() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item in the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemName = (String) tableModel.getValueAt(selectedRow, 0);
        String input = JOptionPane.showInputDialog(this, "Enter quantity to sell for " + itemName + ":");
        if (input != null) {
            try {
                int qtyToSell = Integer.parseInt(input.trim());
                double saleTotal = inventory.sellItem(selectedRow, qtyToSell);
                refreshDashboard();
                JOptionPane.showMessageDialog(this, String.format("Sold %d x %s\nTotal Sale: ₱%.2f", qtyToSell, itemName, saleTotal));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid whole number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Sale Blocked", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showReport(ReportGenerator generator) {
        String reportText = generator.generateReport(inventory.getItems(), inventory.getTotalSales());
        JTextArea textArea = new JTextArea(reportText, 14, 45);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), generator.getReportTitle(), JOptionPane.INFORMATION_MESSAGE);
    }
}

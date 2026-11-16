import java.time.LocalDate;

public class Item {
    private String name;
    private int quantity;
    private double price;
    private LocalDate expirationDate;
    private String eta;
    private int amountReceived;

    public Item(String name, int quantity, double price, LocalDate expirationDate, String eta, int amountReceived) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expirationDate = expirationDate;
        this.eta = eta;
        this.amountReceived = amountReceived;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public String getEta() { return eta; }
    public int getAmountReceived() { return amountReceived; }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantity = quantity;
    }

    public void deductStock(int amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Sale quantity must be greater than zero.");
        }
        if (isExpired()) {
            throw new Exception("Cannot sell '" + name + "' because it is already EXPIRED!");
        }
        if (amount > quantity) {
            throw new Exception("Insufficient stock! Only " + quantity + " units of '" + name + "' available.");
        }
        this.quantity -= amount;
    }

    public boolean isLowStock() {
        return quantity <= 5;
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon() {
        LocalDate today = LocalDate.now();
        return !isExpired() && !expirationDate.isAfter(today.plusDays(7));
    }

    public String getStatus() {
        if (isExpired()) return "Expired";
        if (isExpiringSoon()) return "Expiring Soon";
        if (isLowStock()) return "Low Stock";
        return "In Stock";
    }
}

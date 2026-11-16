public class AdminUser extends User {
    public AdminUser(String username) {
        super(username, "Admin");
    }

    @Override
    public boolean canAddItems() {
        return true;
    }
}


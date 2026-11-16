public class StaffUser extends User {
    public StaffUser(String username) {
        super(username, "Staff");
    }

    @Override
    public boolean canAddItems() {
        return false;
    }
}

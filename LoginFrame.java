import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("StockWise Login");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(5, 1, 8, 8));
        panel.setBorder(new EmptyBorder(15, 25, 10, 25));

        JLabel title = new JLabel("STOCKWISE LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");

        panel.add(title);
        panel.add(new JLabel("Username:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password:"));
        panel.add(txtPassword);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        buttonPanel.add(btnLogin);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnLogin);
        btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        char[] password = txtPassword.getPassword();
        char[] correctPassword = {'1', '2', '3', '4'};

        if (Arrays.equals(password, correctPassword) && username.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "Welcome, Admin!");
            dispose();
            new StockWiseSIMS(new AdminUser(username));
        } else if (Arrays.equals(password, correctPassword) && username.equalsIgnoreCase("staff")) {
            JOptionPane.showMessageDialog(this, "Welcome, Staff!");
            dispose();
            new StockWiseSIMS(new StaffUser(username));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }

        Arrays.fill(password, '0');
        Arrays.fill(correctPassword, '0');
    }
}

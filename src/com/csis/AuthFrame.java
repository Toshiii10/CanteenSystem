package com.csis;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
public class AuthFrame extends JFrame {
    private final JTextField user = new JTextField(18);
    private final JPasswordField pass = new JPasswordField(18);
    public AuthFrame() {
        super(AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 350);
        setLocationRelativeTo(null);
        build();
    }
    private void build() {
        JPanel all = new JPanel(new BorderLayout(10, 10));
        all.setBorder(BorderFactory.createEmptyBorder(22, 30, 22, 30));
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(UIUtils.title("Canteen Sales and Inventory System"));
        top.add(new JLabel("Administrator and Customer Access"));
        all.add(top, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Login"));
        form.add(new JLabel("Username:")); form.add(user);
        form.add(new JLabel("Password:")); form.add(pass);
        JButton login = new JButton("Login");
        JButton register = new JButton("Register Customer");
        JButton test = new JButton("Test Database");
        JButton exit = new JButton("Exit");
        form.add(login); form.add(register); form.add(test); form.add(exit);
        all.add(form, BorderLayout.CENTER);
        all.add(new JLabel("Demo: admin / Admin@123     customer1 / User@123"), BorderLayout.SOUTH);
        add(all);
        login.addActionListener(e -> login());
        pass.addActionListener(e -> login());
        register.addActionListener(e -> new RegisterDialog(this).setVisible(true));
        test.addActionListener(e -> UIUtils.info(this, DB.test() ? "Database connection successful." : "Cannot connect. Import SQL and check config/db.properties."));
        exit.addActionListener(e -> System.exit(0));
    }
    private void login() {
        String username = user.getText().trim();
        String hash = PasswordUtil.hash(new String(pass.getPassword()));
        String sql = "SELECT user_id, username, full_name, role, status FROM users WHERE username=? AND password_hash=?";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, username); p.setString(2, hash);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) { UIUtils.info(this, "Invalid username or password."); return; }
                if (!"ACTIVE".equals(r.getString("status"))) { UIUtils.info(this, "Account is inactive."); return; }
                UserSession session = new UserSession(r.getInt("user_id"), r.getString("username"), r.getString("full_name"), r.getString("role"));
                Audit.log(session, "LOGIN", "Successful login");
                dispose();
                if ("CUSTOMER".equals(session.role)) new UserFrame(session).setVisible(true);
                else new AdminFrame(session).setVisible(true);
            }
        } catch (Exception ex) { UIUtils.error(this, ex); }
    }
}

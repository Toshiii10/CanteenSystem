package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserMessagePanel extends JPanel {
    private final UserSession session;
    private final JTextField subjectField = new JTextField(20);
    private final JTextArea bodyArea = new JTextArea(5, 35);
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    public UserMessagePanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Subject:"));
        formPanel.add(subjectField);
        formPanel.add(new JLabel("Message:"));
        formPanel.add(new JScrollPane(bodyArea));
        
        JButton btnSend = new JButton("Send to Admin");
        formPanel.add(btnSend);
        
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnSend.addActionListener(e -> sendMessage());
        loadMessages();
    }

    private void loadMessages() {
        String sql = """
            SELECT message_id, subject, body, status, created_at 
            FROM messages 
            WHERE sender_id = ? OR receiver_id = ? 
            ORDER BY message_id DESC
            """;
        try {
            model.load(sql, session.userId, session.userId);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void sendMessage() {
        // 1. Defensive Validation: Prevent empty/whitespace submissions
        String subjectText = subjectField.getText().trim();
        String bodyText = bodyArea.getText().trim();

        if (subjectText.isEmpty() || bodyText.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Subject and Message body cannot be empty."));
            return;
        }

        // Hardcoding receiver_id = 1 (System Admin) is acceptable for this scope
        String sql = "INSERT INTO messages(sender_id, receiver_id, subject, body) VALUES(?, 1, ?, ?)";
        
        try (Connection c = DB.getConnection(); 
             PreparedStatement p = c.prepareStatement(sql)) {
            
            p.setInt(1, session.userId);
            p.setString(2, subjectText);
            p.setString(3, bodyText);
            p.executeUpdate();
            
            Audit.log(session, "SEND_MESSAGE", subjectText);
            
            // Clean runway
            subjectField.setText("");
            bodyArea.setText("");
            
            loadMessages();
            UIUtils.info(this, "Message sent to Administration successfully.");
            
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}
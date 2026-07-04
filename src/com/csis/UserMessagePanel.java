package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.sql.*;

public class UserMessagePanel extends JPanel {
    private final UserSession session;
    private final JTextField subjectField = new JTextField(20);
    private final JTextArea bodyArea = new JTextArea(5, 35);
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final Timer autoRefreshTimer;

    public UserMessagePanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel formPanel = new JPanel();
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        formPanel.add(subjectLabel);
        formPanel.add(subjectField);
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        formPanel.add(messageLabel);
        formPanel.add(new JScrollPane(bodyArea));
        
        JButton btnSend = new JButton("Send to Admin");
        btnSend.setFont(new Font("Tahoma", Font.BOLD, 13)); // Bold
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13)); // Bolded
        
        formPanel.add(btnSend);
        formPanel.add(btnRefresh);
        
        add(formPanel, BorderLayout.NORTH);
        
        // --- THE FIX: FORCED BOLD TABLE HEADERS ---
        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnSend.addActionListener(e -> sendMessage());
        btnRefresh.addActionListener(e -> loadMessages());
        loadMessages();

        autoRefreshTimer = new Timer(15_000, e -> loadMessages());
        autoRefreshTimer.start();

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    private void loadMessages() {
        String sql = """
            SELECT message_id AS 'Msg ID', subject AS 'Subject', body AS 'Message', status AS 'Status', created_at AS 'Date Sent' 
            FROM messages 
            WHERE sender_id = ? OR receiver_id = ? 
            ORDER BY message_id DESC
            """;
        try {
            model.load(sql, session.userId, session.userId);
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void sendMessage() {
        String subjectText = subjectField.getText().trim();
        String bodyText = bodyArea.getText().trim();

        if (subjectText.isEmpty() || bodyText.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Subject and Message body cannot be empty."));
            return;
        }

        String sql = "INSERT INTO messages(sender_id, receiver_id, subject, body) VALUES(?, 1, ?, ?)";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, session.userId); p.setString(2, subjectText); p.setString(3, bodyText);
            p.executeUpdate();
            Audit.log(session, "SEND_MESSAGE", subjectText);
            subjectField.setText(""); bodyArea.setText("");
            loadMessages(); UIUtils.info(this, "Message sent to Administration successfully.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}
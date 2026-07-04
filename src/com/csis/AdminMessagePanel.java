package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.sql.*;

/**
 * Admin/Staff inbox panel. Previously there was no UI for any ADMIN/STAFF account to read
 * messages sent via UserMessagePanel (which hardcodes receiver_id = 1). This panel lists
 * messages addressed to the currently logged-in admin/staff user and lets them reply,
 * which inserts a message back to the original sender.
 */
public class AdminMessagePanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    private final JLabel replyTargetLabel = new JLabel("Reply to: (select a message above)");
    private final JTextField replyToUserIdField = new JTextField(6);
    private final JTextField subjectField = new JTextField(20);
    private final JTextArea bodyArea = new JTextArea(5, 35);

    private final Timer autoRefreshTimer;

    public AdminMessagePanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;

        JPanel headerPanel = new JPanel();
        headerPanel.add(UIUtils.title("Inbox - Customer Messages"));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnMarkRead = new JButton("Mark Selected as Read");
        headerPanel.add(btnRefresh);
        headerPanel.add(btnMarkRead);
        add(headerPanel, BorderLayout.NORTH);

        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel replyPanel = new JPanel();
        replyPanel.add(replyTargetLabel);
        replyPanel.add(new JLabel("User ID:"));
        replyToUserIdField.setEditable(false);
        replyPanel.add(replyToUserIdField);
        replyPanel.add(new JLabel("Subject:"));
        replyPanel.add(subjectField);
        replyPanel.add(new JLabel("Message:"));
        replyPanel.add(new JScrollPane(bodyArea));
        JButton btnSend = new JButton("Send Reply");
        replyPanel.add(btnSend);
        add(replyPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadMessages());
        btnMarkRead.addActionListener(e -> markSelectedRead());
        btnSend.addActionListener(e -> sendReply());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.convertRowIndexToModel(table.getSelectedRow());

                int senderIdCol = model.findColumnByName("sender_id");
                Object senderId = senderIdCol >= 0 ? model.getValueAt(row, senderIdCol) : null;
                replyToUserIdField.setText(senderId == null ? "" : String.valueOf(senderId));

                int senderNameCol = model.findColumnByName("sender_name");
                Object senderName = senderNameCol >= 0 ? model.getValueAt(row, senderNameCol) : null;
                int senderUserCol = model.findColumnByName("sender_username");
                Object senderUsername = senderUserCol >= 0 ? model.getValueAt(row, senderUserCol) : null;

                replyTargetLabel.setText("Reply to: " + (senderName == null ? "Unknown" : senderName)
                        + (senderUsername == null ? "" : " (@" + senderUsername + ")"));

                int subjectCol = model.findColumnByName("subject");
                if (subjectCol >= 0) {
                    Object subj = model.getValueAt(row, subjectCol);
                    subjectField.setText("Re: " + (subj == null ? "" : subj));
                }
            }
        });

        loadMessages();

        // Refresh click, so a new incoming customer message would not appear until
        // the admin manually refreshed. Auto-refresh keeps the inbox current.
        autoRefreshTimer = new Timer(15_000, e -> loadMessages());
        autoRefreshTimer.start();

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    private void loadMessages() {
        // Messages addressed to this admin/staff account, newest first, with sender's
        // display name resolved for readability.
        String sql = """
                SELECT m.message_id, m.sender_id, u.full_name AS sender_name, u.username AS sender_username,
                       m.subject, m.body, m.status, m.created_at
                FROM messages m
                LEFT JOIN users u ON u.user_id = m.sender_id
                WHERE m.receiver_id = ?
                ORDER BY m.message_id DESC
                """;
        try {
            model.load(sql, session.userId);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void markSelectedRead() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UIUtils.error(this, new IllegalArgumentException("Select a message first."));
            return;
        }
        row = table.convertRowIndexToModel(row);
        int idCol = model.findColumnByName("message_id");
        if (idCol < 0) return;

        Object messageId = model.getValueAt(row, idCol);
        String sql = "UPDATE messages SET status = 'READ' WHERE message_id = ? AND receiver_id = ?";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setObject(1, messageId);
            p.setInt(2, session.userId);
            p.executeUpdate();
            loadMessages();
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void sendReply() {
        String userIdText = replyToUserIdField.getText().trim();
        String subjectText = subjectField.getText().trim();
        String bodyText = bodyArea.getText().trim();

        // the reply target field was a plain editable JTextField,
        // and if the admin clicked "Send Reply" without selecting a message row
        // first (or after clearing the field), this silently failed with a generic
        // "required fields" error and the customer never received anything. The
        // field is now read-only and only populated via row selection, with a
        // clearer error pointing the admin back to the table.
        if (userIdText.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Select a message from the inbox above first, so the reply has a recipient."));
            return;
        }
        if (subjectText.isEmpty() || bodyText.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Subject and Message body are required."));
            return;
        }

        int recipientUserId;
        try {
            recipientUserId = Integer.parseInt(userIdText);
        } catch (NumberFormatException nfe) {
            UIUtils.error(this, new IllegalArgumentException("Recipient User ID must be a number."));
            return;
        }

        String sql = "INSERT INTO messages(sender_id, receiver_id, subject, body) VALUES(?, ?, ?, ?)";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, session.userId);
            p.setInt(2, recipientUserId);
            p.setString(3, subjectText);
            p.setString(4, bodyText);
            p.executeUpdate();

            Audit.log(session, "SEND_MESSAGE", "Reply to user " + recipientUserId + ": " + subjectText);

            subjectField.setText("");
            bodyArea.setText("");
            String targetDesc = replyTargetLabel.getText().replace("Reply to: ", "");
            UIUtils.info(this, "Reply sent to " + targetDesc + " (User ID " + recipientUserId + ").");
            loadMessages();
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}
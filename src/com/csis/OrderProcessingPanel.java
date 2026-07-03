package com.csis;
import javax.swing.*; import java.awt.*;
import java.sql.Connection; import java.sql.PreparedStatement; import java.sql.ResultSet; import java.sql.SQLException;

public class OrderProcessingPanel extends JPanel {
    private final UserSession s;
    private final QueryTableModel m=new QueryTableModel();
    private final JTable t=new JTable(m);
    private final JTextField id=new JTextField(7);
    private final JLabel activeTimerLabel = new JLabel("Selected Order Countdown: --");

    public OrderProcessingPanel(UserSession s){
        super(new BorderLayout(8,8));this.s=s;
        JPanel h=new JPanel();h.add(UIUtils.title("Live Kitchen Display Board & Queue Control"));h.add(new JLabel("Order ID:"));h.add(id);

        JButton startPrep=new JButton("Start Cooking"), markReady=new JButton("Mark Ready"), completeServed=new JButton("Complete/Serve"), refresh=new JButton("Refresh Board");
        h.add(startPrep); h.add(markReady); h.add(completeServed); h.add(refresh);

        activeTimerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activeTimerLabel.setForeground(Color.RED);
        h.add(activeTimerLabel);

        add(h,BorderLayout.NORTH);add(new JScrollPane(t),BorderLayout.CENTER);

        startPrep.addActionListener(e->triggerStartCooking());
        markReady.addActionListener(e->triggerMarkReady());
        completeServed.addActionListener(e->triggerServeComplete());
        refresh.addActionListener(e->load());

        t.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting() && t.getSelectedRow() >= 0) {
                calculateLiveCountdown();
            }
        });
        load();
    }

    private void load(){
        try {
            // Priority Sort Rule: Dispatches by queue_priority score, then pickup schedule slots.
            // NOTE: v_kitchen_display_queue only includes PENDING/CONFIRMED/PREPARING orders.
            // Once an order is marked READY it drops out of this view, so the "Mark Ready"
            // action below clears the selection/timer rather than leaving a stale row selected.
            m.load("SELECT order_id, order_no, scheduled_pickup, customer_type, order_status, total_estimated_prep_minutes AS 'Est Duration' FROM v_kitchen_display_queue ORDER BY queue_priority ASC, scheduled_pickup ASC");
        } catch(Exception e) { UIUtils.error(this,e); }
    }

    private void calculateLiveCountdown() {
        try {
            int selectedRow = t.getSelectedRow();
            int orderId = Integer.parseInt(String.valueOf(t.getValueAt(selectedRow, 0)));
            id.setText(String.valueOf(orderId));

            // BUGFIX: ResultSet was never closed (only Connection and PreparedStatement were
            // in the try-with-resources). Added explicit resource management for rs.
            try (Connection c = DB.getConnection();
                 PreparedStatement p = c.prepareStatement("SELECT prep_started_at, total_estimated_prep_minutes FROM v_kitchen_display_queue WHERE order_id=?")) {
                p.setInt(1, orderId);
                try (ResultSet rs = p.executeQuery()) {
                    if (rs.next() && rs.getTimestamp("prep_started_at") != null) {
                        long startedTime = rs.getTimestamp("prep_started_at").getTime();
                        long durationMillis = rs.getInt("total_estimated_prep_minutes") * 60000L;
                        long elapsed = System.currentTimeMillis() - startedTime;
                        long remainingSecs = (durationMillis - elapsed) / 1000L;

                        if (remainingSecs > 0) {
                            activeTimerLabel.setText("Order #" + orderId + " Est Time Remaining: " + (remainingSecs / 60) + "m " + (remainingSecs % 60) + "s");
                        } else {
                            activeTimerLabel.setText("Order #" + orderId + " Target Prep Window Overdue!");
                        }
                    } else {
                        activeTimerLabel.setText("Order Chosen: Waiting to start preparation.");
                    }
                }
            }
        } catch (Exception ex) { activeTimerLabel.setText("Select an order to view the live countdown."); }
    }

    private int parseOrderId() {
        String text = id.getText().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Enter or select an Order ID first.");
        }
        return Integer.parseInt(text);
    }

    private void triggerStartCooking() {
        try {
            int orderId = parseOrderId();
            CanteenService.startKitchenPreparation(orderId);
            Audit.log(s,"KITCHEN_START_PREP",String.valueOf(orderId)); load();
        } catch(Exception e) { UIUtils.error(this,e); }
    }

    private void triggerMarkReady() {
        try {
            int orderId = parseOrderId();
            CanteenService.markKitchenReady(orderId);
            Audit.log(s,"KITCHEN_MARK_READY",String.valueOf(orderId)); load();
            // Order now drops out of v_kitchen_display_queue; reset the live countdown
            // and clear the id field so a stale selection doesn't get re-submitted.
            activeTimerLabel.setText("Selected Order Countdown: --");
            id.setText("");
        } catch(Exception e) { UIUtils.error(this,e); }
    }

    private void triggerServeComplete() {
        try {
            int orderId = parseOrderId();
            CanteenService.closeOrderServed(orderId);
            Audit.log(s,"KITCHEN_SERVE_COMPLETE",String.valueOf(orderId)); load();
            id.setText("");
        } catch(Exception e) { UIUtils.error(this,e); }
    }
}
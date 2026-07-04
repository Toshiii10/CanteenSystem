package com.csis;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.sql.Connection; import java.sql.PreparedStatement; import java.sql.ResultSet;

/**
 * Kitchen + pickup-counter control panel.
 *
 * FIX: previously this panel only showed v_kitchen_display_queue, which excludes orders
 * with order_status='READY' by design. Once an order was marked Ready, it vanished from
 * this screen entirely, and staff had no dedicated place to mark it COMPLETED when the
 * customer arrived to claim it -- they had to use the generic "Monitor Unclaimed Orders"
 * CRUD grid instead. This panel now shows two queues:
 *   1. Kitchen Queue (PENDING/CONFIRMED/PREPARING) - Start Cooking / Mark Ready
 *   2. Ready for Pickup (READY) - Complete / Serve (this is the "claim" action)
 */
public class OrderProcessingPanel extends JPanel {
    private final UserSession s;

    private final QueryTableModel kitchenModel = new QueryTableModel();
    private final JTable kitchenTable = new JTable(kitchenModel);
    private final JTextField kitchenIdField = new JTextField(7);
    private final JLabel activeTimerLabel = new JLabel("Selected Order Countdown: --");

    private final QueryTableModel readyModel = new QueryTableModel();
    private final JTable readyTable = new JTable(readyModel);
    private final JTextField readyIdField = new JTextField(7);

    private final Timer autoRefreshTimer;

    public OrderProcessingPanel(UserSession s) {
        super(new BorderLayout(8, 8));
        this.s = s;

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildKitchenSection(), buildReadySection());
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        loadKitchenQueue();
        loadReadyQueue();

        // Auto-refresh both queues so newly-confirmed orders and newly-ready orders
        // appear without staff needing to click Refresh constantly.
        autoRefreshTimer = new Timer(10_000, e -> { loadKitchenQueue(); loadReadyQueue(); });
        autoRefreshTimer.start();

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    private JPanel buildKitchenSection() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel h = new JPanel();
        h.add(UIUtils.title("Kitchen Queue (Cooking)"));
        h.add(new JLabel("Order ID:"));
        h.add(kitchenIdField);

        JButton startPrep = new JButton("Start Cooking");
        JButton markReady = new JButton("Mark Ready");
        JButton refresh = new JButton("Refresh Board");
        h.add(startPrep);
        h.add(markReady);
        h.add(refresh);

        activeTimerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activeTimerLabel.setForeground(Color.RED);
        h.add(activeTimerLabel);

        panel.add(h, BorderLayout.NORTH);
        panel.add(new JScrollPane(kitchenTable), BorderLayout.CENTER);

        startPrep.addActionListener(e -> triggerStartCooking());
        markReady.addActionListener(e -> triggerMarkReady());
        refresh.addActionListener(e -> loadKitchenQueue());

        kitchenTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && kitchenTable.getSelectedRow() >= 0) {
                calculateLiveCountdown();
            }
        });

        return panel;
    }

    private JPanel buildReadySection() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel h = new JPanel();
        h.add(UIUtils.title("Ready for Pickup (Claim Counter)"));
        h.add(new JLabel("Order ID:"));
        h.add(readyIdField);

        JButton completeServed = new JButton("Complete / Serve (Claimed)");
        JButton refresh = new JButton("Refresh Board");
        h.add(completeServed);
        h.add(refresh);

        panel.add(h, BorderLayout.NORTH);
        panel.add(new JScrollPane(readyTable), BorderLayout.CENTER);

        completeServed.addActionListener(e -> triggerServeComplete());
        refresh.addActionListener(e -> loadReadyQueue());

        readyTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && readyTable.getSelectedRow() >= 0) {
                int row = readyTable.getSelectedRow();
                readyIdField.setText(String.valueOf(readyTable.getValueAt(row, 0)));
            }
        });

        return panel;
    }

    private void loadKitchenQueue() {
        try {
            // Priority Sort Rule: Dispatches by queue_priority score, then pickup schedule slots.
            kitchenModel.load("SELECT order_id, order_no, scheduled_pickup, customer_type, order_status, total_estimated_prep_minutes AS 'Est Duration' FROM v_kitchen_display_queue ORDER BY queue_priority ASC, scheduled_pickup ASC");
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void loadReadyQueue() {
        try {
            // Orders that have left the kitchen queue (READY) and are waiting to be
            // physically handed to the customer at the counter.
            readyModel.load("""
                    SELECT o.order_id, o.order_no, COALESCE(u.full_name,'Walk-in') AS customer,
                           o.order_type, o.total_amount, o.prep_ready_at
                    FROM orders o
                    LEFT JOIN customer_profiles cp ON cp.customer_id = o.customer_id
                    LEFT JOIN users u ON u.user_id = cp.user_id
                    WHERE o.order_status = 'READY'
                    ORDER BY o.prep_ready_at ASC
                    """);
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void calculateLiveCountdown() {
        try {
            int selectedRow = kitchenTable.getSelectedRow();
            int orderId = Integer.parseInt(String.valueOf(kitchenTable.getValueAt(selectedRow, 0)));
            kitchenIdField.setText(String.valueOf(orderId));

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

    private int parseOrderId(JTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Enter or select an Order ID first.");
        }
        return Integer.parseInt(text);
    }

    private void triggerStartCooking() {
        try {
            int orderId = parseOrderId(kitchenIdField);
            CanteenService.startKitchenPreparation(orderId);
            Audit.log(s, "KITCHEN_START_PREP", String.valueOf(orderId));
            loadKitchenQueue();
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void triggerMarkReady() {
        try {
            int orderId = parseOrderId(kitchenIdField);
            CanteenService.markKitchenReady(orderId);
            Audit.log(s, "KITCHEN_MARK_READY", String.valueOf(orderId));
            // Order moves from the kitchen queue into the Ready-for-Pickup queue below.
            activeTimerLabel.setText("Selected Order Countdown: --");
            kitchenIdField.setText("");
            loadKitchenQueue();
            loadReadyQueue();
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void triggerServeComplete() {
        try {
            int orderId = parseOrderId(readyIdField);
            CanteenService.closeOrderServed(orderId);
            Audit.log(s, "KITCHEN_SERVE_COMPLETE", String.valueOf(orderId));
            readyIdField.setText("");
            loadReadyQueue();
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}
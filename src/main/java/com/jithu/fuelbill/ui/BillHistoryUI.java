package com.jithu.fuelbill.ui;

import com.jithu.fuelbill.service.BillHistoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BillHistoryUI extends JFrame {

    private final BillHistoryService billService =
            new BillHistoryService();

    private final DefaultTableModel model =
            new DefaultTableModel(
                    new String[]{
                            "Bill No.",
                            "Customer",
                            "Vehicle No.",
                            "Fuel",
                            "Quantity (L)",
                            "Total (₹)",
                            "Payment",
                            "Date & Time"
                    }, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public BillHistoryUI() {

        setTitle("Fuel Bill History");
        setSize(1050, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> loadBills());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadBills();

        setVisible(true);
    }

    private void loadBills() {

        try {

            List<Object[]> bills = billService.getAllBills();

            model.setRowCount(0);

            for (Object[] bill : bills) {
                model.addRow(bill);
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load bill history.\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
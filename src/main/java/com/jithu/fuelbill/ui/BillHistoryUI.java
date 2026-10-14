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

    private final JTextField searchField = new JTextField(20);

    public BillHistoryUI() {

        setTitle("Fuel Bill History");
        setSize(1050, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        // Search button action
        searchButton.addActionListener(e ->
                searchBills(searchField.getText())
        );

        // Press Enter to search
        searchField.addActionListener(e ->
                searchBills(searchField.getText())
        );

        // Refresh button action
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadBills();
        });

        // Top panel
        JPanel topPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        topPanel.add(new JLabel("Bill No / Customer:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        // Add components to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load all bills when opening the window
        loadBills();

        setVisible(true);
    }

    // Load all bills
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

    // Search bills by bill number or customer name
    private void searchBills(String keyword) {

        if (keyword.trim().isEmpty()) {
            loadBills();
            return;
        }

        try {

            List<Object[]> bills =
                    billService.searchBills(keyword);

            model.setRowCount(0);

            for (Object[] bill : bills) {
                model.addRow(bill);
            }

            if (bills.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No matching bills found.",
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to search bills:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
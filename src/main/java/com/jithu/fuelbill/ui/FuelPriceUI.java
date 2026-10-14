package com.jithu.fuelbill.ui;

import com.jithu.fuelbill.service.FuelPriceService;
import com.jithu.fuelbill.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FuelPriceUI extends JFrame {

    private final JComboBox<String> fuelBox;
    private final JLabel currentPriceLabel;
    private final JTextField newPriceField;
    private final JLabel statusLabel;

    private final FuelPriceService fuelPriceService =
            new FuelPriceService();

    public FuelPriceUI() {

        setTitle("Fuel Price Management");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("FUEL PRICE MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        fuelBox = new JComboBox<>(new String[]{"Petrol", "Diesel"});

        currentPriceLabel = new JLabel("Loading...");
        newPriceField = new JTextField();
        statusLabel = new JLabel(" ");

        JButton updateButton = new JButton("Update Price");

        panel.add(titleLabel);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Select Fuel:"));
        panel.add(fuelBox);

        panel.add(new JLabel("Current Price:"));
        panel.add(currentPriceLabel);

        panel.add(new JLabel("New Price (₹/litre):"));
        panel.add(newPriceField);

        panel.add(updateButton);
        panel.add(statusLabel);

        add(panel);

        fuelBox.addActionListener(e -> loadCurrentPrice());
        updateButton.addActionListener(e -> updatePrice());

        loadCurrentPrice();
        setVisible(true);
    }

    private void loadCurrentPrice() {

        try {
            String fuelName = (String) fuelBox.getSelectedItem();

            BigDecimal price =
                    fuelPriceService.getFuelPrice(fuelName);

            currentPriceLabel.setText("₹" + price + " / litre");

        } catch (SQLException e) {
            currentPriceLabel.setText("Unavailable");
            statusLabel.setText("Could not load price.");
            e.printStackTrace();
        }
    }

    private void updatePrice() {

        String fuelName = (String) fuelBox.getSelectedItem();
        String enteredPrice = newPriceField.getText().trim();

        try {
            BigDecimal price = new BigDecimal(enteredPrice);

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                statusLabel.setText("Enter a price above zero.");
                return;
            }

            String sql = """
                    UPDATE fuel_products
                    SET price_per_litre = ?
                    WHERE fuel_name = ?
                    """;

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setBigDecimal(1, price);
                ps.setString(2, fuelName);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    statusLabel.setText("Price updated successfully!");
                    loadCurrentPrice();
                    newPriceField.setText("");
                } else {
                    statusLabel.setText("Fuel record not found.");
                }
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Enter a valid numeric price.");

        } catch (SQLException e) {
            statusLabel.setText("Database update failed.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FuelPriceUI::new);
    }
}
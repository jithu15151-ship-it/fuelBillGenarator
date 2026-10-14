package com.jithu.fuelbill.ui;

import com.jithu.fuelbill.service.FuelPriceService;
import com.jithu.fuelbill.service.BillService;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BillingUI extends JFrame {
    private final BillService billService = new BillService();
    private final JTextField customerField = new JTextField();
    private final JTextField vehicleField = new JTextField();
    private final JComboBox<String> fuelBox =
            new JComboBox<>(new String[]{"Petrol", "Diesel"});
    private final JTextField priceField = new JTextField();
    private final JTextField quantityField = new JTextField();

    private final JComboBox<String> paymentBox =
            new JComboBox<>(new String[]{"Cash", "UPI", "Card"});

    private final JLabel totalLabel = new JLabel("Total: ₹0.00");

    private final FuelPriceService fuelService = new FuelPriceService();

    public BillingUI() {
        setTitle("Fuel Bill Generator");
        setSize(550, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadFuelPrice();

        setVisible(true);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("FUEL BILL GENERATOR", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        gbc.gridwidth = 1;

        addRow(panel, gbc, 1, "Customer Name:", customerField);
        addRow(panel, gbc, 2, "Vehicle Number:", vehicleField);
        addRow(panel, gbc, 3, "Fuel Type:", fuelBox);

        priceField.setEditable(false);
        addRow(panel, gbc, 4, "Price per Litre (₹):", priceField);
        addRow(panel, gbc, 5, "Quantity (Litres):", quantityField);
        addRow(panel, gbc, 6, "Payment Method:", paymentBox);

        JButton calculateButton = new JButton("Calculate Total");
        JButton generateButton = new JButton("Generate Bill");

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(calculateButton, gbc);

        gbc.gridx = 1;
        panel.add(generateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;

        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(totalLabel, gbc);

        add(panel);

        fuelBox.addActionListener(e -> loadFuelPrice());
        calculateButton.addActionListener(e -> calculateTotal());
        generateButton.addActionListener(e -> generateBill());
    }

    private void addRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            Component component) {

        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void loadFuelPrice() {
        String fuel = (String) fuelBox.getSelectedItem();

        try {
            BigDecimal price = fuelService.getFuelPrice(fuel);
            priceField.setText(price.toPlainString());
            calculateTotal();
        } catch (SQLException ex) {
            priceField.setText("");
            totalLabel.setText("Total: ₹0.00");

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load the fuel price.\n"
                            + "Check that MySQL is running and the fuel_products "
                            + "table contains " + fuel + ".\n\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private BigDecimal calculateAmount() {
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (priceText.isEmpty() || quantityText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Enter the quantity and make sure the fuel price is loaded."
            );
        }

        BigDecimal price = new BigDecimal(priceText);
        BigDecimal quantity = new BigDecimal(quantityText);

        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        return price.multiply(quantity)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void calculateTotal() {
        try {
            BigDecimal amount = calculateAmount();
            totalLabel.setText("Total: ₹" + amount.toPlainString());
        } catch (IllegalArgumentException ex) {
    totalLabel.setText("Total: ₹0.00");
}
    }

    private void generateBill() {
        String customer = customerField.getText().trim();
        String vehicle = vehicleField.getText().trim();

        if (customer.isEmpty() || vehicle.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the customer name and vehicle number."
            );
            return;
        }

        try {
            String fuel = (String) fuelBox.getSelectedItem();
            String payment = (String) paymentBox.getSelectedItem();

            BigDecimal price = new BigDecimal(priceField.getText().trim());
            BigDecimal quantity = new BigDecimal(
                    quantityField.getText().trim()
            );
            BigDecimal total = calculateAmount();

            String billNumber = "FB" + System.currentTimeMillis();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

            String dateTime = now.format(formatter);

            String bill = """
                    ====================================
                          FUEL BILL
                    ====================================
                    Bill No       : %s
                    Date & Time   : %s
                    ------------------------------------
                    Customer      : %s
                    Vehicle No    : %s
                    Fuel Type     : %s
                    Price/Litre   : ₹%s
                    Quantity      : %s L
                    ------------------------------------
                    TOTAL AMOUNT  : ₹%s
                    Payment       : %s
                    ====================================
                    Thank you for your purchase!
                    ====================================
                    """.formatted(
                    billNumber,
                    dateTime,
                    customer,
                    vehicle,
                    fuel,
                    price.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    quantity.stripTrailingZeros().toPlainString(),
                    total.toPlainString(),
                    payment
            );

            JTextArea billArea = new JTextArea(bill, 20, 35);
            billArea.setEditable(false);
            billArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(billArea),
                    "Generated Fuel Bill",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid number for quantity.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
package com.jithu.fuelbill.service;

import com.jithu.fuelbill.config.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillService {

    public void saveBill(
            String billNumber,
            String customerName,
            String vehicleNumber,
            String fuelName,
            BigDecimal pricePerLitre,
            BigDecimal quantity,
            BigDecimal totalAmount,
            String paymentMethod
    ) throws SQLException {

        String sql = """
                INSERT INTO bills
                (bill_number, customer_name, vehicle_number,
                 fuel_name, price_per_litre, quantity,
                 total_amount, payment_method)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, billNumber);
            ps.setString(2, customerName);
            ps.setString(3, vehicleNumber);
            ps.setString(4, fuelName);
            ps.setBigDecimal(5, pricePerLitre);
            ps.setBigDecimal(6, quantity);
            ps.setBigDecimal(7, totalAmount);
            ps.setString(8, paymentMethod);

            ps.executeUpdate();
        }
    }
}
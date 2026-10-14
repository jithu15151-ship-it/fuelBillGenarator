package com.jithu.fuelbill.service;

import com.jithu.fuelbill.config.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuelPriceService {

    public BigDecimal getFuelPrice(String fuelName)
            throws SQLException {

        String sql = """
                SELECT price_per_litre
                FROM fuel_products
                WHERE fuel_name = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fuelName);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getBigDecimal("price_per_litre");
                }

                throw new SQLException("Fuel not found: " + fuelName);
            }
        }
    }
}
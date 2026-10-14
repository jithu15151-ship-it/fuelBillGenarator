package com.jithu.fuelbill;

import com.jithu.fuelbill.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;

public class FuelPriceTest {

    public static void main(String[] args) {

        String sql = """
                UPDATE fuel_products
                SET price_per_litre = ?
                WHERE fuel_name = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Update Petrol price
            ps.setBigDecimal(1, new BigDecimal("108.39"));
            ps.setString(2, "Petrol");
            ps.executeUpdate();

            // Update Diesel price
            ps.setBigDecimal(1, new BigDecimal("100.21"));
            ps.setString(2, "Diesel");
            ps.executeUpdate();

            System.out.println("Fuel prices updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
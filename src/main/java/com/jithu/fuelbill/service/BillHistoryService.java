package com.jithu.fuelbill.service;

import com.jithu.fuelbill.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillHistoryService {

    public List<Object[]> getAllBills() throws SQLException {

        List<Object[]> bills = new ArrayList<>();

        String sql = """
                SELECT bill_number, customer_name, vehicle_number,
                       fuel_name, quantity, total_amount,
                       payment_method, bill_date
                FROM bills
                ORDER BY bill_id DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Object[] bill = {
                        rs.getString("bill_number"),
                        rs.getString("customer_name"),
                        rs.getString("vehicle_number"),
                        rs.getString("fuel_name"),
                        rs.getBigDecimal("quantity"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("bill_date")
                };

                bills.add(bill);
            }
        }

        return bills;
    }
    public List<Object[]> searchBills(String keyword) throws SQLException {

    List<Object[]> bills = new ArrayList<>();

    String sql = """
            SELECT bill_number, customer_name, vehicle_number,
                   fuel_name, quantity, total_amount,
                   payment_method, bill_date
            FROM bills
            WHERE bill_number LIKE ?
               OR customer_name LIKE ?
            ORDER BY bill_id DESC
            """;

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        String searchValue = "%" + keyword.trim() + "%";

        ps.setString(1, searchValue);
        ps.setString(2, searchValue);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Object[] bill = {
                        rs.getString("bill_number"),
                        rs.getString("customer_name"),
                        rs.getString("vehicle_number"),
                        rs.getString("fuel_name"),
                        rs.getBigDecimal("quantity"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("bill_date")
                };

                bills.add(bill);
            }
        }
    }

    return bills;
}
}
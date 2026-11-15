package com.happydentalhub.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.happydentalhub.model.Appointment;
import com.happydentalhub.util.DBConnection;

public class AppointmentDAO {

    private static final String INSERT_SQL =
        "INSERT INTO appointments (full_name, phone, email, service_type, " +
        "preferred_date, preferred_time, message) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public boolean save(Appointment a) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, a.getFullName());
            ps.setString(2, a.getPhone());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getServiceType());
            ps.setDate(5, Date.valueOf(a.getPreferredDate()));
            ps.setString(6, a.getPreferredTime());
            ps.setString(7, a.getMessage());
            return ps.executeUpdate() > 0; // auto-commit is on by default
        }
    }
}
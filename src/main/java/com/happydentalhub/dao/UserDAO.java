package com.happydentalhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.happydentalhub.model.User;
import com.happydentalhub.util.DBConnection;

public class UserDAO {

    /** Returns false if the email is already registered. */
    public boolean create(User u) throws SQLException {
        String sql = "INSERT INTO app_users (full_name, email, phone, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getPasswordHash());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001 unique constraint (email)
                return false;
            }
            throw e;
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, full_name, email, phone, password_hash FROM app_users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    return u;
                }
            }
        }
        return null;
    }
}
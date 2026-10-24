package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String DB_URL = "jdbc:sqlite:Ebike_list.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS Users (
                Id       INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE,
                Password TEXT NOT NULL,
                Roles    TEXT NOT NULL,
                Status   TEXT NOT NULL DEFAULT 'PENDING'
            )
            """;
        String defaultAdmin = """
            INSERT OR IGNORE INTO Users (Username, Password, Roles, Status)
            VALUES (?, ?, ?, ?)
            """;
        String migrateOld = "UPDATE Users SET Status = ? WHERE Status = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(createTable)) {
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement(defaultAdmin)) {
                ps.setString(1, "admin");
                ps.setString(2, "admin123");
                ps.setString(3, "ADMIN");
                ps.setString(4, "APPROVED");
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(migrateOld)) {
                ps.setString(1, "APPROVED");
                ps.setString(2, "ACTIVE");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
        }
    }
}
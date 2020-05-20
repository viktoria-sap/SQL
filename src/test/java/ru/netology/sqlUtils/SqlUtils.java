package ru.netology.sqlUtils;

import lombok.val;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlUtils {
    public static Connection getConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql:/localhost:3306/app", "app", "pass");
        return connection;
    }

    public static String getVerificationCode() throws SQLException {
        String userId = null;
        val dataSQL = "SELECT id FROM users WHERE login = ?;";
        try (val conn = getConnection();
             val idStmt = conn.prepareStatement(dataSQL);
        ) {
            idStmt.setString(1, "vasya");
            try (val rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getString("id");
                }
            }
        }
        String code = null;
        val authCode = "SELECT code FROM auth_codes WHERE user_id = ?;";
        try (val conn = getConnection();
             val codeStmt = conn.prepareStatement(authCode);
        ) {
            codeStmt.setString(1, userId);
            try (val rs = codeStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        }
        return code;
    }

    public String getStatusFromDb() throws SQLException {
        String statusSQL = "SELECT status FROM users WHERE login = ?;";
        String status = null;
        try (val conn = getConnection();
             val statusStmt = conn.prepareStatement(statusSQL);) {
            statusStmt.setString(1, "vasya");
            try (val rs = statusStmt.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        }
        return status;
    }

    public static void cleanDb() throws SQLException {
        String deleteCards = "DELETE FROM cards; ";
        String deleteAuthCodes = "DELETE FROM auth_codes; ";
        String deleteUsers = "DELETE FROM users; ";
        try (val conn = SqlUtils.getConnection();
             val deleteCardsStmt = conn.createStatement();
             val deleteAuthCodesStmt = conn.createStatement();
             val deleteUsersStmt = conn.createStatement();
        ) {
            deleteCardsStmt.executeUpdate(deleteCards);
            deleteAuthCodesStmt.executeUpdate(deleteAuthCodes);
            deleteUsersStmt.executeUpdate(deleteUsers);
        }
    }
}

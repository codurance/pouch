package io.codurance.pouch;

import java.sql.*;

class DatabaseHelper {

    private final Connection connection;

    DatabaseHelper(String jdbcUrl) {
        try {
            connection = DriverManager.getConnection(jdbcUrl, "postgres", "");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    void clearResources() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM RESOURCE");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    void insertResource(int id, String added, String title, String url) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO RESOURCE (ID, ADDED, TITLE, URL) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(added));
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, url);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}

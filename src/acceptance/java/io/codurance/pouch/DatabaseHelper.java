package io.codurance.pouch;

import java.sql.*;

class DatabaseHelper {

    private final Connection connection;

    static final class Resource {

        private final int id;
        private final String added;
        private final String title;
        private final String url;

        Resource(Integer id, String added, String title, String url) {
            this.id = id;
            this.added = added;
            this.title = title;
            this.url = url;
        }

        public Integer getId() {
            return id;
        }

        public String getAdded() {
            return added;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

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

    void insertResource(Resource resource) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO RESOURCE (ID, ADDED, TITLE, URL) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, resource.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(resource.getAdded()));
            preparedStatement.setString(3, resource.getTitle());
            preparedStatement.setString(4, resource.getUrl());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}

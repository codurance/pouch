package io.codurance.pouch;

import java.sql.*;
import java.time.Instant;
import java.util.UUID;

class DatabaseHelper {

    private final Connection connection;

    static final class Resource {

        private final UUID id;
        private final Instant added;
        private final String title;
        private final String url;
        private boolean favourite;

        Resource(UUID id, Instant added, String title, String url) {
            this.id = id;
            this.added = added;
            this.title = title;
            this.url = url;
            this.favourite = false;
        }

        public UUID getId() {
            return id;
        }

        public Instant getAdded() {
            return added;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public boolean isFavourite() {
            return favourite;
        }

        public void setAsFavourite() {
            this.favourite = true;
        }

        public void removeFavourite() {
            this.favourite = false;
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
            String sql = "DELETE FROM RESOURCE";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    void insertResource(Resource resource) {
        String sql = "INSERT INTO RESOURCE (ID, ADDED, TITLE, URL) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, resource.getId());
            preparedStatement.setTimestamp(2, Timestamp.from(resource.getAdded()));
            preparedStatement.setString(3, resource.getTitle());
            preparedStatement.setString(4, resource.getUrl());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}

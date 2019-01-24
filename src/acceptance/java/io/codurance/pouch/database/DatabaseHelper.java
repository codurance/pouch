package io.codurance.pouch.database;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DatabaseHelper {

    private final Connection connection;

    public static final class Resource {

        private UUID id;
        private Instant added;
        private String title;
        private String url;
        private boolean favourite;

        public Resource() {
            // default constructor needed for JSON deserialization
        }

        public Resource(UUID id, Instant added, String title, String url) {
            this(id, added, title, url, false);
        }

        public Resource(UUID id, Instant added, String title, String url, boolean favourite) {
            this.id = id;
            this.added = added;
            this.title = title;
            this.url = url;
            this.favourite = favourite;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Resource resource = (Resource) o;

            if (favourite != resource.favourite) return false;
            if (id != null ? !id.equals(resource.id) : resource.id != null) return false;
            if (added != null ? !added.equals(resource.added) : resource.added != null) return false;
            if (title != null ? !title.equals(resource.title) : resource.title != null) return false;
            return url != null ? url.equals(resource.url) : resource.url == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (added != null ? added.hashCode() : 0);
            result = 31 * result + (title != null ? title.hashCode() : 0);
            result = 31 * result + (url != null ? url.hashCode() : 0);
            result = 31 * result + (favourite ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Resource{" +
                    "id=" + id +
                    ", added=" + added +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", favourite=" + favourite +
                    '}';
        }
    }

    public DatabaseHelper(String jdbcUrl) {
        try {
            connection = DriverManager.getConnection(jdbcUrl, "postgres", "");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clearResources() {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM RESOURCE";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void insertResource(Resource resource) {
        String sql = "INSERT INTO RESOURCE (ID, ADDED, TITLE, URL, FAVOURITE) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, resource.getId());
            preparedStatement.setTimestamp(2, Timestamp.from(resource.getAdded()));
            preparedStatement.setString(3, resource.getTitle());
            preparedStatement.setString(4, resource.getUrl());
            preparedStatement.setBoolean(5, resource.isFavourite());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Resource> readResource(UUID uuid) {
        String sql = "SELECT ID, ADDED, TITLE, URL, FAVOURITE FROM RESOURCE WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, uuid);

            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Resource(
                            (UUID) rs.getObject("id"),
                            rs.getTimestamp("added").toInstant(),
                            rs.getString("title"),
                            rs.getString("url"),
                            rs.getBoolean("favourite")));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}

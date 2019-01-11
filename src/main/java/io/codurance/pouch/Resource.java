package io.codurance.pouch;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

public class Resource implements Persistable {

    @Id
    private UUID id;

    private Instant added;

    private String title;
    private String url;

    public Resource() {
        // default constructor needed for JSON deserialization
    }

    Resource(UUID id, Instant added, String title, String url) {
        this.id = id;
        this.added = added;
        this.title = title;
        this.url = url;
    }

    public static Resource of(ResourceDTO resourceDTO) {
        return new Resource(UUID.randomUUID(), Instant.now(), resourceDTO.getTitle(), resourceDTO.getUrl());
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

    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

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
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", added=" + added +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

package io.codurance.pouch.api;

import io.codurance.pouch.domain.Resource;

import java.time.Instant;
import java.util.UUID;

public class ResourceResponseDTO {

    private UUID id;
    private Instant added;
    private String title;
    private String url;
    private boolean favourite;

    public ResourceResponseDTO() {
        // default constructor needed for JSON deserialization
    }

    public ResourceResponseDTO(UUID id, Instant added, String title, String url, boolean favourite) {
        this.id = id;
        this.added = added;
        this.title = title;
        this.url = url;
        this.favourite = favourite;
    }

    public static ResourceResponseDTO createResponseDTOFrom(Resource resource) {
        return new ResourceResponseDTO(resource.getId(), resource.getAdded(), resource.getTitle(), resource.getUrl(), resource.isFavourite());
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

        ResourceResponseDTO that = (ResourceResponseDTO) o;

        if (favourite != that.favourite) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (added != null ? !added.equals(that.added) : that.added != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
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
        return "ResourceResponseDTO{" +
                "id=" + id +
                ", added=" + added +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", favourite=" + favourite +
                '}';
    }
}

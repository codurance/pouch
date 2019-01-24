package io.codurance.pouch.domain;

import java.time.Instant;
import java.util.UUID;

public class Resource extends Entity {

    private Instant added;
    private String title;
    private String url;
    private boolean favourite;

    public Resource() {
        // default constructor needed for JSON deserialization
        super();
    }

    public Resource(UUID id, Instant added, String title, String url) {
        super(id);
        this.added = added;
        this.title = title;
        this.url = url;
        this.favourite = false;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAsFavourite() {
        this.favourite = true;
    }

    public void removeFavourite() {
        this.favourite = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (favourite != resource.favourite) return false;
        if (added != null ? !added.equals(resource.added) : resource.added != null) return false;
        if (title != null ? !title.equals(resource.title) : resource.title != null) return false;
        return url != null ? url.equals(resource.url) : resource.url == null;
    }

    @Override
    public int hashCode() {
        int result = added != null ? added.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (favourite ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "added=" + added +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", favourite=" + favourite +
                '}';
    }
}

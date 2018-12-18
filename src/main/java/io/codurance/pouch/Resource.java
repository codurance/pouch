package io.codurance.pouch;

import org.springframework.data.annotation.Id;

public class Resource {

    @Id
    private final Integer id;
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
}

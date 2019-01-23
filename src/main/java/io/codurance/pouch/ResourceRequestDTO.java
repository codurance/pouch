package io.codurance.pouch;

public class ResourceRequestDTO {

    private String title;
    private String url;

    public ResourceRequestDTO() {
        // default constructor needed for JSON deserialization
    }

    public ResourceRequestDTO(String title, String url) {
        this.title = title;
        this.url = url;
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

        ResourceRequestDTO that = (ResourceRequestDTO) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResourceRequestDTO{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

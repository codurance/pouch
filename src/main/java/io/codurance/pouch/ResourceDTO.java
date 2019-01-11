package io.codurance.pouch;

public class ResourceDTO {

    private String title;
    private String url;

    public ResourceDTO() {
        // default constructor needed for JSON deserialization
    }

    public ResourceDTO(String title, String url) {
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

        ResourceDTO that = (ResourceDTO) o;

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
        return "ResourceDTO{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

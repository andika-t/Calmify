package calmifyStudio.model;

import java.util.Objects;

public class Content {
    private String id;
    private String title;
    private String description;
    private String contentLink;

    public Content() {
    }

    public Content(String id, String title, String description, String contentLink) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentLink = contentLink;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Content content = (Content) o;
        return Objects.equals(id, content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", contentLink='" + contentLink + '\'' +
                '}';
    }
}

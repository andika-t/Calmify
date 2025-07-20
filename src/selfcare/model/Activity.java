package selfCare.model;

import java.io.Serializable;

public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    private int defaultPoints;

    public Activity() {
    }

    public Activity(String id, String name, String description, int defaultPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultPoints = defaultPoints;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDefaultPoints() {
        return defaultPoints;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDefaultPoints(int defaultPoints) {
        this.defaultPoints = defaultPoints;
    }

    @Override
    public String toString() {
        return name;
    }
}
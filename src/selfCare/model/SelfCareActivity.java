package selfCare.model;

import java.io.Serializable;

public class SelfCareActivity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    private int defaultPoints;

    public SelfCareActivity() {}

    public SelfCareActivity(String id, String name, String description, int defaultPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultPoints = defaultPoints;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDefaultPoints() { return defaultPoints; }
    
    @Override
    public String toString() {
        return name; // Penting untuk ComboBox
    }
}
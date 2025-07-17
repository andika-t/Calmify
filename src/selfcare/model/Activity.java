package selfcare.model;

public class Activity {
    private String id;
    private String name;
    private int defaultPoints;

    public Activity(String id, String name, int defaultPoints) {
        this.id = id;
        this.name = name;
        this.defaultPoints = defaultPoints;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDefaultPoints() {
        return defaultPoints;
    }
}
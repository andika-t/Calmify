package selfCare.model;

public class SelfCareActivity {
    private String name;
    private int points;
    public SelfCareActivity(String name, int points) { this.name = name; this.points = points; }
    public String getName() { return name; }
    public int getPoints() { return points; }
    @Override public String toString() { return name + " (" + points + " poin)"; }
}
package selfcare.model;

import java.util.Objects;

public class User {
    private String id;
    private String name;
    private int totalPoints;
    private String currentLevel;

    public User(String id, String name, int totalPoints, String currentLevel) {
        this.id = id;
        this.name = name;
        this.totalPoints = totalPoints;
        this.currentLevel = currentLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
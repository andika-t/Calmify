// src/Selfcare/model/User.java
package selfCare.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int totalPoints;
    private String currentLevelOrBadge;
    private List<PointHistoryEntry> pointHistory = new ArrayList<>();
    private List<AssignedMission> assignedMissions = new ArrayList<>();

    public User() {
    }

    public User(String id, String name) {
        this();
        this.id = id;
        this.name = name;
        this.totalPoints = 0;
        this.currentLevelOrBadge = "Newbie";
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

    public String getCurrentLevelOrBadge() {
        return currentLevelOrBadge;
    }

    public List<PointHistoryEntry> getPointHistory() {
        if (pointHistory == null) {
            pointHistory = new ArrayList<>();
        }
        return pointHistory;
    }

    public List<AssignedMission> getAssignedMissions() {
        if (assignedMissions == null) {
            assignedMissions = new ArrayList<>();
        }
        return assignedMissions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setCurrentLevelOrBadge(String currentLevelOrBadge) {
        this.currentLevelOrBadge = currentLevelOrBadge;
    }

    public void setPointHistory(List<PointHistoryEntry> pointHistory) {
        this.pointHistory = pointHistory;
    }

    public void setAssignedMissions(List<AssignedMission> assignedMissions) {
        this.assignedMissions = assignedMissions;
    }

    public void addPoints(int pointsToAdd, String activityName) {
        this.totalPoints += pointsToAdd;
        getPointHistory().add(new PointHistoryEntry(activityName, pointsToAdd, System.currentTimeMillis()));
    }

    public boolean updateLevelOrBadge() {
        String oldLevel = this.currentLevelOrBadge;
        if (totalPoints >= 500) {
            this.currentLevelOrBadge = "Master Healer";
        } else if (totalPoints >= 200) {
            this.currentLevelOrBadge = "Advanced Caretaker";
        } else if (totalPoints >= 50) {
            this.currentLevelOrBadge = "Self-Care Enthusiast";
        } else {
            this.currentLevelOrBadge = "Newbie";
        }
        return !oldLevel.equals(this.currentLevelOrBadge);
    }

    public void addAssignedMission(AssignedMission mission) {
        if (getAssignedMissions().stream().noneMatch(m -> m.getId().equals(mission.getId()))) {
            getAssignedMissions().add(mission);
        }
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

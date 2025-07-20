package selfCare.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelfCareUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String name;
    private int totalPoints;
    private String currentLevelOrBadge;
    private boolean shareData;
    private List<SelfCarePointHistory> pointHistory = new ArrayList<>();
    private List<SelfCareMission> assignedMissions = new ArrayList<>();

    public SelfCareUser() {}

    public SelfCareUser(String username, String name) {
        this.username = username;
        this.name = name;
        this.totalPoints = 0;
        this.currentLevelOrBadge = "Newbie";
        this.shareData = false;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalPoints() { return totalPoints; }
    public String getCurrentLevelOrBadge() { return currentLevelOrBadge; }
    public List<SelfCarePointHistory> getPointHistory() { return pointHistory; }
    public List<SelfCareMission> getAssignedMissions() { return assignedMissions; }
    public boolean isShareData() { return shareData; }
    public void setShareData(boolean shareData) { this.shareData = shareData; }

    public void addPoints(int points, String activity) {
        this.totalPoints += points;
        this.pointHistory.add(new SelfCarePointHistory(activity, points, System.currentTimeMillis()));
        updateLevelOrBadge();
    }

    public void updateLevelOrBadge() {
        if (totalPoints >= 200) this.currentLevelOrBadge = "Master";
        else if (totalPoints >= 50) this.currentLevelOrBadge = "Enthusiast";
        else this.currentLevelOrBadge = "Newbie";
    }

    public void addAssignedMission(SelfCareMission mission) {
        if (this.assignedMissions.stream().noneMatch(m -> m.getId().equals(mission.getId()))) {
            this.assignedMissions.add(mission);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(username, ((SelfCareUser) o).username);
    }

    @Override
    public int hashCode() { return Objects.hash(username); }
    
    @Override
    public String toString() { return name; }
}
package selfCare.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("selfCareUser")
public class SelfCareUser {
    private String username;
    private String name;
    private String userType;
    private int totalPoints;
    private boolean shareData;

    @XStreamImplicit
    private List<PointHistory> pointHistory = new ArrayList<>();

    @XStreamImplicit
    private List<AssignedMission> assignedMissions = new ArrayList<>();

    public SelfCareUser() {}

    public SelfCareUser(String username, String name, String userType) {
        this.username = username;
        this.name = name;
        this.userType = userType;
        this.totalPoints = 0;
        this.shareData = false;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getUserType() { return userType; }
    public int getTotalPoints() { return totalPoints; }
    public boolean isShareData() { return shareData; }
    public void setShareData(boolean shareData) { this.shareData = shareData; }
    public List<PointHistory> getPointHistory() { return pointHistory; }
    public List<AssignedMission> getAssignedMissions() { return assignedMissions; }
    public void addPoints(int points) { this.totalPoints += points; }
    public String getLevel() {
        if (totalPoints < 100) return "Newbie";
        if (totalPoints < 500) return "Enthusiast";
        if (totalPoints < 1000) return "Master";
        return "Guru";
    }
}
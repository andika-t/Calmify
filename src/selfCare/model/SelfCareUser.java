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

    public SelfCareUser() {
        if (this.pointHistory == null) {
            this.pointHistory = new ArrayList<>();
        }
        if (this.assignedMissions == null) {
            this.assignedMissions = new ArrayList<>();
        }
    }

    public SelfCareUser(String username, String name, String userType) {
        this.username = username;
        this.name = name;
        this.userType = userType;
        this.totalPoints = 0;
        this.shareData = false;
        this.pointHistory = new ArrayList<>();
        this.assignedMissions = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getUserType() {
        return userType;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public boolean isShareData() {
        return shareData;
    }

    public void setShareData(boolean shareData) {
        this.shareData = shareData;
    }

    public List<PointHistory> getPointHistory() {
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

    public void addPoints(int points) {
        this.totalPoints += points;
    }

    public String getLevel() {
        if (totalPoints < 100)
            return "Newbie";
        if (totalPoints < 500)
            return "Enthusiast";
        if (totalPoints < 1000)
            return "Master";
        return "Guru";
    }

    public void setAssignedMissions(List<AssignedMission> assignedMissions) {
        this.assignedMissions = assignedMissions;
    }
    
}
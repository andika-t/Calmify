// src/Selfcare/model/PointHistoryEntry.java
package selfCare.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PointHistoryEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String activityName;
    private int pointsGained;
    private long timestamp;

    public PointHistoryEntry() {
    }

    public PointHistoryEntry(String activityName, int pointsGained, long timestamp) {
        this.activityName = activityName;
        this.pointsGained = pointsGained;
        this.timestamp = timestamp;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getPointsGained() {
        return pointsGained;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setPointsGained(int pointsGained) {
        this.pointsGained = pointsGained;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Aktivitas: " + activityName + ", Poin: " + pointsGained + ", Waktu: " + getFormattedTimestamp();
    }
}

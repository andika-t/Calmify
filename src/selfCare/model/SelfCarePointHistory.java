package selfCare.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfCarePointHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String activityName;
    private int pointsGained;
    private long timestamp;

    public SelfCarePointHistory() {}

    public SelfCarePointHistory(String activityName, int pointsGained, long timestamp) {
        this.activityName = activityName;
        this.pointsGained = pointsGained;
        this.timestamp = timestamp;
    }

    public String getActivityName() { return activityName; }
    public int getPointsGained() { return pointsGained; }
    public long getTimestamp() { return timestamp; }

    public String getFormattedTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }
}
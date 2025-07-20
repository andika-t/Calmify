package selfCare.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PointHistory {
    private String activityName;
    private int points;
    private LocalDateTime timestamp;
    public PointHistory(String activityName, int points) { this.activityName = activityName; this.points = points; this.timestamp = LocalDateTime.now(); }
    public String getActivityName() { return activityName; }
    public int getPoints() { return points; }
    public String getFormattedTimestamp() { return timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")); }
}
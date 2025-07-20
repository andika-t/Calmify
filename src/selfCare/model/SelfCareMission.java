package selfCare.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class SelfCareMission implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String activityId;
    private String activityName;
    private long assignedDate;
    private boolean isCompleted;
    private long completionDate;

    public SelfCareMission() {
        this.id = UUID.randomUUID().toString();
        this.isCompleted = false;
    }

    public SelfCareMission(String activityId, String activityName) {
        this();
        this.activityId = activityId;
        this.activityName = activityName;
        this.assignedDate = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getActivityId() { return activityId; }
    public String getActivityName() { return activityName; }
    public long getAssignedDate() { return assignedDate; }
    public boolean isCompleted() { return isCompleted; }

    public String getFormattedAssignedDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(assignedDate));
    }

    public String getFormattedCompletionDate() {
        return isCompleted ? new SimpleDateFormat("yyyy-MM-dd").format(new Date(completionDate)) : "-";
    }

    public void markAsCompleted() {
        if (!this.isCompleted) {
            this.isCompleted = true;
            this.completionDate = System.currentTimeMillis();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelfCareMission that = (SelfCareMission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
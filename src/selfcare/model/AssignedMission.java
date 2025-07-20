package selfCare.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AssignedMission implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String activityId;
    private String activityName;
    private long assignedDate;
    private boolean isCompleted;
    private long completionDate;

    public AssignedMission() {
        this.id = UUID.randomUUID().toString();
        this.isCompleted = false;
        this.completionDate = 0;
    }

    public AssignedMission(String activityId, String activityName) {
        this();
        this.activityId = activityId;
        this.activityName = activityName;
        this.assignedDate = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public long getAssignedDate() {
        return assignedDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public long getCompletionDate() {
        return completionDate;
    }

    public String getFormattedAssignedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(assignedDate));
    }

    public String getFormattedCompletionDate() {
        if (isCompleted && completionDate > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(new Date(completionDate));
        }
        return "-";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setAssignedDate(long assignedDate) {
        this.assignedDate = assignedDate;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setCompletionDate(long completionDate) {
        this.completionDate = completionDate;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
        this.completionDate = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Misi: " + activityName + " (Ditugaskan: " + getFormattedAssignedDate() + ", Selesai: "
                + (isCompleted ? "Ya" : "Tidak") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AssignedMission that = (AssignedMission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

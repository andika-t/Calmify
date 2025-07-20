package selfCare.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AssignedMission {
    public enum MissionStatus { DITUGASKAN, SELESAI }
    private String missionName;
    private MissionStatus status;
    private LocalDate assignedDate;
    private LocalDate completionDate;
    public AssignedMission(String missionName) { this.missionName = missionName; this.status = MissionStatus.DITUGASKAN; this.assignedDate = LocalDate.now(); }
    public String getMissionName() { return missionName; }
    public MissionStatus getStatus() { return status; }
    public void setStatus(MissionStatus status) { this.status = status; }
    public String getFormattedAssignedDate() { return assignedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); }
    public String getFormattedCompletionDate() { return completionDate != null ? completionDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "-"; }
    public void setCompletionDate(LocalDate date) { this.completionDate = date; }
}
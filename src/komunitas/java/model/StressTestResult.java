package komunitas.java.model;

import java.time.LocalDateTime;

public class StressTestResult {
    private String id;
    private String userId;
    private int score;
    private String level;
    private LocalDateTime testDate;
    private boolean shareWithProfessionals;
    
    public StressTestResult(String id, String userId, int score, String level, 
                          LocalDateTime testDate, boolean shareWithProfessionals) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.level = level;
        this.testDate = testDate;
        this.shareWithProfessionals = shareWithProfessionals;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public LocalDateTime getTestDate() { return testDate; }
    public void setTestDate(LocalDateTime testDate) { this.testDate = testDate; }
    public boolean isShareWithProfessionals() { return shareWithProfessionals; }
    public void setShareWithProfessionals(boolean shareWithProfessionals) { 
        this.shareWithProfessionals = shareWithProfessionals; 
    }
}
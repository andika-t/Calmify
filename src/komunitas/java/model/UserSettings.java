package komunitas.java.model;

public class UserSettings {
    private String userId;
    private boolean autoSaveResults;
    private boolean allowDataSharing;
    private String theme;
    
    public UserSettings(String userId, boolean autoSaveResults, 
                       boolean allowDataSharing, String theme) {
        this.userId = userId;
        this.autoSaveResults = autoSaveResults;
        this.allowDataSharing = allowDataSharing;
        this.theme = theme;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public boolean isAutoSaveResults() { return autoSaveResults; }
    public void setAutoSaveResults(boolean autoSaveResults) { this.autoSaveResults = autoSaveResults; }
    public boolean isAllowDataSharing() { return allowDataSharing; }
    public void setAllowDataSharing(boolean allowDataSharing) { this.allowDataSharing = allowDataSharing; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
}
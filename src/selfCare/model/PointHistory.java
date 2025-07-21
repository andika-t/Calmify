package selfCare.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// import com.thoughtworks.xstream.annotations.XStreamAlias; // Tambahkan ini jika Anda menggunakan XStreamAlias

// @XStreamAlias("PointHistory") // Opsional: Tambahkan ini jika Anda ingin nama tag XML menjadi <PointHistory>
public class PointHistory {
    private String activityName;
    private int points;
    private LocalDateTime timestamp;

    // --- PENTING: Tambahkan konstruktor default ini untuk XStream ---
    public PointHistory() {
        this.activityName = ""; // Inisialisasi default yang aman
        this.points = 0;
        this.timestamp = LocalDateTime.now(); // Inisialisasi default yang aman
    }

    public PointHistory(String activityName, int points) {
        // Pastikan activityName tidak pernah null
        this.activityName = (activityName != null) ? activityName : ""; 
        this.points = points;
        this.timestamp = LocalDateTime.now();
    }

    public String getActivityName() { return activityName; }
    public int getPoints() { return points; }

    public String getFormattedTimestamp() {
        // Pastikan timestamp tidak null sebelum diformat
        return (timestamp != null) ? timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "-";
    }

    // --- Opsional: Tambahkan setter untuk properti jika diubah setelah dimuat ---
    public void setActivityName(String activityName) { this.activityName = (activityName != null) ? activityName : ""; }
    public void setPoints(int points) { this.points = points; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = (timestamp != null) ? timestamp : LocalDateTime.now(); }
}
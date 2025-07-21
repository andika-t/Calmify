package komunitas.java.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StressTest {
    private String id;
    private String userId;
    private List<Question> questions;
    private List<Integer> answers;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public StressTest(String id, String userId, List<Question> questions) {
        this.id = id;
        this.userId = userId;
        this.questions = questions;
        this.answers = new ArrayList<>();
        this.startTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public void addAnswer(int answer) {
        this.answers.add(answer);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void completeTest() {
        this.endTime = LocalDateTime.now();
    }

    public long getDurationInMinutes() {
        if (endTime == null) return 0;
        return Duration.between(startTime, endTime).toMinutes();
    }
}
package komunitas.java.service;

import java.util.List;

import komunitas.java.model.Question;
import komunitas.java.model.StressTestResult;

public interface StressTestService {
    List<Question> getQuestions();
    StressTestResult calculateResult(List<Integer> answers, boolean shareWithProfessionals, String userId);
    List<StressTestResult> getUserResults(String userId);
    void deleteResult(String resultId);
    List<StressTestResult> getSharedResults();
    void saveResult(StressTestResult result);
}
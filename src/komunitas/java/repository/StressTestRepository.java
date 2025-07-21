package komunitas.java.repository;

import java.util.List;

import komunitas.java.model.StressTestResult;

public interface StressTestRepository {
    void save(StressTestResult result);
    List<StressTestResult> findByUserId(String userId);
    List<StressTestResult> findAllSharedResults();
    void update(StressTestResult result);
    void delete(String resultId);
}
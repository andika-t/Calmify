package komunitas.java.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import komunitas.java.model.Question;
import komunitas.java.model.StressTestResult;
import komunitas.java.repository.StressTestRepository;
import komunitas.java.service.StressTestService;

import java.util.ArrayList;

public class StressTestServiceImpl implements StressTestService {
    private StressTestRepository stressTestRepository;
    private List<Question> questions;

    public StressTestServiceImpl(StressTestRepository stressTestRepository) {
        this.stressTestRepository = stressTestRepository;
        initializeQuestions();
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("Q1", "Seberapa sering Anda merasa gelisah atau cemas dalam seminggu terakhir?", 1, "Anxiety"));
        questions.add(new Question("Q2", "Seberapa sering Anda mengalami kesulitan tidur dalam seminggu terakhir?", 2, "Sleep"));
        questions.add(new Question("Q3", "Seberapa sering Anda merasa lelah tanpa alasan yang jelas?", 1, "Fatigue"));
        questions.add(new Question("Q4", "Seberapa sering Anda merasa sulit berkonsentrasi?", 2, "Concentration"));
        questions.add(new Question("Q5", "Seberapa sering Anda merasa tidak bahagia atau sedih?", 3, "Mood"));
    }

    @Override
    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    @Override
    public StressTestResult calculateResult(List<Integer> answers, boolean shareWithProfessionals, String userId) {
        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException("Jumlah jawaban tidak sesuai dengan jumlah pertanyaan");
        }

        int score = 0;
        for (int i = 0; i < answers.size(); i++) {
            score += answers.get(i) * questions.get(i).getWeight();
        }

        String level = determineStressLevel(score);
        String resultId = "RES-" + System.currentTimeMillis();
        
        StressTestResult result = new StressTestResult(
            resultId, 
            userId,
            score, 
            level, 
            LocalDateTime.now(), 
            shareWithProfessionals
        );

        stressTestRepository.save(result);
        return result;
    }

    private String determineStressLevel(int score) {
        if (score <= 10) return "Rendah";
        if (score <= 20) return "Sedang";
        if (score <= 30) return "Tinggi";
        return "Sangat Tinggi";
    }

    @Override
    public List<StressTestResult> getUserResults(String userId) {
        return stressTestRepository.findByUserId(userId);
    }

    @Override
    public void deleteResult(String resultId) {
        stressTestRepository.delete(resultId);
    }

    @Override
    public List<StressTestResult> getSharedResults() {
        return stressTestRepository.findAllSharedResults();
    }

    @Override
    public void saveResult(StressTestResult result) {
        stressTestRepository.save(result);
    }

}
package komunitas.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import komunitas.java.main.AppManager;
import komunitas.java.model.Question;
import komunitas.java.model.StressTestResult;
import komunitas.java.service.AuthService;
import komunitas.java.service.StressTestService;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.List;


public class QuestionController {
    @FXML private Label questionTextLabel;
    @FXML private Label questionNumberLabel;
    @FXML private RadioButton option1;
    @FXML private RadioButton option2;
    @FXML private RadioButton option3;
    @FXML private RadioButton option4;
    @FXML private RadioButton option5;
    @FXML private Button nextButton;
    @FXML private Button prevButton;
    @FXML private CheckBox shareCheckbox;
    
    private AppManager appManager;
    private ToggleGroup optionsGroup;
    private StressTestService stressTestService;
    private AuthService authService;
    private List<Question> questions;
    private List<Integer> answers;
    private int currentQuestionIndex;
    
    public void initialize() {
        optionsGroup = new ToggleGroup();
        option1.setToggleGroup(optionsGroup);
        option2.setToggleGroup(optionsGroup);
        option3.setToggleGroup(optionsGroup);
        option4.setToggleGroup(optionsGroup);
        option5.setToggleGroup(optionsGroup);
        
        answers = new ArrayList<>();
    }
    
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    public void setServices(StressTestService stressTestService, AuthService authService) {
        this.stressTestService = stressTestService;
        this.authService = authService;
        loadQuestions();
    }
    
    private void loadQuestions() {
        try {
            questions = stressTestService.getQuestions();
            currentQuestionIndex = 0;
            answers.clear();

            for (int i = 0; i < questions.size(); i++) {
                answers.add(0);
            }
            
            showQuestion(currentQuestionIndex);
        } catch (Exception e) {
            showAlert("Error", "Gagal memuat pertanyaan: " + e.getMessage());
            appManager.showDashboard();
        }
    }
    
    private void showQuestion(int index) {
        if (index < 0 || index >= questions.size()) return;
        
        Question question = questions.get(index);
        questionNumberLabel.setText("Pertanyaan " + (index + 1) + " dari " + questions.size());
        questionTextLabel.setText(question.getText());

        optionsGroup.selectToggle(null);
        
        int currentAnswer = answers.get(index);
        if (currentAnswer > 0) {
            switch (currentAnswer) {
                case 1: optionsGroup.selectToggle(option1); break;
                case 2: optionsGroup.selectToggle(option2); break;
                case 3: optionsGroup.selectToggle(option3); break;
                case 4: optionsGroup.selectToggle(option4); break;
                case 5: optionsGroup.selectToggle(option5); break;
            }
        }
        
        prevButton.setDisable(index == 0);
        nextButton.setText(index == questions.size() - 1 ? "Selesai" : "Berikutnya");
    }
    
    private void saveCurrentAnswer() {
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected != null) {
            int answer = Integer.parseInt(selected.getText());
            answers.set(currentQuestionIndex, answer);
        } else {
            answers.set(currentQuestionIndex, 0);
        }
    }
    
    @FXML
    private void handleNext() {
        saveCurrentAnswer();
        
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        } else {
            try {
                if (answers.contains(0)) {
                    showAlert("Peringatan", "Anda belum menjawab semua pertanyaan. Silakan kembali dan lengkapi jawaban Anda.");
                    return;
                }
                
                boolean shareWithProfessionals = shareCheckbox.isSelected();
                StressTestResult result = stressTestService.calculateResult(
                    answers, 
                    shareWithProfessionals,
                    authService.getCurrentUser().getId()
                );
                
                appManager.showResultScreen(result);
            } catch (Exception e) {
                showAlert("Error", "Gagal menghitung hasil: " + e.getMessage());
                appManager.showDashboard();
            }
        }
    }
    
    @FXML
    private void handlePrev() {
        saveCurrentAnswer();
        currentQuestionIndex--;
        showQuestion(currentQuestionIndex);
    }
    
    @FXML
    private void handleCancel() {
        appManager.showDashboard();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
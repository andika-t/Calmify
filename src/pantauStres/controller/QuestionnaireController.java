package pantauStres.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import pantauStres.model.Question;
import pantauStres.services.QuestionManager;

public class QuestionnaireController implements Initializable {

    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label questionLabel;
    @FXML
    private HBox ratingBox;
    @FXML
    private Button ratingBtn1;
    @FXML
    private Button ratingBtn2;
    @FXML
    private Button ratingBtn3;
    @FXML
    private Button ratingBtn4;
    @FXML
    private Button ratingBtn5;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button finishButton;

    private QuestionManager questionManager;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    private Map<Integer, Integer> answers;
    private Map<Integer, String> descriptions;

    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.questionManager = new QuestionManager();
        this.questions = questionManager.getQuestions();
        answers = new HashMap<>();
        descriptions = new HashMap<>();

        if (questions != null && !questions.isEmpty()) {
            showQuestion(currentQuestionIndex);
        } else {
            showErrorState("Gagal memuat pertanyaan. Pastikan file 'data/questions.xml' ada dan formatnya benar.");
        }
    }

    private void showQuestion(int index) {
        questionLabel.setText(questions.get(index).getPertanyaan());

        double progress = (double) (index + 1) / questions.size();
        progressBar.setProgress(progress);
        progressLabel.setText("Pertanyaan " + (index + 1) + " dari " + questions.size());

        previousButton.setVisible(index > 0);
        nextButton.setVisible(index < questions.size() - 1);
        finishButton.setVisible(index == questions.size() - 1);

        nextButton.setDisable(!answers.containsKey(index));
        finishButton.setDisable(!answers.containsKey(index));

        clearRatingSelection();
        if (answers.containsKey(index)) {
            int score = answers.get(index);
            highlightButton(score);
        }
        descriptionArea.setText(descriptions.getOrDefault(index, ""));
    }

    private void showErrorState(String message) {
        questionLabel.setText(message);
        progressLabel.setText("Error");
        progressBar.setProgress(0);
        ratingBox.setDisable(true);
        nextButton.setDisable(true);
        previousButton.setVisible(false);
        finishButton.setVisible(false);
    }

    @FXML
    private void handleFinishButton() {
        saveCurrentDescription();
        System.out.println("=== HASIL ASSESSMENT ===");
        int totalScore = 0;
        for (int i = 0; i < questions.size(); i++) {
            int score = answers.getOrDefault(i, 0);
            String desc = descriptions.getOrDefault(i, "Tidak ada deskripsi.");

            int questionId = questions.get(i).getId();
            System.out.println("Pertanyaan ID " + questionId + ": Skor Pilihan = " + score + ", Deskripsi: " + desc);
            totalScore += score;
        }
        System.out.println("-------------------------");
        System.out.println("Total Skor Pilihan: " + totalScore);
        System.out.println("=======================");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Assessment Selesai");
        alert.setHeaderText("Terima kasih telah menyelesaikan assessment!");
        alert.setContentText("Hasil Anda telah direkam. Total skor Anda: " + totalScore);
        alert.showAndWait();

        Stage stage = (Stage) finishButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleRatingButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        int score = 0;

        if ("ratingBtn1".equals(buttonId))
            score = 1;
        else if ("ratingBtn2".equals(buttonId))
            score = 2;
        else if ("ratingBtn3".equals(buttonId))
            score = 3;
        else if ("ratingBtn4".equals(buttonId))
            score = 4;
        else if ("ratingBtn5".equals(buttonId))
            score = 5;

        answers.put(currentQuestionIndex, score);

        clearRatingSelection();
        highlightButton(score);
        nextButton.setDisable(false);
        finishButton.setDisable(false);
    }

    @FXML
    private void handleNextButton() {
        saveCurrentDescription();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        }
    }

    @FXML
    private void handlePreviousButton() {
        saveCurrentDescription();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion(currentQuestionIndex);
        }
    }

    private void saveCurrentDescription() {
        String description = descriptionArea.getText();
        if (description != null && !description.trim().isEmpty()) {
            descriptions.put(currentQuestionIndex, description);
        } else {
            descriptions.remove(currentQuestionIndex);
        }
    }

    private void highlightButton(int score) {
        getButtonByScore(score).ifPresent(button -> button.getStyleClass().add("selected-rating-button"));
    }

    private void clearRatingSelection() {
        for (Node node : ratingBox.getChildren()) {
            if (node instanceof Button) {
                node.getStyleClass().remove("selected-rating-button");
            }
        }
    }

    private Optional<Button> getButtonByScore(int score) {
        switch (score) {
            case 1:
                return java.util.Optional.of(ratingBtn1);
            case 2:
                return java.util.Optional.of(ratingBtn2);
            case 3:
                return java.util.Optional.of(ratingBtn3);
            case 4:
                return java.util.Optional.of(ratingBtn4);
            case 5:
                return java.util.Optional.of(ratingBtn5);
            default:
                return java.util.Optional.empty();
        }
    }

    public void setMainPane(BorderPane maiPane){
        this.mainPane = maiPane;
    }
}
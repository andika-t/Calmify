package pantauStres.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import authenticator.model.User;
import home.controller.MainHomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pantauStres.model.Answer;
import pantauStres.model.Question;
import pantauStres.model.Score;
import pantauStres.services.AnswerModel;
import pantauStres.services.QuestionModel;

public class QuestionnaireController {
    @FXML
    private Label progressLabel, questionLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private HBox ratingBox;
    @FXML
    private Button previousButton, nextButton, finishButton;
    @FXML
    private Button ratingBtn1, ratingBtn2, ratingBtn3, ratingBtn4, ratingBtn5;

    private final QuestionModel questionModel = new QuestionModel();
    private final AnswerModel answerModel = new AnswerModel();

    private User currentUser;
    private MainHomeController mainController;
    private BorderPane mainPane;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private final Map<Integer, Integer> userAnswers = new HashMap<>();

    public void setData(BorderPane mainPane, User currentUser, MainHomeController mainController) {
        this.mainPane = mainPane;
        this.currentUser = currentUser;
        this.mainController = mainController;
        this.questions = questionModel.getQuestions();

        if (questions != null && !questions.isEmpty()) {
            showQuestion(currentQuestionIndex);
        } else {
            showErrorState("Gagal memuat pertanyaan.");
        }
    }

    @FXML
    private void handleFinishButton() {
        int totalSkorMood = 0;
        int totalSkorTidur = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int ratingPengguna = userAnswers.getOrDefault(i, 0);
            int bobotSkor = q.getSkor();
            int skorFinal = ratingPengguna * bobotSkor;
            if ("Mood".equalsIgnoreCase(q.getKategori())) {
                totalSkorMood += skorFinal;
            } else if ("Tidur".equalsIgnoreCase(q.getKategori())) {
                totalSkorTidur += skorFinal;
            }
        }
        int totalSkorKeseluruhan = totalSkorMood + totalSkorTidur;
        String interpretasi;
        if (totalSkorKeseluruhan <= 25)
            interpretasi = Score.RENDAH.getDisplayValue();
        else if (totalSkorKeseluruhan <= 50)
            interpretasi = Score.SEDANG.getDisplayValue();
        else
            interpretasi = Score.TINGGI.getDisplayValue();

        LocalDateTime waktuTes = LocalDateTime.now();
        String idJawaban = currentUser.getUsername() + "_"
                + waktuTes.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String waktuTesString = waktuTes.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Answer newAnswer = new Answer(idJawaban, currentUser.getUsername(), waktuTesString, interpretasi, false,
                totalSkorMood, totalSkorTidur);

        answerModel.addAnswer(newAnswer);

        if (mainController != null) {
            mainController.refreshUIData();
        }

        new Alert(Alert.AlertType.INFORMATION, "Hasil asesmen Anda telah berhasil disimpan.").showAndWait();

        kembaliKeHalamanUtama();
    }

    private void kembaliKeHalamanUtama() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/UserAssessmentView.fxml"));
            Pane root = loader.load();
            UserAssessmentController controller = loader.getController();
            controller.setData(mainPane, currentUser, mainController);
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
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
        boolean answered = userAnswers.containsKey(index);
        nextButton.setDisable(!answered);
        finishButton.setDisable(!answered);
        clearRatingSelection();
        if (answered) {
            highlightButton(userAnswers.get(index));
        }
    }

    @FXML
    private void handleRatingButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        int score = 0;

        switch (buttonId) {
            case "ratingBtn1":
                score = 1;
                break;
            case "ratingBtn2":
                score = 2;
                break;
            case "ratingBtn3":
                score = 3;
                break;
            case "ratingBtn4":
                score = 4;
                break;
            case "ratingBtn5":
                score = 5;
                break;
            default:
                System.err.println("Tombol rating tidak dikenal: " + buttonId);
                return; 
        }
        userAnswers.put(currentQuestionIndex, score);
        clearRatingSelection();
        highlightButton(score);
        nextButton.setDisable(false);
        finishButton.setDisable(false);
    }

    @FXML
    private void handleNextButton() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        }
    }

    @FXML
    private void handlePreviousButton() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion(currentQuestionIndex);
        }
    }

    private void highlightButton(int score) {
        getButtonByScore(score).ifPresent(button -> button.getStyleClass().add("button-rating-selected"));
    }

    private void clearRatingSelection() {
        ratingBox.getChildren().forEach(node -> node.getStyleClass().remove("button-rating-selected"));
    }

    private Optional<Button> getButtonByScore(int score) {
        switch (score) {
            case 1:
                return Optional.of(ratingBtn1);
            case 2:
                return Optional.of(ratingBtn2);
            case 3:
                return Optional.of(ratingBtn3);
            case 4:
                return Optional.of(ratingBtn4);
            case 5:
                return Optional.of(ratingBtn5);
            default:
                return Optional.empty();
        }
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
}
package pantauStres.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import authenticator.model.User;
import home.controller.MainHomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import pantauStres.model.Answer;
import pantauStres.services.AnswerModel;

public class UserAssessmentController {

    @FXML
    private Button btnLihatRiwayat;

    private BorderPane mainPane;
    private User currentUser;
    private MainHomeController mainController;

    private final AnswerModel answerModel = new AnswerModel();

    public void setData(BorderPane mainPane, User user, MainHomeController mainController) {
        this.mainPane = mainPane;
        this.currentUser = user;
        this.mainController = mainController;

        checkHistoryAndSetButtonState();
    }

    @FXML
    private void handleMulai(ActionEvent event) {
        AnswerModel answerModel = new AnswerModel();
        List<Answer> allAnswers = answerModel.getAnswers();
        String username = currentUser.getUsername();

        Optional<Answer> lastTest = allAnswers.stream()
                .filter(answer -> username.equals(answer.getUsername()))
                .max(Comparator.comparing(Answer::getWaktuTes));

        if (lastTest.isPresent()) {
            try {
                String lastTestString = lastTest.get().getWaktuTes();
                LocalDate lastTestDate = LocalDate.parse(lastTestString.substring(0, 10));
                LocalDate todayDate = LocalDate.now();
                if (lastTestDate.isEqual(todayDate)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Tes Sudah Diambil");
                    alert.setHeaderText(null);
                    alert.setContentText("Anda sudah mengambil tes hari ini. Silakan coba lagi besok.");
                    alert.showAndWait();
                    return;
                }
            } catch (DateTimeParseException | StringIndexOutOfBoundsException e) {
                System.err.println("Format tanggal tidak valid pada data tes terakhir: " + e.getMessage());
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/QuestionnaireView.fxml"));
            Parent pane = loader.load();
            QuestionnaireController controller = loader.getController();
            controller.setData(mainPane, currentUser, mainController);
            mainPane.setCenter(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRiwayat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/HistoryView.fxml"));
            Parent historyPane = loader.load();
            HistoryController controller = loader.getController();
            controller.setData(currentUser);
            mainPane.setCenter(historyPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkHistoryAndSetButtonState() {
        if (currentUser == null)
            return;
        boolean hasHistory = answerModel.getAnswers().stream()
                .anyMatch(answer -> currentUser.getUsername().equals(answer.getUsername()));

        btnLihatRiwayat.setDisable(!hasHistory);
    }
}
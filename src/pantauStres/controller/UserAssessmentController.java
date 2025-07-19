package pantauStres.controller;

import java.io.IOException;
import authenticator.model.User;
import home.controller.MainHomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import pantauStres.services.AnswerModel;

public class UserAssessmentController {

    @FXML private Button btnLihatRiwayat;
    
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/HistoryChartView.fxml"));
            Parent historyPane = loader.load();
            // Kirim konteks ke HistoryChartController
            HistoryChartController controller = loader.getController();
            controller.setData(currentUser);
            mainPane.setCenter(historyPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkHistoryAndSetButtonState() {
        if (currentUser == null) return;
        // Baca data dari file untuk memeriksa riwayat
        boolean hasHistory = answerModel.getAnswers().stream()
                .anyMatch(answer -> currentUser.getUsername().equals(answer.getUsername()));
        
        btnLihatRiwayat.setDisable(!hasHistory);
    }
}
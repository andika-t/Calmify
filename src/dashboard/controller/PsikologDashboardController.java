package dashboard.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import authenticator.model.User;
import home.controller.MainHomeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pantauStres.model.Answer;
import pantauStres.services.AnswerModel;

public class PsikologDashboardController implements Initializable {
    @FXML
    private VBox mainPanel;
    @FXML
    private Label labelFullname;
    @FXML
    private Button btnBell;
    @FXML
    private Button btnChat;

    private User currentUser;
    private MainHomeController mainController;
    private final AnswerModel answerModel = new AnswerModel();

    public void setData(BorderPane mainPane, User user, MainHomeController mainController) {
        this.currentUser = user;
        this.mainController = mainController;

        if (this.currentUser != null) {
            labelFullname.setText(currentUser.getFullName());
        } else {
            labelFullname.setText("Pengguna Tamu");
        }
        refreshUIData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelFullname.setText("Memuat...");
        loadStudioContent();
    }

    public void refreshUIData() {
        if (currentUser == null) {
            System.err.println("Error: currentUser belum diatur. Tidak bisa refresh UI.");
            return;
        }
        List<Answer> userAnswers = answerModel.getAnswers().stream()
                .filter(answer -> currentUser.getUsername().equals(answer.getUsername()))
                .collect(Collectors.toList());
    }

    private void loadStudioContent() {
        try {
            URL studioContentUrl = getClass().getResource("/selfCare/view/SCPsychologistView.fxml");
            if (studioContentUrl == null) {
                System.err.println("FIle tidak ditemukan");
                return;
            }
            Parent studioContent = FXMLLoader.load(studioContentUrl);
            mainPanel.getChildren().add(studioContent);
        } catch (IOException e) {
            System.err.println("Gagal memuat StudioContent.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

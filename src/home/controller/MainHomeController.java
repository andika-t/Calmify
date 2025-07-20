package home.controller;

import authenticator.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pantauStres.controller.UserAssessmentController;
import pantauStres.model.Answer;
import pantauStres.services.AnswerModel;
import profile.controller.MainSettingController;
import util.SceneSwitcher;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainHomeController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private VBox panelKanan;
    @FXML
    private Label lnamaLengkap;
    @FXML
    private Label lusername;
    @FXML
    private Label ltipePengguna;
    @FXML
    private Label labelPoint;

    private User currentUser;
    private AnswerModel answerModel = new AnswerModel();
    private Node panelNavigasiKiri;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.answerModel = new AnswerModel();
    }

    public void setData(User user) {
        this.currentUser = user;

        lnamaLengkap.setText(user.getFirstName() + " " + user.getLastName());
        lusername.setText("@" + user.getUsername());
        ltipePengguna.setText(user.getUserType());

        muatPanelNavigasiSesuaiTipePengguna();
        handleHome(null);
        refreshUIData();
    }

    private void muatPanelNavigasiSesuaiTipePengguna() {
        if (currentUser == null)
            return;

        String fxmlPath = "";
        String userType = currentUser.getUserType();

        if ("Pengguna Umum".equalsIgnoreCase(userType)) {
            fxmlPath = "/home/view/UserNavigation.fxml";
        } else if ("Psikolog".equalsIgnoreCase(userType)) {
            fxmlPath = "/home/view/PsikologNavigation.fxml";
        }

        if (fxmlPath.isEmpty()) {
            mainPane.setLeft(null);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            this.panelNavigasiKiri = loader.load();

            if ("Pengguna Umum".equalsIgnoreCase(userType)) {
                UserNavigationController controller = loader.getController();
                controller.setMainController(this);
            } else if ("Psikolog".equalsIgnoreCase(userType)) {
                PsikologNavigationController controller = loader.getController();
                controller.setMainController(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHome(ActionEvent event) {
        mainPane.setLeft(panelNavigasiKiri);
        mainPane.setRight(panelKanan);
        mainPane.setCenter(null);
    }

    @FXML
    public void handlePantauStres(ActionEvent event) {
        mainPane.setLeft(panelNavigasiKiri);
        mainPane.setRight(panelKanan);

        String fxmlPath = "";
        try {
            if ("Pengguna Umum".equalsIgnoreCase(currentUser.getUserType())) {
                fxmlPath = "/pantauStres/view/UserAssessmentView.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Pane scene = loader.load();
                UserAssessmentController controller = loader.getController();
                controller.setData(mainPane, currentUser, this);
                mainPane.setCenter(scene);
            } else {
                fxmlPath = "/pantauStres/view/ManagerView.fxml";
                Pane scene = FXMLLoader.load(getClass().getResource(fxmlPath));
                mainPane.setCenter(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSelfCare(ActionEvent event) {
        if (currentUser == null) {
            System.err.println("FATAL ERROR: currentUser di MainHomeController adalah null!");
            showAlert(Alert.AlertType.ERROR, "Error Kritis",
                    "Data pengguna tidak ditemukan. Silakan coba login ulang.");
            return;
        }
        mainPane.setLeft(panelNavigasiKiri);
        mainPane.setRight(panelKanan);
        showAlert(AlertType.INFORMATION,"TIDAK TERSEDIA","Fitur dalam pengembangan");
    }

    @FXML
    public void handleCalmifyStudio(ActionEvent event) {
        mainPane.setLeft(panelNavigasiKiri);
        String fxmlPath = "Pengguna Umum".equalsIgnoreCase(currentUser.getUserType())
                ? "/calmifyStudio/view/UserView.fxml"
                : "/calmifyStudio/view/PsikologView.fxml";
        muatTampilanKeTengah(fxmlPath);
    }

    @FXML
    public void handleCommunity(ActionEvent event) {
        mainPane.setLeft(panelNavigasiKiri);
        showAlert(AlertType.INFORMATION,"TIDAK TERSEDIA","Fitur dalam pengembangan");
    }

    @FXML
    public void handleSetting(ActionEvent event) {
        mainPane.setLeft(null);
        mainPane.setRight(panelKanan);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/MainSettingView.fxml"));
            Pane pane = loader.load();
            MainSettingController settingController = loader.getController();
            settingController.setCurrentUser(this.currentUser);

            mainPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Memuat", "Tidak dapat memuat halaman Pengaturan.");
        }
    }

    @FXML
    private void handleButtonLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Anda akan keluar dari aplikasi.");
        alert.setContentText("Apakah Anda yakin?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                new SceneSwitcher().switchScene("/authenticator/view/LoginView");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void muatTampilanKeTengah(String fxmlPath) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Memuat", "Tidak dapat memuat halaman: " + fxmlPath);
        }
    }

    public void refreshUIData() {
        if (currentUser == null) {
            System.err.println("Error: currentUser belum diatur. Tidak bisa refresh UI.");
            return;
        }
        List<Answer> userAnswers = answerModel.getAnswers().stream()
                .filter(answer -> currentUser.getUsername().equals(answer.getUsername()))
                .collect(Collectors.toList());

        int totalPoin = userAnswers.stream().mapToInt(ans -> ans.getSkorMood() + ans.getSkorKualitas()).sum();
        labelPoint.setText(totalPoin + " Poin");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public BorderPane getMainPane() {
        return this.mainPane;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
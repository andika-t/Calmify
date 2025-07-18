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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pantauStres.controller.ManagerController;
import pantauStres.controller.UserAssessmentController;
import util.SceneSwitcher;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

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

    private User currentUser;
    private Node panelNavigasiKiri;

    public void setData(User user) {
        this.currentUser = user;
        lnamaLengkap.setText(user.getFirstName() + " " + user.getLastName());
        lusername.setText("@" + user.getUsername());
        ltipePengguna.setText(user.getUserType());
        muatPanelNavigasiSesuaiTipePengguna();
        handleDashboard(null);
    }

    private void muatPanelNavigasiSesuaiTipePengguna() {
        if (currentUser == null)
            return;
        String userType = currentUser.getUserType();
        String pathFxml = "";
        if ("Pengguna Umum".equalsIgnoreCase(userType)) {
            pathFxml = "/home/view/UserNavigation.fxml";
        } else if ("Psikolog".equalsIgnoreCase(userType)) {
            pathFxml = "/home/view/PsikologNavigation.fxml";
        }

        if (!pathFxml.isEmpty()) {
            muatPanelNavigasiDariPath(pathFxml);
        } else {
            mainPane.setLeft(null);
        }
    }

    private void muatPanelNavigasiDariPath(String pathKeFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathKeFxml));
            loader.setController(this);

            this.panelNavigasiKiri = loader.load();
            mainPane.setLeft(this.panelNavigasiKiri);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat panel navigasi dari: " + pathKeFxml);
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
    }

    @FXML
    private void handlePantauStres(ActionEvent event) {
        mainPane.setRight(panelKanan);
        if (currentUser == null)
            return;
        String userType = currentUser.getUserType();

        try {
            if ("Pengguna Umum".equalsIgnoreCase(userType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/UserAssessmentView.fxml"));
                Pane sceneUmum = loader.load();
                UserAssessmentController controller = loader.getController();
                controller.setMainPane(mainPane);
                mainPane.setCenter(sceneUmum);
            } else if ("Psikolog".equalsIgnoreCase(userType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/ManagerView.fxml"));
                Pane scenePsikolog = loader.load();
                ManagerController controller = loader.getController();
                controller.setMainPane(mainPane);
                mainPane.setCenter(scenePsikolog);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat FXML untuk halaman Pantau Stres.");
        }
    }

    @FXML
    private void handleSelfCare(ActionEvent event) {
    }

    @FXML
    private void handleCalmifyStudio(ActionEvent event) {
        mainPane.setRight(panelKanan);
        if (currentUser == null)
            return;
        String userType = currentUser.getUserType();

        try {
            if ("Pengguna Umum".equalsIgnoreCase(userType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/calmifyStudio/view/UserView.fxml"));
                Pane sceneUmum = loader.load();
                mainPane.setCenter(sceneUmum);
            } else if ("Psikolog".equalsIgnoreCase(userType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/calmifyStudio/view/PsikologView.fxml"));
                Pane scenePsikolog = loader.load();
                mainPane.setCenter(scenePsikolog);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat FXML untuk halaman Pantau Stres.");
        }
    }

    @FXML
    private void handleCommunity(ActionEvent event) {
        
    }

    @FXML
    private void handleHome(ActionEvent event) {
        System.out.println("Tombol Home ditekan, mengembalikan tampilan utama...");
        mainPane.setRight(panelKanan);
        mainPane.setLeft(panelNavigasiKiri);
        mainPane.setCenter(null);
    }

    @FXML
    private void handleSetting(ActionEvent event) {
        mainPane.setRight(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/MainSettingView.fxml"));
            Pane pane = loader.load();
            mainPane.setCenter(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Anda akan keluar dari aplikasi.");
        alert.setContentText("Apakah Anda yakin ingin melanjutkan?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                SceneSwitcher pindahScene = new SceneSwitcher();
                pindahScene.switchScene("/authenticator/view/loginView");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Gagal memuat halaman login.");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
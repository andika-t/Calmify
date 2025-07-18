package home.controller;

import authenticator.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import util.SceneSwitcher;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PsikologHomeController implements Initializable {

    @FXML private BorderPane mainPane;
    @FXML private Label lnamaLengkap;
    @FXML private Label lusername;
    @FXML private Button btnLogout;
    @FXML private Button btnDashboard;

    private User currentUser;
    private SceneSwitcher pindahScene;

    public void setData(User user) {
        this.currentUser = user;
        lnamaLengkap.setText(user.getFirstName() + " " + user.getLastName());
        lusername.setText("@" + user.getUsername());
        handleDashboard(null);
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
                pindahScene = new SceneSwitcher();
                pindahScene.switchScene("/authenticator/view/LoginView");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Gagal memuat halaman login.");
            }
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        try {
            pindahScene = new SceneSwitcher();
            Pane scene = pindahScene.getPane("/dashboard/view/DashboardView");
            mainPane.setCenter(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePantauStres(ActionEvent event) {
        try {
            pindahScene = new SceneSwitcher();
            Pane scene = pindahScene.getPane("/pantauStres/view/ManagerView");
            mainPane.setCenter(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelfCare(ActionEvent event) {
        try {
            pindahScene = new SceneSwitcher();
            Pane scene = pindahScene.getPane("/selfcare/MainView");
            mainPane.setCenter(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCalmifyStudio(ActionEvent event){
        try {
            pindahScene = new SceneSwitcher();
            Pane scene = pindahScene.getPane("/calmifyStudio/view/PsikologView");
            mainPane.setCenter(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCommunity(ActionEvent event){
        try {
            pindahScene = new SceneSwitcher();
            Pane scene = pindahScene.getPane("/calmifyStudio/view/UserView");
            mainPane.setCenter(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}

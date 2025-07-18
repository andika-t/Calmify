package profile.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import authenticator.model.User;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import util.SceneSwitcher;

public class MainSettingController implements Initializable {

    private static final String STYLE_CLASS_SELECTED = "button-settings-selected";

    @FXML
    private BorderPane mainPane;
    @FXML
    private Button btnSecurity;
    @FXML
    private Button btnMyAccount;


    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleMyAccount(ActionEvent event) {
        updateButtonStyles(btnMyAccount);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/ProfileView.fxml"));
            Pane pane = loader.load();
            ProfileController controller = loader.getController();
            controller.setData(currentUser);

            controller.setMainPane(mainPane);
            mainPane.setCenter(pane);
            btnMyAccount.getStyleClass().add(STYLE_CLASS_SELECTED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSecurity(ActionEvent event) {
        updateButtonStyles(btnSecurity);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/SecurityView.fxml"));
            Pane pane = loader.load();
            SecurityController controller = loader.getController();
            controller.setData(currentUser);

            controller.setMainPane(mainPane);
            mainPane.setCenter(pane);
            btnSecurity.getStyleClass().add(STYLE_CLASS_SELECTED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmasi Hapus Akun");
        confirmation.setHeaderText("Apakah Anda benar-benar yakin?");
        confirmation
                .setContentText("Tindakan ini akan menghapus akun Anda secara permanen dan tidak dapat diurungkan.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (XMLUserService.deleteUser(currentUser.getUsername())) {
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Akun Anda telah dihapus.");
                try {
                    SceneSwitcher pindahScene = new SceneSwitcher();
                    pindahScene.switchScene("/authenticator/view/LoginView");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghapus akun.");
            }
        }
    }

    private void updateButtonStyles(Button selectedButton) {
        btnMyAccount.getStyleClass().remove(STYLE_CLASS_SELECTED);
        btnSecurity.getStyleClass().remove(STYLE_CLASS_SELECTED);

        if (selectedButton.equals(btnSecurity)) {
            btnSecurity.getStyleClass().add(STYLE_CLASS_SELECTED);
        } else if (selectedButton.equals(btnMyAccount)) {
            btnMyAccount.getStyleClass().add(STYLE_CLASS_SELECTED);
        } else {
            btnMyAccount.getStyleClass().remove(STYLE_CLASS_SELECTED);
            btnSecurity.getStyleClass().remove(STYLE_CLASS_SELECTED);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
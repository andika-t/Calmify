package profile.controller;

import authenticator.model.User;
import authenticator.services.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import util.SceneSwitcher;

import java.io.IOException;
import java.util.Optional;

public class MainSettingController {

    private static final String STYLE_CLASS_SELECTED = "button-settings-selected";

    @FXML private BorderPane mainPane;
    @FXML private Button btnSecurity;
    @FXML private Button btnMyAccount;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        handleMyAccount(null); 
    }

    @FXML
    private void handleMyAccount(ActionEvent event) {
        updateButtonStyles(btnMyAccount);
        loadPaneToCenter("/profile/view/ProfileView.fxml");
    }

    @FXML
    private void handleSecurity(ActionEvent event) {
        updateButtonStyles(btnSecurity);
        loadPaneToCenter("/profile/view/SecurityView.fxml");
    }

    private void loadPaneToCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane pane = loader.load();

            if (loader.getController() instanceof ProfileController) {
                ProfileController controller = loader.getController();
                controller.setData(currentUser);
            } else if (loader.getController() instanceof SecurityController) {
                SecurityController controller = loader.getController();
                controller.setData(currentUser);
            }
            
            mainPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmasi Hapus Akun");
        confirmation.setHeaderText("Apakah Anda benar-benar yakin?");
        confirmation.setContentText("Tindakan ini akan menghapus akun Anda secara permanen.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (UserManager.deleteUser(currentUser.getUsername())) {
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Akun Anda telah dihapus.");
                try {
                    new SceneSwitcher().switchScene("/authenticator/view/LoginView");
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
        if (selectedButton != null) {
            selectedButton.getStyleClass().add(STYLE_CLASS_SELECTED);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
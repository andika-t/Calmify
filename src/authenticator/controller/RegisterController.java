package authenticator.controller;

import java.net.URL;
import java.util.ResourceBundle;

import authenticator.model.User;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.SceneSwitcher;

public class RegisterController implements Initializable {

    private static final String PENGGUNA_BIASA = "Pengguna Biasa";
    private static final String PSIKOLOG = "Psikolog";
    private static final String STYLE_SELECTED = "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-border-color: #1976D2; -fx-border-width: 1;";
    private static final String STYLE_DEFAULT = "-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-border-color: transparent;";

    @FXML private TextField tfFirstName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfUsername;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;

    @FXML private Button btnPengguna;
    @FXML private Button btnPsikolog;
    @FXML private Button buttonCreateAcc;

    private String selectedUserType;
    private SceneSwitcher pindahScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set default user type
        selectedUserType = PENGGUNA_BIASA;
        btnPengguna.setStyle(STYLE_SELECTED);
        btnPsikolog.setStyle(STYLE_DEFAULT);
    }

    @FXML
    private void selectPenggunaAction(ActionEvent event) {
        selectedUserType = PENGGUNA_BIASA;
        btnPengguna.setStyle(STYLE_SELECTED);
        btnPsikolog.setStyle(STYLE_DEFAULT);
    }

    @FXML
    private void selectPsikologAction(ActionEvent event) {
        selectedUserType = PSIKOLOG;
        btnPsikolog.setStyle(STYLE_SELECTED);
        btnPengguna.setStyle(STYLE_DEFAULT);
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        String firstName = tfFirstName.getText().trim();
        String lastName = tfLastName.getText().trim();
        String username = tfUsername.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Harap isi semua kolom yang tersedia.");
            return;
        }

        if (selectedUserType == null || selectedUserType.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Harap pilih tipe pengguna.");
            return;
        }

        User newUser = new User(firstName, lastName, username, email, password, selectedUserType);
        boolean isAdded = XMLUserService.addUser(newUser);

        if (isAdded) {
            showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun untuk '" + username + "' berhasil dibuat.");
            goToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Username '" + username + "' sudah digunakan atau terjadi kesalahan.");
        }
    }

    @FXML
    private void handleHaveAccount(ActionEvent event) {
        goToLogin();
    }

    private void goToLogin() {
        pindahScene = new SceneSwitcher();
        pindahScene.switchScene("/authenticator/view/LoginView");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

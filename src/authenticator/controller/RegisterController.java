package authenticator.controller;

import java.net.URL;
import java.util.ResourceBundle;

import authenticator.model.User;
import authenticator.services.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.SceneSwitcher;

public class RegisterController implements Initializable {

    private static final String PENGGUNA_UMUM = "Pengguna Umum";
    private static final String PSIKOLOG = "Psikolog";
    private static final String STYLE_CLASS_SELECTED = "button-userType-selected";

    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassword;

    @FXML
    private Button btnPengguna;
    @FXML
    private Button btnPsikolog;
    @FXML
    private Button buttonCreateAcc;

    private String selectedUserType;
    private SceneSwitcher pindahScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void selectPenggunaAction(ActionEvent event) {
        selectUserType(PENGGUNA_UMUM);
    }

    @FXML
    private void selectPsikologAction(ActionEvent event) {
        selectUserType(PSIKOLOG);
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

        if (username.contains(" ")) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Username tidak boleh mengandung spasi.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Password minimal harus 6 karakter.");
            return;
        }

        User newUser = new User(firstName, lastName, username, email, password, selectedUserType, false);
        boolean isAdded = UserManager.addUser(newUser);

        if (isAdded) {
            showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil",
                    "Akun untuk '" + username + "' berhasil dibuat.");
            goToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal",
                    "Username '" + username + "' sudah digunakan atau terjadi kesalahan.");
        }
    }

    @FXML
    private void handleHaveAccount(ActionEvent event) {
        goToLogin();
    }

    private void selectUserType(String userType) {
        this.selectedUserType = userType;
        updateButtonStyles();
    }

    private void updateButtonStyles() {
        btnPengguna.getStyleClass().remove(STYLE_CLASS_SELECTED);
        btnPsikolog.getStyleClass().remove(STYLE_CLASS_SELECTED);

        if (PENGGUNA_UMUM.equals(selectedUserType)) {
            btnPengguna.getStyleClass().add(STYLE_CLASS_SELECTED);
        } else {
            btnPsikolog.getStyleClass().add(STYLE_CLASS_SELECTED);
        }
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

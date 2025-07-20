package profile.controller;

import java.util.Optional;

import authenticator.model.User;
import authenticator.services.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUsernameController {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lPeringatan;

    private Button btnSave;
    private User currentUser;

    public void setData(User user){
        this.currentUser = user;
        tfUsername.setText(currentUser.getUsername());
    }

    @FXML
    private void prosesEditUsername(ActionEvent event) {
        String newUsername = tfUsername.getText().trim();
        String password = pfPassword.getText();

        if (newUsername.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Harap isi semua kolom.");
            return;
        }

        if (!currentUser.getPassword().equals(password)) {
            showAlert(Alert.AlertType.ERROR, "Verifikasi Gagal", "Password yang Anda masukkan salah.");
            return;
        }

        if (newUsername.equalsIgnoreCase(currentUser.getUsername())) {
            showAlert(Alert.AlertType.INFORMATION, "Tidak Ada Perubahan",
                    "Username baru sama dengan username saat ini.");
            return;
        }

        Optional<User> existingUser = UserManager.findUserByUsername(newUsername);
        if (existingUser.isPresent()) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal",
                    "Username '" + newUsername + "' sudah digunakan. Silakan pilih yang lain.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Perubahan");
        alert.setHeaderText("Anda akan mengubah username Anda menjadi '" + newUsername + "'.");
        alert.setContentText("Apakah Anda yakin ingin melanjutkan?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            currentUser.setUsername(newUsername);
            if (UserManager.updateUser(currentUser)) {
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Username berhasil diubah.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menyimpan perubahan.");
            }
        }
        ((Stage) btnSave.getScene().getWindow()).close();
    }


    @FXML
    private void handleBatal(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) btnSave.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

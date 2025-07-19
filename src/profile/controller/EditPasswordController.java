package profile.controller;

import authenticator.model.User;
import authenticator.services.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class EditPasswordController {
    
    @FXML private PasswordField pfOldPassword;
    @FXML private PasswordField pfNewPassword;
    @FXML private PasswordField pfConfirmPassword;
    @FXML private Button btnSimpan;

    private User currentUser;

    public void setData(User user) {
        this.currentUser = user;
    }

    @FXML
    private void prosesEditPassword(ActionEvent event) {
        String oldPass = pfOldPassword.getText();
        String newPass = pfNewPassword.getText();
        String confirmPass = pfConfirmPassword.getText();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Harap isi semua kolom yang tersedia.");
            return;
        }

        if (!currentUser.getPassword().equals(oldPass)) {
            showAlert(Alert.AlertType.ERROR, "Verifikasi Gagal", "Password lama yang Anda masukkan salah.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Verifikasi Gagal", "Password baru dan konfirmasi tidak cocok.");
            return;
        }

        currentUser.setPassword(newPass);

        if (UserManager.updateUser(currentUser)) {
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Password berhasil diubah.");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menyimpan perubahan.");
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) btnSimpan.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

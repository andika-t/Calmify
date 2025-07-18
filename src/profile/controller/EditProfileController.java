package profile.controller;

import java.io.File;

import authenticator.model.User;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditProfileController {

    @FXML private ImageView profileImageView; 
    @FXML private TextField tfUsername;
    @FXML private TextField tfFirstName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPhoneNumber;
    @FXML private Button btnSave;

    private User currentUser;

    public void setData(User user) {
        tfUsername.setText(user.getUsername());
        tfFirstName.setText(user.getFirstName());
        tfLastName.setText(user.getLastName());
        tfEmail.setText(user.getEmail());
        tfPhoneNumber.setText(user.getPhoneNumber());
        String imagePath = user.getProfilePicturePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                profileImageView.setImage(new Image(imgFile.toURI().toString()));
                return;
            }
        }
        profileImageView.setImage(new Image(getClass().getResourceAsStream("/resources/assets/CALMIFY-LOGO.png")));
    }

    @FXML
    private void prosesEditProfile(ActionEvent event) {
        String firstName = tfFirstName.getText().trim();
        String lastName = tfLastName.getText().trim();
        String email = tfEmail.getText().trim();
        String phoneNumber = tfPhoneNumber.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Nama Depan dan Nama Belakang tidak boleh kosong.");
            return;
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        currentUser.setPhoneNumber(phoneNumber);

        boolean isUpdated = XMLUserService.updateUser(currentUser);

        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Profil berhasil diperbarui.");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menyimpan perubahan.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
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

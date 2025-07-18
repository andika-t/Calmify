package profile.controller;

import authenticator.model.User;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProfileController {

    @FXML private ImageView profileImageView;
    @FXML private TextField tfUsername;
    @FXML private TextField tfFirstName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPhoneNumber;
    @FXML private TextArea taBio;
    @FXML private TextField tfKota;
    @FXML private TextField tfNegara;
    @FXML private TextField tfAlamat;
    @FXML private TextField tfKodePos;
    @FXML private Button btnEditFoto;

    private User currentUser;
    private BorderPane mainPane;

    public void setMainPane(BorderPane mainPane){
        this.mainPane = mainPane;
    }

    public void setData(User user) {
        tfUsername.setText("@" + user.getUsername());
        tfFirstName.setText(user.getFirstName());
        tfLastName.setText(user.getLastName());
        tfEmail.setText(user.getEmail());
        tfPhoneNumber.setText(user.getPhoneNumber().isEmpty() ? "-" : user.getPhoneNumber());
        taBio.setText(user.getBio().isEmpty() ? "-" : user.getBio());
        tfAlamat.setText(user.getAddress().isEmpty() ? "-" : user.getAddress());
        tfNegara.setText(user.getCountry());
        tfKota.setText(user.getCity());
        tfKodePos.setText(user.getPostalCode());

        String imagePath = user.getProfilePicturePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                profileImageView.setImage(new Image(imgFile.toURI().toString()));
                return;
            }
        }
        profileImageView.setImage(new Image(getClass().getResourceAsStream("/resources/assets/LOGO-CALMIFY.png")));
    }

    @FXML
    private void handleEditFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Profil Baru");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile != null) {
            String newPath = saveProfilePicture(selectedFile, currentUser.getUsername());
            if (newPath != null) {
                currentUser.setProfilePicturePath(newPath);
                if (XMLUserService.updateUser(currentUser)) {
                    profileImageView.setImage(new Image(selectedFile.toURI().toString()));
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Foto profil berhasil diperbarui.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan perubahan foto profil.");
                }
            }
        }
    }

    @FXML
    private void handleEditUsername(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/EditUsernameView.fxml"));
            Parent root = loader.load();
            EditUsernameController controller = loader.getController();
            controller.setData(currentUser);

            Stage stage = new Stage();
            stage.setTitle("Edit Data Username");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditProfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/EditProfileView.fxml"));
            Parent root = loader.load();
            EditProfileController controller = loader.getController();
            controller.setData(currentUser);

            Stage stage = new Stage();
            stage.setTitle("Edit Data Profil");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditAlamat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/EditProfileView.fxml"));
            Parent root = loader.load();
            EditAlamatController controller = loader.getController();
            controller.setData(currentUser);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Data Username");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String saveProfilePicture(File file, String username) {
        try {
            Path targetDir = Paths.get("profile_images");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            Path targetPath = targetDir.resolve(username + extension);
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan Gambar", "Terjadi kesalahan saat menyimpan file gambar.");
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

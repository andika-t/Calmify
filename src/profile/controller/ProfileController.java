package profile.controller;

import authenticator.model.User;
import authenticator.services.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class ProfileController implements Initializable{

    @FXML private ImageView profileImageView;
    @FXML private TextField tfUsername, tfFirstName, tfLastName, tfEmail, tfPhoneNumber;
    @FXML private TextArea taBio;
    @FXML private TextField tfKota, tfNegara, tfAlamat, tfKodePos;

    private User currentUser;

    public void setData(User user) {
        this.currentUser = user;
        if (user == null) {
            System.err.println("User yang dikirim ke ProfileController adalah null!");
            return;
        }
        tfUsername.setText("@" + getDisplayString(user.getUsername()));
        tfFirstName.setText(getDisplayString(user.getFirstName()));
        tfLastName.setText(getDisplayString(user.getLastName()));
        tfEmail.setText(getDisplayString(user.getEmail()));
        tfPhoneNumber.setText(getDisplayString(user.getPhoneNumber()));
        taBio.setText(getDisplayString(user.getBio()));
        tfAlamat.setText(getDisplayString(user.getAddress()));
        tfNegara.setText(getDisplayString(user.getCountry()));
        tfKota.setText(getDisplayString(user.getCity()));
        tfKodePos.setText(getDisplayString(user.getPostalCode()));

        loadProfileImage(user.getProfilePicturePath());
    }

    private String getDisplayString(String text) {
        return (text == null || text.trim().isEmpty()) ? "-" : text;
    }

    private void loadProfileImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                try {
                    profileImageView.setImage(new Image(imgFile.toURI().toString()));
                    return;
                } catch (Exception e) {
                    System.err.println("Gagal memuat gambar profil dari path: " + imagePath);
                }
            }
        }
        profileImageView.setImage(new Image(getClass().getResourceAsStream("/resources/assets/CALMIFY-LOGO.png")));
    }

    @FXML
    private void handleEditFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Profil Baru");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile != null) {
            String newAbsolutePath = saveProfilePicture(selectedFile, currentUser.getUsername());
            
            if (newAbsolutePath != null) {
                currentUser.setProfilePicturePath(newAbsolutePath);
                if (UserManager.updateUser(currentUser)) {
                    loadProfileImage(newAbsolutePath);
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Foto profil berhasil diperbarui.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan path foto profil ke data pengguna.");
                }
            }
        }
    }

    private String saveProfilePicture(File file, String username) {
        try {
            Path targetDir = Paths.get(System.getProperty("user.home"), ".CalmifyApp", "profile_images");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            Path targetPath = targetDir.resolve(username + extension);
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toAbsolutePath().toString();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan Gambar", "Terjadi kesalahan saat menyimpan file gambar.");
            return null;
        }
    }

    private void showEditPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (loader.getController() instanceof EditProfileController) {
                ((EditProfileController) loader.getController()).setData(currentUser);
            } else if (loader.getController() instanceof EditUsernameController) {
                ((EditUsernameController) loader.getController()).setData(currentUser);
            } else if (loader.getController() instanceof EditAlamatController) {
                ((EditAlamatController) loader.getController()).setData(currentUser);
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            setData(this.currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void handleEditUsername(ActionEvent event) {
        showEditPopup("/profile/view/EditUsernameView.fxml", "Edit Username");
    }
    @FXML private void handleEditProfil(ActionEvent event) {
        showEditPopup("/profile/view/EditProfileView.fxml", "Edit Profil");
    }
    @FXML private void handleEditAlamat(ActionEvent event) {
        showEditPopup("/profile/view/EditAlamatView.fxml", "Edit Alamat");
    }

    @FXML
    private void handleSimpanBio(ActionEvent event){
        String bio = taBio.getText().trim();
        currentUser.setBio(bio);
        boolean isUpdated = UserManager.updateUser(currentUser);
        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Bio berhasil diperbarui.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menyimpan perubahan.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
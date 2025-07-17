package authenticator.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import authenticator.model.User;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SceneSwitcher;

public class LoginController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lperingatan;
    @FXML
    private Button buttonLogin, buttonDontHaveAcc;
    @FXML
    private VBox authBox;
    @FXML
    private BorderPane panel;
    @FXML
    private ImageView imgCalmify;

    SceneSwitcher pindahScene;
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username dan Password tidak boleh kosong.");
            return;
        }

        Optional<User> userOptional = XMLUserService.findUserByUsername(username);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                Stage currentStage = (Stage) buttonLogin.getScene().getWindow();

                if ("Psikolog".equalsIgnoreCase(user.getUserType())) {
                    showAlert(AlertType.INFORMATION, "Login Berhasil", "Mengarahkan ke Halaman Psikolog...");
                    pindahScene = new SceneSwitcher();
                    pindahScene.switchScene("/home/view/PsikologHomeView");
                } else {
                    showAlert(AlertType.INFORMATION, "Login Berhasil", "Mengarahkan ke Halaman User...");
                    pindahScene = new SceneSwitcher();
                    pindahScene.switchScene("/home/view/UserHomeView");
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Login gagal", "Username atau password salah");
            }
        } else {
            showAlert(AlertType.ERROR, "Login gagal", "Akun tidak terdaftar");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event){
        goToRegiter();
    }

    private void goToRegiter() {
        try {
            SceneSwitcher switcher = new SceneSwitcher();
            switcher.switchScene("/authenticator/view/RegisterView");
        } catch (Exception e) {
            e.printStackTrace();
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
    public void initialize(URL location, ResourceBundle resources) {}
}

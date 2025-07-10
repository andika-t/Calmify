package authenticator.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.naming.AuthenticationException;

import authenticator.model.User;
import authenticator.services.UserAuthenticator;
import authenticator.services.UserService;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

    private UserAuthenticator autentikasi;
    private UserService userService = new XMLUserService();
    private SceneSwitcher pindahScene = new SceneSwitcher();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lperingatan.setText("");
        UserService userService = new XMLUserService();
        autentikasi = new UserAuthenticator(userService);
    }

    @FXML
    private void handleButtonLogin(ActionEvent event) throws AuthenticationException, IOException {
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lperingatan.setText("Username atau password wajib diisi");
            showAlert(Alert.AlertType.WARNING, "Username atau password wajib diisi");
            return;
        }
        User user = userService.findByUsername(username);
        if (user == null) {
            showAlert(Alert.AlertType.WARNING, "Akun tidak terdaftar");
            return;
        }
        
        if(!user.getUsername().equals(username) || !user.getPassword().equals(password)){
            lperingatan.setText("Username atau password salah");
            showAlert(AlertType.WARNING,"Username atau password salah");
            return;
        }
        
        boolean loginSukses = autentikasi.login(username, password);
        if (loginSukses) {
            pindahScene.switchScene("/home/view/homeView");
        }
    }

    @FXML
    private void handleButtonDontHaveAcc(ActionEvent event) throws IOException {
        pindahScene.switchScene("/authenticator/view/registerView");
    }

    private void showAlert(Alert.AlertType type, String pesanAlert) {
        Alert alert = new Alert(type, pesanAlert, ButtonType.OK);
        alert.setTitle("PERINGATAN");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

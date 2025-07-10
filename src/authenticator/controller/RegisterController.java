package authenticator.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.layout.BorderPane;
import util.SceneSwitcher;

public class RegisterController implements Initializable {

    @FXML
    private TextField tfFirstName, tfLastName, tfUsername, tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button buttonCreateAcc, buttonHaveAcc;
    @FXML
    private Label lperingatan;

    @FXML
    private BorderPane panel;

    private UserAuthenticator autentikasi;
    private SceneSwitcher pindahScene = new SceneSwitcher();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lperingatan.setText("");;
        UserService userService = new XMLUserService();
        autentikasi = new UserAuthenticator(userService);
    }

    @FXML
    private void handleButtonCreateAcc(ActionEvent event) throws IOException {
        String firstname = tfFirstName.getText();
        String lastname = tfLastName.getText();
        String username = tfUsername.getText();
        String email = tfEmail.getText();
        String password = tfPassword.getText();

        if(firstname.isEmpty() || lastname.isEmpty() || username.isEmpty()
        || email.isEmpty() || password.isEmpty()){
            lperingatan.setText("Lengkapi data diri anda");
            showAlert(Alert.AlertType.WARNING, "Lengkapi data diri anda");
            return;
        }

        User u = autentikasi.findUser(username);
        if (u != null){
            lperingatan.setText("Username tidak tersedia");
            showAlert(AlertType.WARNING,"Username tidak tersedia");
        }

        User newUser = new User(firstname, lastname, username, email, password);
        boolean registerSukses = autentikasi.register(newUser);
        if(!registerSukses){
            showAlert(AlertType.WARNING,"Gagal membuat akun");
        }
        pindahScene.switchScene("/home/view/homeView");

    }

    @FXML
    private void handleButtonHaveAcc(ActionEvent event) throws IOException {

    }

    private void showAlert(Alert.AlertType type, String pesanAlert) {
        Alert alert = new Alert(type, pesanAlert, ButtonType.OK);
        alert.setTitle("PERINGATAN");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

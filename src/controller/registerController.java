package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.User;
import model.UserXML;
import util.AnimasiTransisi;
import util.SceneSwitcher;
import util.AnimasiHover;

public class registerController implements Initializable {

    @FXML
    private TextField tfFirstName, tfLastName, tfUsername, tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button buttonCreateAcc, buttonHaveAcc;
    @FXML
    private Label lperingatan;

    @FXML private BorderPane panel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lperingatan.setText("");
        AnimasiHover.to(buttonCreateAcc);
        AnimasiHover.to(buttonHaveAcc);
    }

    @FXML
    private void handleButtonCreateAcc(ActionEvent event) throws IOException {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String username = tfUsername.getText();
        String mailString = tfEmail.getText();
        String password = tfPassword.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || mailString.isEmpty()
                || password.isEmpty()) {
            lperingatan.setText("Lengkapi semua data anda");
            return;
        }
        UserXML xml = new UserXML();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmailAddress(mailString);
        user.setPassword(password);

        boolean daftarBerhasil = xml.saveUser(user);

        try {
            if (daftarBerhasil) {
                SceneSwitcher.switchScene("/view/loginView.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonHaveAcc(ActionEvent event) throws IOException {
        AnimasiTransisi.slideOutRight(panel, () -> {
            try {
                SceneSwitcher.switchScene("/view/loginView.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

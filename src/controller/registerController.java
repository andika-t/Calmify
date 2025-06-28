package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import model.UserXML;
import util.AnimasiTransisi;
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

    private FXMLLoader loader;
    private Scene scene;
    private Stage stage;
    private Parent root;

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
                loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonHaveAcc(ActionEvent event) throws IOException {
        AnimasiTransisi.slideOutRight(panel, () -> {
            try {
                loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}

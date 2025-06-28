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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.UserXML;
import util.AnimasiTransisi;
import util.AnimasiHover;

public class loginController implements Initializable{
    
    @FXML private TextField tfUsername;
    @FXML private PasswordField tfPassword;
    @FXML private Label lperingatan;
    @FXML private Button buttonLogin, buttonDontHaveAcc;
    @FXML private VBox authBox;
    @FXML private BorderPane panel;
    @FXML private ImageView imgCalmify;

    private FXMLLoader loader;
    private Scene scene;
    private Stage stage;
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lperingatan.setText("");

        AnimasiHover.to(buttonLogin);
        AnimasiHover.to(buttonDontHaveAcc);
    }

    @FXML
    private void handleButtonLogin(ActionEvent event) throws IOException {
    String username = tfUsername.getText();
    String password = tfPassword.getText();

    UserXML service = new UserXML();
    boolean loginSuccess = service.login(username, password);

    if (loginSuccess) {
        loader = new FXMLLoader(getClass().getResource("/view/homeView.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } else {
        lperingatan.setText("Username atau password salah");
        }
    }

    @FXML
    private void handleButtonDontHaveAcc(ActionEvent event) throws IOException {
        AnimasiTransisi.slideOutLeft(panel,() ->{
            try {
            loader = new FXMLLoader(getClass().getResource("/view/registerView.fxml"));
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

package home.controller;

import java.net.URL;
import java.util.ResourceBundle;

import authenticator.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import util.SceneSwitcher;

public class HomeSettingController implements Initializable{
    @FXML private BorderPane mainPane;
    @FXML private Label lnamaLengkap;
    @FXML private Label lusername;
    @FXML private Label ltipePengguna;
    @FXML private Button btnLogout;
    @FXML private Button btnDashboard;

    private User currentUser;
    private SceneSwitcher pindahScene;

    public void setUser(User user){
        this.currentUser = user;
    }

    @FXML private handleHome(ActionEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}

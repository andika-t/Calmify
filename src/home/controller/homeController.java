package home.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import util.SceneSwitcher;

public class homeController implements Initializable {
    @FXML
    private StackPane mainPane;

    @FXML
    private Button btnHome, btnSetting, btnDashboard, 
    btnPantauStres, btnSelfCare, btnStudio, btnCommunity, btnBell, btnChat, btnLogout;

    @FXML private Label labelPengguna, labelUsia;

    private SceneSwitcher pindahScene = new SceneSwitcher();
    
    @FXML
    public void handleButtonLogout(ActionEvent event) {
        try {
            pindahScene.switchScene("/authenticator/view/loginView");
        } catch (Exception e) {
            System.out.println("Perhatian: "+e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
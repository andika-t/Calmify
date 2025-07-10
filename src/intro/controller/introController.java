package intro.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import util.SceneSwitcher;

public class introController implements Initializable {

    @FXML private AnchorPane intro;
    
    private SceneSwitcher pindahScene;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PauseTransition delay = new PauseTransition(Duration.millis(800));
        delay.setOnFinished(e -> fadeOutScene());
        delay.play();
    }

    private void fadeOutScene() {
        pindahScene = new SceneSwitcher();
        FadeTransition fade = new FadeTransition(Duration.millis(1000), intro);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            try {
                pindahScene.switchScene("/authenticator/view/loginView");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        fade.play();
    }
}
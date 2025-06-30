package util;

import java.io.IOException;

import app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
    public static void switchScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = Main.getMainStage();
        Scene scene = Main.getMainStage().getScene();
        scene.setRoot(root);
        stage.getFullScreenExitHint();
        stage.getScene();
    }
}

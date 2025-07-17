package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/authenticator/view/RegisterView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreenExitKeyCombination(
                new KeyCodeCombination(KeyCode.ESCAPE));

        stage.getIcons().add(new Image("/resources/assets/CALMIFY-LOGO.png"));
        stage.setTitle("CALMIFY");
        stage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}

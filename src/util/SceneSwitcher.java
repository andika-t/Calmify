package util;


import java.net.URL;

import app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneSwitcher {
    private Pane halaman;
    
    public Pane getPane(String namaFile){
        try {
            URL fileHalaman = Main.class.getResource(namaFile + ".fxml");
            if(fileHalaman == null){
                throw new java.io.FileNotFoundException("Halaman tidak ditemukan");
            }
            new FXMLLoader();
            halaman = FXMLLoader.load(fileHalaman);
        } catch (Exception e) {
            System.out.println("Tidak ditemukan halaman tersebut");
            e.printStackTrace();
        }
        return halaman;
    }

    public void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath+".fxml"));
            Parent root = loader.load();
            Stage stage = Main.getMainStage();
            Scene scene = Main.getMainStage().getScene();
            scene.setRoot(root);
            stage.getFullScreenExitHint();
            stage.getScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

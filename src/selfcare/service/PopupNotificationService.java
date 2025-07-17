package selfcare.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import selfcare.controller.NotificationPopupController;

import java.io.IOException;

public class PopupNotificationService implements INotificationService {

    @Override
    public void showNotification(String title, String message, Stage ownerStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/selfcareapp/NotificationPopup.fxml"));
            Parent root = loader.load();

            NotificationPopupController controller = loader.getController();
            controller.setNotificationText(title, message);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(ownerStage);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat NotificationPopup.fxml: " + e.getMessage());
        }
    }
}
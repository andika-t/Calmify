package selfcare.service;

import javafx.stage.Stage;

public interface INotificationService {
    void showNotification(String title, String message, Stage ownerStage);
}
package selfcare.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificationPopupController {

    @FXML
    private Label notificationTitleLabel;
    @FXML
    private Label notificationMessageLabel;
    @FXML
    private Button okButton;

    public void setNotificationText(String title, String message) {
        notificationTitleLabel.setText(title);
        notificationMessageLabel.setText(message);
    }

    @FXML
    private void handleOkButton(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
package authenticator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class RecoveryController {

    @FXML private FlowPane codesPane;
    @FXML private Button btnDone;

    public void initData(List<String> codes) {
        codesPane.getChildren().clear();
        for (String code : codes) {
            Label codeLabel = new Label(code);
            codeLabel.setFont(new Font("Monospaced", 16));
            codeLabel.setStyle("-fx-background-color: #f3f4f6; -fx-padding: 5 10 5 10; -fx-background-radius: 5;");
            codesPane.getChildren().add(codeLabel);
        }
    }

    @FXML
    private void handleDoneAction(ActionEvent event) {
        try {
            ((Stage) btnDone.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

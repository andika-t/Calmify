package pantauStres.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class UserAssessmentController  {

    private BorderPane mainPane;

    public void setMainPane(BorderPane mainPane){
        this.mainPane = mainPane;
    }
    
    @FXML 
    private void handleMulai(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/QuestionnaireView.fxml"));
            Pane pane = loader.load();
            QuestionnaireController controller = loader.getController();
            controller.setMainPane(mainPane);
            mainPane.setCenter(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
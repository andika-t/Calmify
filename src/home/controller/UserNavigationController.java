package home.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserNavigationController {

    private MainHomeController mainController;

    public void setMainController(MainHomeController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        if (mainController != null)
            mainController.handleHome(event);
    }

    @FXML
    private void handlePantauStres(ActionEvent event) {
        if (mainController != null)
            mainController.handlePantauStres(event);
    }

    @FXML
    private void handleSelfCare(ActionEvent event) {
        if (mainController != null)
            mainController.handleSelfCare(event);
    }

    @FXML
    private void handleCalmifyStudio(ActionEvent event) {
        if (mainController != null)
            mainController.handleCalmifyStudio(event);
    }

    @FXML
    private void handleCommunity(ActionEvent event) {
        if (mainController != null)
            mainController.handleCommunity(event);
    }

    @FXML
    private void handleSetting(ActionEvent event) {
        if (mainController != null)
            mainController.handleSetting(event);
    }
}
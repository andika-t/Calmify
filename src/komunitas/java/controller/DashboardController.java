package komunitas.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import komunitas.java.main.AppManager;
import komunitas.java.model.StressTestResult;
import komunitas.java.service.AuthService;
import komunitas.java.service.StressTestService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label lastTestLabel;
    @FXML private Label stressLevelLabel;
    
    private AppManager appManager;
    private AuthService authService;
    private StressTestService stressTestService;

    public void setAppManager(AppManager appManager) {
    this.appManager = appManager;
    }
    
    public void setServices(AuthService authService, StressTestService stressTestService) {
        this.authService = authService;
        this.stressTestService = stressTestService;
        updateDashboard();
    }
    
    private void updateDashboard() {
        try {
            String username = authService.getCurrentUser().getUsername();
            welcomeLabel.setText("Selamat datang, " + username + "!");
            
            List<StressTestResult> results = stressTestService.getUserResults(
                authService.getCurrentUser().getId()
            );
            
            if (!results.isEmpty()) {
                StressTestResult lastResult = results.get(results.size() - 1);
                lastTestLabel.setText("Tes terakhir: " + formatDate(lastResult.getTestDate()));
                stressLevelLabel.setText("Tingkat stres: " + lastResult.getLevel());
                
                // Set style based on stress level
                setStressLevelStyle(lastResult.getLevel());
            } else {
                lastTestLabel.setText("Anda belum melakukan tes");
                stressLevelLabel.setText("");
                stressLevelLabel.setStyle("");
            }
        } catch (Exception e) {
            showAlert("Error", "Gagal memuat dashboard: " + e.getMessage());
        }
    }
    
    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
    
    private void setStressLevelStyle(String level) {
        String style = "";
        switch (level.toLowerCase()) {
            case "rendah":
                style = "-fx-text-fill: #27ae60;";
                break;
            case "sedang":
                style = "-fx-text-fill: #f39c12;";
                break;
            case "tinggi":
            case "sangat tinggi":
                style = "-fx-text-fill: #e74c3c;";
                break;
        }
        stressLevelLabel.setStyle(style);
    }
    
    
    @FXML
    private void handleStartTest() {
        appManager.showQuestionScreen();
    }

    @FXML
    private void handleViewHistory() {
        appManager.showHistoryScreen();
    }

    @FXML
    private void handleSettings() {
        appManager.showSettingsScreen();
    }

    @FXML
    private void handleLogout() {
        authService.logout();
        appManager.showLoginScreen();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
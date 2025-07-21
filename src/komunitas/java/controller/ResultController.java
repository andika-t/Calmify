package komunitas.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import komunitas.java.main.AppManager;
import komunitas.java.model.StressTestResult;
import komunitas.java.service.StressTestService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.format.DateTimeFormatter;

public class ResultController {
    @FXML private Label resultLabel;
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label dateLabel;
    @FXML private Label recommendationLabel;
    
    private AppManager appManager;
    private StressTestService stressTestService;
    private StressTestResult result;
    
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    public void setStressTestService(StressTestService stressTestService) {
        this.stressTestService = stressTestService;
    }
    
    public void setResult(StressTestResult result) {
        this.result = result;
        displayResult();
    }
    
    private void displayResult() {
        scoreLabel.setText(String.valueOf(result.getScore()));
        levelLabel.setText(result.getLevel());
        
        // Fix untuk format tanggal
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        dateLabel.setText(result.getTestDate().format(formatter));

        setLevelStyle(result.getLevel());
        recommendationLabel.setText(getRecommendation(result.getLevel()));
    }
    
    private void setLevelStyle(String level) {
        String style = "-fx-font-weight: bold; ";
        
        switch (level.toLowerCase()) {
            case "rendah":
                style += "-fx-text-fill: #27ae60;";
                break;
            case "sedang":
                style += "-fx-text-fill: #f39c12;";
                break;
            case "tinggi":
            case "sangat tinggi":
                style += "-fx-text-fill: #e74c3c;";
                break;
        }
        
        levelLabel.setStyle(style);
    }
    
    private String getRecommendation(String level) {
        switch (level.toLowerCase()) {
            case "rendah":
                return "Tingkat stres Anda rendah. Pertahankan gaya hidup sehat dan rutin memeriksa tingkat stres Anda.";
            case "sedang":
                return "Anda mengalami stres sedang. Coba lakukan relaksasi, olahraga ringan, atau aktivitas yang menyenangkan.";
            case "tinggi":
                return "Tingkat stres Anda tinggi. Pertimbangkan untuk berkonsultasi dengan profesional kesehatan mental.";
            case "sangat tinggi":
                return "Tingkat stres Anda sangat tinggi. Sangat disarankan untuk segera berkonsultasi dengan profesional kesehatan mental.";
            default:
                return "Silakan konsultasikan hasil ini dengan profesional kesehatan mental.";
        }
    }
    
    @FXML
    private void handleSave() {
        try {
            stressTestService.saveResult(result);
            showAlert("Sukses", "Hasil tes berhasil disimpan", AlertType.INFORMATION);
            appManager.showDashboard();
        } catch (Exception e) {
            showAlert("Error", "Gagal menyimpan hasil: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleBackToDashboard() {
        appManager.showDashboard();
    }
    
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
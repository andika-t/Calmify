package komunitas.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import komunitas.java.main.AppManager;
import komunitas.java.model.StressTestResult;
import komunitas.java.service.AuthService;
import komunitas.java.service.StressTestService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryController {
    @FXML private TableView<StressTestResult> historyTable;
    @FXML private TableColumn<StressTestResult, LocalDateTime> dateColumn;
    @FXML private TableColumn<StressTestResult, Integer> scoreColumn;
    @FXML private TableColumn<StressTestResult, String> levelColumn;
    
    private AppManager appManager;
    private AuthService authService;
    private StressTestService stressTestService;
    
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    public void setServices(AuthService authService, StressTestService stressTestService) {
        this.authService = authService;
        this.stressTestService = stressTestService;
        loadHistory();
    }
    
    private void loadHistory() {
        try {
            List<StressTestResult> results = stressTestService.getUserResults(
                authService.getCurrentUser().getId()
            );
            
            // Fix untuk dateColumn - gunakan LocalDateTime langsung
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("testDate"));
            dateColumn.setCellFactory(column -> {
                return new TableCell<StressTestResult, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            // Format LocalDateTime langsung tanpa parsing
                            setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                        }
                    }
                };
            });
            
            scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
            levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

            levelColumn.setCellFactory(column -> {
                return new TableCell<StressTestResult, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            setStyle(getLevelStyle(item));
                        }
                    }
                };
            });
            
            ObservableList<StressTestResult> observableList = FXCollections.observableArrayList(results);
            historyTable.setItems(observableList);
        } catch (Exception e) {
            showAlert("Error", "Gagal memuat riwayat: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    private String getLevelStyle(String level) {
        if (level == null) return "";
        
        switch (level.toLowerCase()) {
            case "rendah":
                return "-fx-text-fill: #27ae60; -fx-font-weight: bold;";
            case "sedang":
                return "-fx-text-fill: #f39c12; -fx-font-weight: bold;";
            case "tinggi":
            case "sangat tinggi":
                return "-fx-text-fill: #e74c3c; -fx-font-weight: bold;";
            default:
                return "";
        }
    }
    
    @FXML
    private void handleDelete() {
        StressTestResult selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                stressTestService.deleteResult(selected.getId());
                loadHistory();
                showAlert("Sukses", "Hasil tes berhasil dihapus", AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Gagal menghapus hasil: " + e.getMessage(), AlertType.ERROR);
            }
        } else {
            showAlert("Peringatan", "Silakan pilih hasil tes yang ingin dihapus", AlertType.WARNING);
        }
    }
    
    @FXML
    private void handleViewDetail() {
        StressTestResult selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            appManager.showResultScreen(selected);
        } else {
            showAlert("Peringatan", "Silakan pilih hasil tes yang ingin dilihat", AlertType.WARNING);
        }
    }
    
    @FXML
    private void handleBack() {
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
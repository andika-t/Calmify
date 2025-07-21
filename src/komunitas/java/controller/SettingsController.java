package komunitas.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import komunitas.java.main.AppManager;
import komunitas.java.model.UserSettings;
import komunitas.java.repository.UserRepository;
import komunitas.java.service.AuthService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class SettingsController {
    @FXML private CheckBox autoSaveCheckbox;
    @FXML private CheckBox allowSharingCheckbox;
    @FXML private ComboBox<String> themeComboBox;
    
    private AppManager appManager;
    private AuthService authService;
    private UserRepository userRepository;
    private UserSettings currentSettings;
    
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    public void setServices(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        loadSettings();
    }
    
    private void loadSettings() {
        try {
            currentSettings = userRepository.getUserSettings(authService.getCurrentUser().getId());
            
            if (currentSettings == null) {
                // Create default settings if not exists
                currentSettings = new UserSettings(
                    authService.getCurrentUser().getId(),
                    true,
                    false,
                    "Light"
                );
            }
            
            autoSaveCheckbox.setSelected(currentSettings.isAutoSaveResults());
            allowSharingCheckbox.setSelected(currentSettings.isAllowDataSharing());
            
            themeComboBox.getItems().addAll("Light", "Dark", "Blue");
            themeComboBox.setValue(currentSettings.getTheme());
        } catch (Exception e) {
            showAlert("Error", "Gagal memuat pengaturan: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSave() {
        try {
            currentSettings.setAutoSaveResults(autoSaveCheckbox.isSelected());
            currentSettings.setAllowDataSharing(allowSharingCheckbox.isSelected());
            currentSettings.setTheme(themeComboBox.getValue());
            
            userRepository.saveUserSettings(currentSettings);
            showAlert("Sukses", "Pengaturan berhasil disimpan", AlertType.INFORMATION);
            
            appManager.applyTheme(currentSettings.getTheme());
        } catch (Exception e) {
            showAlert("Error", "Gagal menyimpan pengaturan: " + e.getMessage());
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
    
    private void showAlert(String title, String message) {
        showAlert(title, message, AlertType.INFORMATION);
    }
}
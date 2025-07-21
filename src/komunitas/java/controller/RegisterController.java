package komunitas.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import komunitas.java.main.AppManager;
import komunitas.java.service.AuthService;
import javafx.scene.control.Alert.AlertType;


public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    
    private AppManager appManager;
    
    private AuthService authService; 
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password must be filled");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }
        
        try {
            if (authService.register(username, password)) {
                showAlert("Success", "Registration successful", AlertType.INFORMATION);
                appManager.showLoginScreen();
            } else {
                showAlert("Error", "Registration failed. Username may already exist");
            }
        } catch (Exception e) {
            showAlert("Error", "Registration error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        appManager.showLoginScreen();
    }
    
    private void showAlert(String title, String message) {
        showAlert(title, message, AlertType.ERROR);
    }
    
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
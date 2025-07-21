package komunitas.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import komunitas.java.main.AppManager;
import komunitas.java.service.AuthService;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;



public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    private AppManager appManager;
    private AuthService authService;
    
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username dan password harus diisi");
            return;
        }
        
        try {
            if (authService.login(username, password) != null) {
                appManager.showDashboard();
            } else {
                showAlert("Login Gagal", "Username atau password salah");
            }
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan saat login: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        appManager.showRegisterScreen();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
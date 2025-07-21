package komunitas.java.main;

import java.net.URL;

import komunitas.java.controller.DashboardController;
import komunitas.java.controller.HistoryController;
import komunitas.java.controller.LoginController;
import komunitas.java.controller.QuestionController;
import komunitas.java.controller.RegisterController;
import komunitas.java.controller.ResultController;
import komunitas.java.controller.SettingsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import komunitas.java.repository.StressTestRepository;
import komunitas.java.repository.UserRepository;
import komunitas.java.repository.impl.StressTestXmlRepository;
import komunitas.java.repository.impl.UserXmlRepository;
import komunitas.java.service.AuthService;
import komunitas.java.service.StressTestService;
import komunitas.java.service.impl.AuthServiceImpl;
import komunitas.java.service.impl.StressTestServiceImpl;
import komunitas.java.model.StressTestResult;
import komunitas.java.model.UserSettings;

public class AppManager {
    private Stage primaryStage;
    private AuthService authService;
    private StressTestService stressTestService;
    private UserRepository userRepository;

    public AppManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeServices();
    }

    private void initializeServices() {
        userRepository = new UserXmlRepository();
        StressTestRepository stressTestRepository = new StressTestXmlRepository();

        this.authService = new AuthServiceImpl(userRepository);
        this.stressTestService = new StressTestServiceImpl(stressTestRepository);
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setAppManager(this);
            controller.setAuthService(authService);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            primaryStage.setTitle("Stress Monitor App - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRegisterScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setAppManager(this);
            controller.setAuthService(authService);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            primaryStage.setTitle("Stress Monitor App - Register");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setAppManager(this);
            controller.setServices(authService, stressTestService);

            Scene scene = new Scene(root);
            applyCurrentTheme(scene);

            primaryStage.setTitle("Stress Monitor App - Dashboard");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load dashboard");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void showQuestionScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/question.fxml"));
            Parent root = loader.load();

            QuestionController controller = loader.getController();
            controller.setAppManager(this);
            controller.setServices(stressTestService, authService);

            Scene scene = new Scene(root);
            applyCurrentTheme(scene);

            primaryStage.setTitle("Stress Monitor App - Tes Stres");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal memuat halaman tes");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void showResultScreen(StressTestResult result) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/result.fxml"));
            Parent root = loader.load();

            ResultController controller = loader.getController();
            controller.setAppManager(this);
            controller.setStressTestService(stressTestService);
            controller.setResult(result);

            Scene scene = new Scene(root);
            applyCurrentTheme(scene);

            primaryStage.setTitle("Stress Monitor App - Hasil Tes");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHistoryScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/history.fxml"));
            Parent root = loader.load();

            HistoryController controller = loader.getController();
            controller.setAppManager(this);
            controller.setServices(authService, stressTestService);

            Scene scene = new Scene(root);
            applyCurrentTheme(scene);

            primaryStage.setTitle("Stress Monitor App - Riwayat Tes");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSettingsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
            Parent root = loader.load();

            SettingsController controller = loader.getController();
            controller.setAppManager(this);
            controller.setServices(authService, userRepository);

            Scene scene = new Scene(root);
            applyCurrentTheme(scene);

            primaryStage.setTitle("Stress Monitor App - Pengaturan");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyTheme(String theme) {
        if (primaryStage.getScene() != null) {
            applyCurrentTheme(primaryStage.getScene());
        }
    }

    private void applyCurrentTheme(Scene scene) {
        scene.getStylesheets().clear();

        URL baseStyle = getClass().getResource("/styles/styles.css");
        if (baseStyle != null) {
            scene.getStylesheets().add(baseStyle.toExternalForm());
        } else {
            System.err.println("Base stylesheet not found");
        }

        if (authService.getCurrentUser() != null) {
            UserSettings settings = userRepository.getUserSettings(authService.getCurrentUser().getId());
            if (settings != null && settings.getTheme() != null && !settings.getTheme().equalsIgnoreCase("Light")) {
                String themeStyle = "/styles/" + settings.getTheme().toLowerCase() + "_theme.css";
                URL themeResource = getClass().getResource(themeStyle);

                if (themeResource != null) {
                    scene.getStylesheets().add(themeResource.toExternalForm());
                } else {
                    System.err.println("Theme stylesheet not found: " + themeStyle);
                    URL defaultTheme = getClass().getResource("/styles/dark_theme.css");
                    if (defaultTheme != null) {
                        scene.getStylesheets().add(defaultTheme.toExternalForm());
                    }
                }
            }
        }
    }
}
package selfCare;

import selfCare.controller.PsychologistViewController;
import selfCare.controller.UserViewController;
import selfCare.model.User;
import selfCare.service.IDataManager;
import selfCare.service.IUserService;
import selfCare.service.UserService;
import selfCare.service.XmlDataManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static final String DATA_FILE = "selfcare_data.xml";

    private IDataManager dataManager;
    private IUserService userService;

    private static final boolean IS_PSYCHOLOGIST_VIEW = true;

    private static final String USER_ID_FOR_USER_VIEW = "user001";

    @Override
    public void start(Stage primaryStage) {
        try {
            dataManager = new XmlDataManager(DATA_FILE);
            userService = new UserService(dataManager);

            if (userService.getAllUsers().isEmpty()) {
                userService.addUser(new User("user001", "Alice (Pengguna Individu)"));
                userService.addUser(new User("user002", "Bob (Pendamping)"));
                userService.addUser(new User("user003", "Charlie (Pengguna Individu)"));
                System.out.println("Pengguna awal ditambahkan.");
            }

            FXMLLoader loader;
            Parent root;

            if (IS_PSYCHOLOGIST_VIEW) {
                loader = new FXMLLoader(getClass().getResource("/Selfcare/view/PsychologistView.fxml"));
                root = loader.load();
                PsychologistViewController controller = loader.getController();
                controller.setUserService(userService);
                primaryStage.setTitle("Aplikasi Self-Care - Tampilan Psikolog");
            } else {
                loader = new FXMLLoader(getClass().getResource("/Selfcare/view/UserView.fxml"));
                root = loader.load();
                UserViewController controller = loader.getController();
                controller.initData(userService, USER_ID_FOR_USER_VIEW);
                primaryStage.setTitle("Aplikasi Self-Care - Profil Pengguna: " + USER_ID_FOR_USER_VIEW);
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                userService.saveAllUsers();
                System.out.println("Data disimpan sebelum keluar.");
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Kesalahan Pemuatan Tampilan: Gagal memuat tampilan FXML: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Kesalahan Aplikasi: Terjadi kesalahan yang tidak terduga: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

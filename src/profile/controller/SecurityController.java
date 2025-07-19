package profile.controller;

import authenticator.controller.RecoveryController;
import authenticator.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SecurityController {

    @FXML private TextField tfEmail;
    
    private User currentUser;

    // PERBAIKAN: Metode initData dan setData digabung menjadi satu agar lebih jelas.
    public void setData(User user) {
        this.currentUser = user;
        if (user != null) {
            tfEmail.setText(user.getEmail());
        }
    }

    @FXML
    private void handleGantiPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/view/EditPasswordView.fxml"));
            Parent root = loader.load();
            EditPasswordController controller = loader.getController();
            controller.setData(currentUser);
            Stage stage = createPopupStage("Ubah Password", root);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Gagal memuat pop-up ganti password.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLihatKode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/authenticator/view/RecoveryView.fxml"));
            Parent root = loader.load();
            RecoveryController controller = loader.getController();
            controller.initData(currentUser.getRecoveryCodes());
            Stage stage = createPopupStage("Kode Pemulihan Anda", root);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman kode pemulihan.");
            e.printStackTrace();
        }
    }

    private Stage createPopupStage(String title, Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        return stage;
    }
}
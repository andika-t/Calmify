package profile.controller;

import authenticator.model.User;
import authenticator.services.XMLUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditAlamatController {

    @FXML private TextField tfNegara;
    @FXML private TextField tfKota;
    @FXML private TextField tfAlamat;
    @FXML private TextField tfKodePos;
    @FXML private Button btnSimpan;

    private User currentUser;

    public void setData(User user) {
        tfNegara.setText(user.getCountry());
        tfKota.setText(user.getCity());
        tfAlamat.setText(user.getAddress());
        tfKodePos.setText(user.getPostalCode());
    }

    @FXML
    private void handleEditAlamat(ActionEvent event) {
        String negara = tfNegara.getText().trim();
        String kota = tfKota.getText().trim();
        String alamat = tfAlamat.getText().trim();
        String kodePos = tfKodePos.getText().trim();

        if (negara.isEmpty() || kota.isEmpty() || alamat.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Negara, Kota, dan Alamat tidak boleh kosong.");
            return;
        }

        currentUser.setCountry(negara);
        currentUser.setCity(kota);
        currentUser.setAddress(alamat);
        currentUser.setPostalCode(kodePos);

        boolean isUpdated = XMLUserService.updateUser(currentUser);

        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Alamat berhasil diperbarui.");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menyimpan perubahan.");
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) btnSimpan.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

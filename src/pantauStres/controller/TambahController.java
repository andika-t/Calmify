package pantauStres.controller;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pantauStres.model.Question;
import pantauStres.services.QuestionManager;

public class TambahController {

    @FXML
    private TextField tfId;
    @FXML
    private TextArea taPertanyaan;
    @FXML
    private TextField tfSkor;

    private QuestionManager manager = new QuestionManager();
    private ArrayList<Question> dataList = manager.getQuestions();

    @FXML
    private void prosesTambahData() {
        int id = Integer.parseInt(tfId.getText());
        int skor = Integer.parseInt(tfSkor.getText());
        String pertanyaan = taPertanyaan.getText();

        if (tfId.getText().isEmpty() || taPertanyaan.getText().isEmpty() || tfSkor.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Harap isi semua field yang tersedia.");
            return;
        }

        Question newQuestion = new Question(id, pertanyaan, skor);

        int duplikat = 0;

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId() == id) {
                duplikat++;
            }
        }

        if (duplikat > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Data dengan ID = " + id + " sudah ada. Gunakan ID lain.");
            alert.showAndWait();
        } else {
            manager.tambahData(newQuestion);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pertanyaan berhasil ditambahkan!");
            Stage stage = (Stage) tfId.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

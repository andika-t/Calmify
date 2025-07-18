package pantauStres.controller;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pantauStres.model.Question;
import pantauStres.services.QuestionManager;

public class EditController {

    @FXML
    private TextField tfId;
    @FXML
    private TextArea taPertanyaan;
    @FXML
    private TextField tfSkor;

    QuestionManager manager = new QuestionManager();
    ArrayList<Question> dataList = manager.getQuestions();

    @FXML
    private void prosesEditData() {
        int id = Integer.parseInt(tfId.getText());
        int skor = Integer.parseInt(tfSkor.getText());
        String pertanyaan = taPertanyaan.getText();
        Question newQuestion = new Question(id, pertanyaan, skor);

        if (tfId.getText().isEmpty() || taPertanyaan.getText().isEmpty() || tfSkor.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Harap isi semua field yang tersedia.");
            return;
        }

        int index = 0;

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId() == id) {
                index = i;
                break;
            }
        }

            manager.editData(index, newQuestion);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil diubah");
            Stage stage = (Stage) tfId.getScene().getWindow();
            stage.close();
        }

    public void setData(Question item) {
        tfId.setText(String.valueOf(item.getId()));
        taPertanyaan.getText();
        tfSkor.setText(String.valueOf(item.getId()));
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

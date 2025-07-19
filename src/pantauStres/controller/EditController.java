package pantauStres.controller;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pantauStres.model.Question;
import pantauStres.services.QuestionModel;

public class EditController {
    private static final String STYLE_CLASS_SELECTED = "button-userType-selected";
    private static final String KATEGORI_MOOD = "Mood";
    private static final String KATEGORI_KUALITAS = "Kualitas";

    @FXML private TextField tfId;
    @FXML private TextArea taPertanyaan;
    @FXML private TextField tfSkor;
    @FXML private Button btnMood;
    @FXML private Button btnKualitas;

    private final QuestionModel model = new QuestionModel();
    private String selectKategori;
    private Question questionToEdit;

    public void setData(Question item) {
        this.questionToEdit = item;
        tfId.setText(String.valueOf(item.getId()));
        tfId.setDisable(true); // ID tidak seharusnya diubah
        taPertanyaan.setText(item.getPertanyaan());
        tfSkor.setText(String.valueOf(item.getSkor())); // PERBAIKAN: Menggunakan getSkor()
        selectKategori(item.getKategori());
        updateButtonStyles();
    }

    @FXML
    private void prosesEditData() {
        if (taPertanyaan.getText().isEmpty() || tfSkor.getText().isEmpty() || selectKategori == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Harap isi semua field dan pilih kategori.");
            return;
        }

        try {
            int id = Integer.parseInt(tfId.getText());
            int skor = Integer.parseInt(tfSkor.getText());
            String pertanyaan = taPertanyaan.getText();
            
            Question newQuestion = new Question(id, pertanyaan, skor, selectKategori);

            // Cari index dari data yang akan di-edit dengan membaca ulang dari file
            ArrayList<Question> dataList = model.getQuestions();
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getId() == id) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                model.updateQuestion(index, newQuestion);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil diubah");
                ((Stage) tfId.getScene().getWindow()).close();
            } else {
                 showAlert(Alert.AlertType.ERROR, "Error", "Data yang akan diubah tidak ditemukan.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Skor harus berupa angka.");
        }
    }
        
    @FXML private void handleKategoriMood(ActionEvent event) {
        selectKategori(KATEGORI_MOOD);
    }
    @FXML private void handleKategoriKualitas(ActionEvent event) {
        selectKategori(KATEGORI_KUALITAS);
    }
    private void selectKategori(String kategori) {
        this.selectKategori = kategori;
        updateButtonStyles();
    }
    private void updateButtonStyles() {
        btnMood.getStyleClass().remove(STYLE_CLASS_SELECTED);
        btnKualitas.getStyleClass().remove(STYLE_CLASS_SELECTED);
        if (KATEGORI_MOOD.equals(selectKategori)) {
            btnMood.getStyleClass().add(STYLE_CLASS_SELECTED);
        } else if (KATEGORI_KUALITAS.equals(selectKategori)) {
            btnKualitas.getStyleClass().add(STYLE_CLASS_SELECTED);
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
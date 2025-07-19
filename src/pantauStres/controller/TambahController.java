package pantauStres.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pantauStres.model.Question;
import pantauStres.services.QuestionModel;

public class TambahController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int nextId = model.getQuestions().stream()
                .mapToInt(Question::getId)
                .max()
                .orElse(0) + 1;
        tfId.setText(String.valueOf(nextId));
        tfId.setDisable(true);
    }

    @FXML
    private void prosesTambahData() {
        if (taPertanyaan.getText().isEmpty() || tfSkor.getText().isEmpty() || selectKategori == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Harap isi semua kolom dan pilih kategori.");
            return;
        }

        try {
            int id = Integer.parseInt(tfId.getText());
            int skor = Integer.parseInt(tfSkor.getText());
            String pertanyaan = taPertanyaan.getText();

            boolean isDuplicate = model.getQuestions().stream().anyMatch(q -> q.getId() == id);
            if (isDuplicate) {
                showAlert(Alert.AlertType.WARNING, "ID Duplikat", "ID " + id + " sudah ada. ID akan diatur ulang.");
                initialize(null, null);
                return;
            }

            model.addQuestion(new Question(id, pertanyaan, skor, selectKategori));
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pertanyaan berhasil ditambahkan!");
            ((Stage) tfId.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "ID dan Skor harus berupa angka!");
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
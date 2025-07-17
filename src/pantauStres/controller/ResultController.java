package pantauStres.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pantauStres.model.Answer; // Sesuaikan import
import pantauStres.services.AnswerManager; // Sesuaikan import

public class ResultController implements Initializable {

    // Gunakan AnswerManager yang sesuai
    private AnswerManager resultManager = new AnswerManager();
    private ObservableList<Answer> resultData;

    @FXML private TableView<Answer> tableView;
    // ... (FXML untuk kolom tabel lainnya) ...
    @FXML private TableColumn<Answer, String> tcId;
    @FXML private TableColumn<Answer, String> tcWaktu;
    @FXML private TableColumn<Answer, Number> tcMood;
    @FXML private TableColumn<Answer, Number> tcSleep;
    @FXML private TableColumn<Answer, String> tcInterpretasi;
    @FXML private TableColumn<Answer, Boolean> tcShare;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ... (Pengaturan cellValueFactory tetap sama) ...
        tcId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        tcWaktu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWaktuTes()));
        tcMood.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSkorMood()));
        tcSleep.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSkorTidur()));
        tcInterpretasi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInterpretasi()));
        tcShare.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isShareData()));
        
        loadDataFromFile();
    }
    
    private void loadDataFromFile() {
        // --- PENYESUAIAN DI SINI ---
        ArrayList<Answer> answers = resultManager.getAnswers();
        resultData = FXCollections.observableArrayList(answers);
        tableView.setItems(resultData);
    }

    @FXML
    private void handleHapusButton(ActionEvent event) {
        Answer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang ingin dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setContentText("Anda yakin ingin menghapus hasil tes dengan ID: " + selected.getId() + "?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // --- PENYESUAIAN DI SINI ---
            // Langsung panggil metode hapusData dari manager
            resultManager.hapusData(selected.getId());
            
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil dihapus.");
            
            // Muat ulang data untuk merefresh tabel
            loadDataFromFile();
            tableView.refresh();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        // ... (kode tidak berubah) ...
    }
}
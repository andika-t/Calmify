package pantauStres.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pantauStres.model.Question;
import pantauStres.services.QuestionModel;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    // Setiap controller membuat instance modelnya sendiri, sesuai pola ModelXML
    private final QuestionModel model = new QuestionModel();
    private ObservableList<Question> data;

    @FXML private TableView<Question> tableview;
    @FXML private TableColumn<Question, Number> tcID;
    @FXML private TableColumn<Question, String> tcQuestion;
    @FXML private TableColumn<Question, Number> tcScore;
    @FXML private TableColumn<Question, String> tcKategori;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup kolom tabel
        tcID.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()));
        tcQuestion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPertanyaan()));
        tcScore.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getSkor()));
        tcKategori.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKategori()));

        // Muat data awal
        refreshTable();
    }

    // Metode ini sekarang penting untuk selalu mendapatkan data terbaru dari file
    private void refreshTable() {
        if (data == null) {
            data = FXCollections.observableArrayList();
            tableview.setItems(data);
        }
        data.setAll(model.getQuestions()); // Selalu baca ulang dari file
    }

    @FXML
    private void buttonTambah(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/TambahView.fxml"));
            Parent root = loader.load();

            // Controller 'Tambah' mandiri dan membuat instance modelnya sendiri
            Stage stage = new Stage();
            stage.setTitle("Tambah Pertanyaan Baru");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable(); // Muat ulang data setelah window ditutup
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonEdit(ActionEvent event) {
        Question selected = tableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Silakan pilih item yang ingin diedit.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/EditView.fxml"));
            Parent root = loader.load();

            EditController controller = loader.getController();
            controller.setData(selected); // Kirim hanya data yang dipilih

            Stage stage = new Stage();
            stage.setTitle("Edit Pertanyaan");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonHapus(ActionEvent event) {
        Question selected = tableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Pilih data yang ingin dihapus.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus pertanyaan ini?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            model.deleteQuestion(selected.getId());
            refreshTable();
            new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus.").showAndWait();
        }
    }
}
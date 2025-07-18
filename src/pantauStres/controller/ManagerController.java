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
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pantauStres.model.Question;
import pantauStres.services.QuestionManager;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    QuestionManager manager = new QuestionManager();
    ObservableList<Question> data = FXCollections.observableArrayList(manager.getQuestions());

    @FXML
    private TableView<Question> tableview;
    @FXML
    private TableColumn<Question, Number> tcID;
    @FXML
    private TableColumn<Question, String> tcQuestion;
    @FXML
    private TableColumn<Question, Number> tcScore;

    private BorderPane mainPane;

    public void setMainPane(BorderPane mainPane){
        this.mainPane = mainPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromXML();

        tcID.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()));
        tcQuestion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPertanyaan()));
        tcScore.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getSkor()));
        tableview.setItems(this.data);

        this.tableview.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        this.tableview.setItems(this.data);
        int rowHeight = 30;
        this.tableview.setPrefHeight(rowHeight * data.size() + 30);
    }

    public void loadDataFromXML() {
        data.setAll(manager.getQuestions());
    }

    @FXML
    private void buttonTambah(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/TambahView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Tambah Pertanyaan Baru");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadDataFromXML();
            this.tableview.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonEdit(ActionEvent event) {
        Question selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int id = selected.getId();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/EditView.fxml"));
                Parent root = loader.load();
                EditController controller = loader.getController();
                Question editData = null;
                
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId() == id) {
                        editData = data.get(i);
                        break;
                    }
                }
                controller.setData(editData);

                Stage stage = new Stage();
                stage.setTitle("Edit Pertanyaan");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                loadDataFromXML();
                this.tableview.refresh();
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Gagal membuka form edit.");
                errorAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih item yang ingin diedit.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void buttonHapus(ActionEvent event) {
        Question selected = tableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih data yang ingin dihapus.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Apakah Anda yakin ingin menghapus pertanyaan dengan ID: " + selected.getId() + "?");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            manager.hapusData(selected.getId());
            loadDataFromXML();
            this.tableview.refresh();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Berhasil");
            info.setHeaderText(null);
            info.setContentText("Data berhasil dihapus.");
            info.showAndWait();
        }
    }
}
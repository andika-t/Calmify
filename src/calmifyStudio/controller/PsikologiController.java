package calmifyStudio.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

import calmifyStudio.model.Content;
import calmifyStudio.model.XmlContentImplement;
import calmifyStudio.util.ArrayList;

import java.io.File;
import java.util.List;

public class PsikologiController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField contentLinkField;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Content> contentTable;
    @FXML
    private TableColumn<Content, String> titleColumn;
    @FXML
    private TableColumn<Content, String> descriptionColumn;
    @FXML
    private TableColumn<Content, String> contentLinkColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;

    private ContentService contentService;
    private ObservableList<Content> contentList;
    private Content selectedContent;

    @FXML
    public void initialize() {
        contentService = new ContentService(new XmlContentImplement("data/contents.xml"));
        contentList = FXCollections.observableArrayList();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contentLinkColumn.setCellValueFactory(new PropertyValueFactory<>("contentLink"));

        contentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedContent = newSelection;
                populateForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            } else {
                selectedContent = null;
                doClearFormLogic();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                addButton.setDisable(false);
            }
        });

        refreshTableView();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void populateForm(Content content) {
        titleField.setText(content.getTitle());
        descriptionArea.setText(content.getDescription());
        contentLinkField.setText(content.getContentLink());
    }

    private void doClearFormLogic() {
        titleField.clear();
        descriptionArea.clear();
        contentLinkField.clear();
        statusLabel.setText("");
        contentTable.getSelectionModel().clearSelection();
        selectedContent = null;
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void clearForm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Bersihkan Form");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin membersihkan semua bidang form?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            doClearFormLogic();
            statusLabel.setText("Form dibersihkan.");
        } else {
            statusLabel.setText("Pembersihan form dibatalkan.");
        }
    }

    @FXML
    private void handleAddContent() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String link = contentLinkField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || link.isEmpty()) {
            statusLabel.setText("Semua bidang harus diisi!");
            return;
        }

        ArrayList<Content> allContents = (ArrayList<Content>) contentService.getAllContents();
        for (int i = 0; i < allContents.size(); i++) {
            Content existingContent = allContents.get(i);
            if (existingContent.getTitle().equalsIgnoreCase(title) &&
                    existingContent.getDescription().equalsIgnoreCase(description) &&
                    existingContent.getContentLink().equalsIgnoreCase(link)) {
                statusLabel.setText("Konten dengan judul, deskripsi, dan link yang sama sudah ada!");
                return;
            }
        }

        Content newContent = new Content(null, title, description, link);
        if (contentService.addContent(newContent)) {
            statusLabel.setText("Konten berhasil diunggah!");
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Unggah Berhasil");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("Konten '" + title + "' berhasil diunggah!");
            infoAlert.showAndWait();

            doClearFormLogic();
            refreshTableView();
        } else {
            statusLabel.setText("Gagal mengunggah konten.");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Unggah Gagal");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Terjadi kesalahan saat mengunggah konten. Silakan coba lagi.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void handleUpdateContent() {
        if (selectedContent == null) {
            statusLabel.setText("Pilih konten yang akan diedit dari tabel.");
            return;
        }

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String link = contentLinkField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || link.isEmpty()) {
            statusLabel.setText("Semua bidang harus diisi!");
            return;
        }

        if (selectedContent.getTitle().equalsIgnoreCase(title) &&
                selectedContent.getDescription().equalsIgnoreCase(description) &&
                selectedContent.getContentLink().equalsIgnoreCase(link)) {

            Alert noChangeAlert = new Alert(Alert.AlertType.INFORMATION);
            noChangeAlert.setTitle("Tidak Ada Perubahan");
            noChangeAlert.setHeaderText(null);
            noChangeAlert.setContentText("Tidak ada perubahan yang terdeteksi pada konten ini.");
            noChangeAlert.showAndWait();
            statusLabel.setText("Pembaruan dibatalkan: Tidak ada perubahan.");
            return;
        }

        ArrayList<Content> allContents = (ArrayList<Content>) contentService.getAllContents();
        for (int i = 0; i < allContents.size(); i++) {
            Content existingContent = allContents.get(i);
            if (!existingContent.getId().equals(selectedContent.getId()) &&
                    existingContent.getTitle().equalsIgnoreCase(title) &&
                    existingContent.getDescription().equalsIgnoreCase(description) &&
                    existingContent.getContentLink().equalsIgnoreCase(link)) {
                statusLabel.setText("Konten lain dengan judul, deskripsi, dan link yang sama sudah ada!");
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Edit Konten");
        alert.setHeaderText("Anda akan memperbarui konten yang dipilih.");
        alert.setContentText("Apakah Anda yakin ingin menyimpan perubahan ini?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedContent.setTitle(title);
            selectedContent.setDescription(description);
            selectedContent.setContentLink(link);

            if (contentService.updateContent(selectedContent)) {
                statusLabel.setText("Konten berhasil diperbarui!");
                doClearFormLogic();
                refreshTableView();
            } else {
                statusLabel.setText("Gagal memperbarui konten.");
            }
        } else {
            statusLabel.setText("Pembaruan konten dibatalkan.");
        }
    }

    @FXML
    private void handleDeleteContent() {
        if (selectedContent == null) {
            statusLabel.setText("Pilih konten yang akan dihapus dari tabel.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Konten");
        alert.setHeaderText("Anda akan menghapus konten yang dipilih.");
        alert.setContentText("Apakah Anda yakin ingin menghapus konten '" + selectedContent.getTitle() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (contentService.deleteContent(selectedContent.getId())) {
                statusLabel.setText("Konten berhasil dihapus!");
                doClearFormLogic();
                refreshTableView();
            } else {
                statusLabel.setText("Gagal menghapus konten.");
            }
        } else {
            statusLabel.setText("Penghapusan konten dibatalkan.");
        }
    }

    private void refreshTableView() {
        ArrayList<Content> customArrayList = (ArrayList<Content>) contentService.getAllContents();

        java.util.List<Content> javaUtilList = new java.util.ArrayList<>();
        for (int i = 0; i < customArrayList.size(); i++) {
            javaUtilList.add(customArrayList.get(i));
        }
        contentList.setAll(javaUtilList);
        contentTable.setItems(contentList);
    }
}

package calmifyStudio.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import calmifyStudio.model.Content;
import calmifyStudio.model.XmlContentImplement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
                clearForm();
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

    @FXML
    private void clearForm() {
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
    private void handleAddContent() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String link = contentLinkField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || link.isEmpty()) {
            statusLabel.setText("Semua bidang harus diisi!");
            return;
        }

        List<Content> allContents = contentService.getAllContents();
        for (Content existingContent : allContents) {
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
            clearForm();
            refreshTableView();
        } else {
            statusLabel.setText("Gagal mengunggah konten.");
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

        List<Content> allContents = contentService.getAllContents();
        for (Content existingContent : allContents) {
            if (!existingContent.getId().equals(selectedContent.getId()) &&
                existingContent.getTitle().equalsIgnoreCase(title) &&
                existingContent.getDescription().equalsIgnoreCase(description) &&
                existingContent.getContentLink().equalsIgnoreCase(link)) {
                statusLabel.setText("Konten lain dengan judul, deskripsi, dan link yang sama sudah ada!");
                return; 
            }
        }

        selectedContent.setTitle(title);
        selectedContent.setDescription(description);
        selectedContent.setContentLink(link);

        if (contentService.updateContent(selectedContent)) {
            statusLabel.setText("Konten berhasil diperbarui!");
            clearForm();
            refreshTableView();
        } else {
            statusLabel.setText("Gagal memperbarui konten.");
        }
    }

    @FXML
    private void handleDeleteContent() {
        if (selectedContent == null) {
            statusLabel.setText("Pilih konten yang akan dihapus dari tabel.");
            return;
        }

        if (contentService.deleteContent(selectedContent.getId())) {
            statusLabel.setText("Konten berhasil dihapus!");
            clearForm();
            refreshTableView();
        } else {
            statusLabel.setText("Gagal menghapus konten.");
        }
    }

    private void refreshTableView() {
        contentList.setAll(contentService.getAllContents());
        contentTable.setItems(contentList);
    }
}

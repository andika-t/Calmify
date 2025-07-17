package calmifyStudio.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import calmifyStudio.model.Content;
import calmifyStudio.model.XmlContentImplement;
import javafx.scene.control.Alert; 
import javafx.scene.control.ButtonType; 
import java.util.Optional; 

import java.awt.Desktop; 
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class UserViewController {

    @FXML
    private FlowPane contentDisplayPane;
    @FXML
    private Label statusLabel;

    private ContentService contentService;

    @FXML
    public void initialize() {
        contentService = new ContentService(new XmlContentImplement("src/data/contents.xml"));
        loadAndDisplayContents();
    }
    private void loadAndDisplayContents() {
        contentDisplayPane.getChildren().clear();
        List<Content> contents = contentService.getAllContents();

        if (contents.isEmpty()) {
            statusLabel.setText("Belum ada konten relaksasi yang tersedia.");
            return;
        }

        for (Content content : contents) {
            VBox contentCard = createContentCard(content);
            contentDisplayPane.getChildren().add(contentCard);
        }
        statusLabel.setText("Menampilkan " + contents.size() + " konten relaksasi.");
    }

    private VBox createContentCard(Content content) {
        VBox card = new VBox(5);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        Label titleLabel = new Label(content.getTitle());
        titleLabel.setFont(new Font("System Bold", 14));
        titleLabel.setWrapText(true);

        Label descriptionLabel = new Label(content.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 12px;");

        Hyperlink link = new Hyperlink("Tonton Konten");
        link.setOnAction(event -> confirmAndOpenLink(content.getTitle(), content.getContentLink()));

        card.getChildren().addAll(titleLabel, descriptionLabel, link);
        return card;
    }

    private void confirmAndOpenLink(String title, String url) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Pembukaan Konten");
        alert.setHeaderText("Anda akan membuka konten eksternal.");
        alert.setContentText("Apakah Anda yakin ingin membuka '" + title + "' di browser?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            openLink(url);
        } else {
            statusLabel.setText("Pembukaan konten dibatalkan.");
        }
    }

    private void openLink(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                statusLabel.setText("Gagal membuka tautan: Fitur browser tidak didukung.");
                System.err.println("Desktop browsing not supported.");
            }
        } catch (IOException | URISyntaxException e) {
            statusLabel.setText("Gagal membuka tautan: " + e.getMessage());
            System.err.println("Error opening link: " + e.getMessage());
        }
    }
}

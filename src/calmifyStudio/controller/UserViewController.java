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
import javafx.scene.web.WebView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class UserViewController {

    @FXML
    private FlowPane contentDisplayPane;
    @FXML
    private Label statusLabel;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private StackPane webViewContainer;
    @FXML
    private WebView webView;
    @FXML
    private Button closeWebViewButton;

    private ContentService contentService;

    @FXML
    public void initialize() {
        contentService = new ContentService(new XmlContentImplement("data/contents.xml"));
        loadAndDisplayContents();

        webViewContainer.setVisible(false);
        webViewContainer.setManaged(false);
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
        card.setStyle("-fx-background-color: #f9f9f9; " +
                "-fx-border-color: #ccc; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 0);");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; " +
                "-fx-border-color: #d0d0d0; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 0); " +
                "-fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #f9f9f9; " +
                "-fx-border-color: #ccc; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 0);"));

        Label titleLabelPrefix = new Label("Judul:");
        titleLabelPrefix.setFont(new Font("System Bold", 12));
        Label titleLabel = new Label(content.getTitle());
        titleLabel.setFont(new Font("System Bold", 14));
        titleLabel.setWrapText(true);
        titleLabel.setMaxHeight(40);
        VBox titleBox = new VBox(0, titleLabelPrefix, titleLabel);

        Label descriptionLabelPrefix = new Label("Deskripsi:");
        descriptionLabelPrefix.setFont(new Font("System Bold", 12));
        Label descriptionLabel = new Label(content.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");
        VBox descriptionBox = new VBox(0, descriptionLabelPrefix, descriptionLabel);
        VBox.setVgrow(descriptionBox, javafx.scene.layout.Priority.ALWAYS);

        Label contentLinkPrefix = new Label("Link Konten:");
        contentLinkPrefix.setFont(new Font("System Bold", 12));
        Hyperlink link = new Hyperlink("Tonton Konten");
        link.setStyle("-fx-text-fill: #007bff; -fx-underline: true;");
        link.setOnAction(event -> confirmAndOpenLink(content.getTitle(), content.getContentLink()));
        VBox linkBox = new VBox(0, contentLinkPrefix, link);

        card.getChildren().addAll(titleBox, descriptionBox, linkBox);
        return card;
    }

    private void confirmAndOpenLink(String title, String url) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Pembukaan Konten");
        alert.setHeaderText("Anda akan membuka konten eksternal di dalam aplikasi.");
        alert.setContentText("Apakah Anda yakin ingin membuka '" + title + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            openLinkInWebView(url);
        } else {
            statusLabel.setText("Pembukaan konten dibatalkan.");
        }
    }

    private void openLinkInWebView(String url) {
        try {
            new URI(url);

            webView.getEngine().load(url);
            webViewContainer.setVisible(true);
            webViewContainer.setManaged(true);
            statusLabel.setText("Membuka: " + url);
        } catch (URISyntaxException e) {
            statusLabel.setText("Gagal membuka tautan: Format URL tidak valid.");
            System.err.println("Error opening link (invalid URL format): " + e.getMessage());
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Kesalahan URL");
            errorAlert.setHeaderText("Format URL tidak valid.");
            errorAlert.setContentText("Tautan yang diberikan tidak dalam format yang benar: " + url);
            errorAlert.showAndWait();
        } catch (Exception e) {
            statusLabel.setText("Gagal membuka tautan: " + e.getMessage());
            System.err.println("Error opening link in WebView: " + e.getMessage());
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Kesalahan Pembukaan Konten");
            errorAlert.setHeaderText("Terjadi kesalahan saat mencoba membuka konten.");
            errorAlert.setContentText("Detail: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void handleCloseWebView() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Tutup Konten");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menutup tampilan konten ini?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            webView.getEngine().load(null);
            webViewContainer.setVisible(false);
            webViewContainer.setManaged(false);
            statusLabel.setText("Tampilan konten ditutup.");
        } else {
            statusLabel.setText("Penutupan konten dibatalkan.");
        }
    }
}

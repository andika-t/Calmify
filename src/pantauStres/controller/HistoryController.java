package pantauStres.controller;

import authenticator.model.User;
import authenticator.services.UserManager;
import home.controller.MainHomeController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import pantauStres.model.Answer;
import pantauStres.services.AnswerModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HistoryController {

    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CheckBox cbMood;
    @FXML
    private CheckBox cbSleep;
    @FXML
    private CheckBox cbBagikan;
    @FXML
    private TableView<Answer> tableView;
    @FXML
    private TableColumn<Answer, String> tcWaktu;
    @FXML
    private TableColumn<Answer, Integer> tcMood;
    @FXML
    private TableColumn<Answer, Integer> tcSleep;
    @FXML
    private TableColumn<Answer, String> tcInterpretasi;
    @FXML
    private TableColumn<Answer, Boolean> tcShare;

    private final AnswerModel answerModel = new AnswerModel();
    private User currentUser;
    private ObservableList<Answer> tableData;
    private BorderPane mainPane;
    private MainHomeController mainController;

    public void setData(User user) {
        this.currentUser = user;
        if (this.currentUser == null) {
            lineChart.setTitle("Gagal memuat riwayat: Pengguna tidak ditemukan.");
            return;
        }
        initializeUI();
        handleRefresh(null);
    }

    private void initializeUI() {
        tcWaktu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWaktuTes()));

        tcMood.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSkorMood()).asObject());
        tcSleep.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getSkorKualitas()).asObject());
        tcInterpretasi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInterpretasi()));
        tcShare.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isShareData()));

        tableData = FXCollections.observableArrayList();
        tableView.setItems(tableData);

        boolean hasConsented = UserManager.getShareData(currentUser.getUsername());
        cbBagikan.setSelected(hasConsented);
    }

    private void populateChart(List<Answer> userAnswers) {
        lineChart.getData().clear();

        if (userAnswers.isEmpty()) {
            lineChart.setTitle("Belum ada riwayat asesmen untuk " + currentUser.getUsername());
            return;
        }
        lineChart.setTitle("Rata-Rata Skor Harian");

        Map<String, List<Answer>> groupedByDay = userAnswers.stream()
                .collect(Collectors.groupingBy(
                        answer -> answer.getWaktuTes().substring(0, 10),
                        LinkedHashMap::new,
                        Collectors.toList()));

        XYChart.Series<String, Number> moodSeries = new XYChart.Series<>();
        moodSeries.setName("Rata-rata Skor Mood");
        XYChart.Series<String, Number> sleepSeries = new XYChart.Series<>();
        sleepSeries.setName("Rata-rata Skor Tidur");

        for (Map.Entry<String, List<Answer>> entry : groupedByDay.entrySet()) {
            String day = entry.getKey();
            double avgMood = entry.getValue().stream().mapToInt(Answer::getSkorMood).average().orElse(0);
            double avgSleep = entry.getValue().stream().mapToInt(Answer::getSkorKualitas).average().orElse(0);
            moodSeries.getData().add(new XYChart.Data<>(day, avgMood));
            sleepSeries.getData().add(new XYChart.Data<>(day, avgSleep));
        }

        if (cbMood.isSelected())
            lineChart.getData().add(moodSeries);
        if (cbSleep.isSelected())
            lineChart.getData().add(sleepSeries);
    }

    private void populateTable(List<Answer> userAnswers) {
        userAnswers.sort(Comparator.comparing(Answer::getWaktuTes).reversed());
        tableData.setAll(userAnswers);
    }

    @FXML
    private void handleFilterChange(ActionEvent event) {
        handleRefresh(event);
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        if (currentUser == null)
            return;

        List<Answer> userAnswers = answerModel.getAnswers().stream()
                .filter(answer -> currentUser.getUsername().equals(answer.getUsername()))
                .collect(Collectors.toList());

        populateChart(userAnswers);
        populateTable(userAnswers);
    }

    @FXML
    private void handleShareConsent(ActionEvent event) {
        if (currentUser == null)
            return;
        boolean consentGiven = cbBagikan.isSelected();
        UserManager.updateShareData(currentUser.getUsername(), consentGiven);
        for (Answer answer : tableData) {
            answer.setShareData(consentGiven);
        }
        tableView.refresh();
        String status = consentGiven ? "diizinkan" : "dibatalkan";
        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Persetujuan berbagi data telah " + status + ".");
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
        confirm.setContentText("Anda yakin ingin menghapus hasil tes ini?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            answerModel.deleteAnswer(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil dihapus.");
            handleRefresh(null);
        }
    }

    @FXML
    private void handleHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantauStres/view/UserAssessmentView.fxml"));
            Pane root = loader.load();
            UserAssessmentController controller = loader.getController();
            controller.setData(mainPane, currentUser, mainController);
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
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
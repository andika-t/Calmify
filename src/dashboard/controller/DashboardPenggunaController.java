package dashboard.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn; // Tambahkan import ini
import javafx.scene.control.TableView; // Tambahkan import ini
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox; // Import VBox
import pantauStres.model.Answer;
import pantauStres.services.AnswerModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // Import Parent
import java.io.IOException; // Import IOException
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import authenticator.model.User;
import home.controller.MainHomeController;
import javafx.fxml.Initializable;

public class DashboardPenggunaController implements Initializable {

    @FXML
    private Label labelFullname;

    @FXML
    private Label labelPoint;

    @FXML
    private Button btnBell;

    @FXML
    private Button btnChat;
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
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> tcWaktu;
    @FXML
    private TableColumn<?, ?> tcMood;
    @FXML
    private TableColumn<?, ?> tcSleep;

    @FXML
    private VBox paneStudio; // Deklarasikan fx:id untuk paneStudio

    @FXML
    private VBox panePantauStres;

    private BorderPane mainPane;
    private User currentUser;
    private MainHomeController mainController;
    private final AnswerModel answerModel = new AnswerModel();

    public void setData(BorderPane mainPane, User user, MainHomeController mainController) {
        this.mainPane = mainPane;
        this.currentUser = user;
        this.mainController = mainController;

        if (this.currentUser != null) {
            labelFullname.setText(currentUser.getFullName());
            labelPoint.setText(String.valueOf(currentUser.getTotalPoints()));
            populateDashboardCharts();
            setupChartFilterListeners();
        } else {
            labelFullname.setText("Pengguna Tamu");
            labelPoint.setText("0 Points");
            lineChart.setTitle("Tidak ada data pengguna.");
        }

        refreshUIData();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelFullname.setText("Memuat...");
        labelPoint.setText("Memuat...");

        xAxis.setLabel("Tanggal Tes");
        yAxis.setLabel("Rata-Rata Skor");
        lineChart.setTitle("Rata-Rata Skor Stres Harian"); // Judul default

        loadStudioContent();
        populateDashboardCharts();
    }

    private void populateDashboardCharts() {
        lineChart.getData().clear();
        if (currentUser == null) {
            lineChart.setTitle("Gagal memuat riwayat: Pengguna tidak ditemukan.");
            return;
        }

        List<Answer> userAnswers = answerModel.getAnswers().stream()
                .filter(answer -> currentUser.getUsername().equals(answer.getUsername()))

                .sorted(Comparator.comparing(Answer::getWaktuTes))
                .collect(Collectors.toList());

        if (userAnswers.isEmpty()) {
            lineChart.setTitle("Belum ada riwayat asesmen untuk " + currentUser.getUsername());
            return;
        }

        Map<String, List<Answer>> groupedByDay = userAnswers.stream()
                .collect(Collectors.groupingBy(
                        answer -> answer.getWaktuTes().substring(0, 10), // Ambil YYYY-MM-DD
                        LinkedHashMap::new, // Penting untuk menjaga urutan hari
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

        lineChart.getData().add(moodSeries);
        lineChart.getData().add(sleepSeries);
        handleFilterChange();
    }

    private void setupChartFilterListeners() {
        if (cbMood != null) {
            cbMood.selectedProperty().addListener((obs, oldVal, newVal) -> handleFilterChange());
        }
        if (cbSleep != null) {
            cbSleep.selectedProperty().addListener((obs, oldVal, newVal) -> handleFilterChange());
        }
    }

    @FXML
    private void handleFilterChange() {
        if (lineChart.getData().isEmpty()) {
            return;
        }

        XYChart.Series<String, Number> moodSeries = null;
        XYChart.Series<String, Number> sleepSeries = null;

        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            if ("Rata-rata Skor Mood".equals(s.getName())) {
                moodSeries = s;
            } else if ("Rata-rata Skor Tidur".equals(s.getName())) {
                sleepSeries = s;
            }
        }

        // Atur visibilitas series dan node-nya
        if (moodSeries != null && cbMood != null) {
            moodSeries.getNode().setVisible(cbMood.isSelected());
            for (XYChart.Data<String, Number> data : moodSeries.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setVisible(cbMood.isSelected());
                }
            }
        }
        if (sleepSeries != null && cbSleep != null) {
            sleepSeries.getNode().setVisible(cbSleep.isSelected());
            for (XYChart.Data<String, Number> data : sleepSeries.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setVisible(cbSleep.isSelected());
                }
            }
        }
    }

    public void refreshUIData() {
        if (currentUser == null) {
            System.err.println("Error: currentUser belum diatur. Tidak bisa refresh UI.");
            return;
        }
        List<Answer> userAnswers = answerModel.getAnswers().stream()
                .filter(answer -> currentUser.getUsername().equals(answer.getUsername()))
                .collect(Collectors.toList());

        int totalPoin = userAnswers.stream().mapToInt(ans -> ans.getSkorMood() + ans.getSkorKualitas()).sum();
        labelPoint.setText(totalPoin + " Poin");
    }

    private void loadStudioContent() {
        try {
            URL studioContentUrl = getClass().getResource("/calmifyStudio/view/UserView.fxml");
            if (studioContentUrl == null) {
                System.err.println("StudioContent.fxml not found! Check path.");
                return;
            }
            Parent studioContent = FXMLLoader.load(studioContentUrl);
            paneStudio.getChildren().add(studioContent);
        } catch (IOException e) {
            System.err.println("Gagal memuat StudioContent.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
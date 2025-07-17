package pantauStres.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import pantauStres.model.Answer;
import pantauStres.services.AnswerManager; // Pastikan ini menunjuk ke AnswerManager Anda

public class HistoryChartController implements Initializable {

    @FXML private LineChart<String, Number> lineChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private CheckBox cbMood;
    @FXML private CheckBox cbSleep;

    private AnswerManager answerManager = new AnswerManager();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateDailyChart();
    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        populateDailyChart();
    }
    
    @FXML
    private void handleFilterChange(ActionEvent event) {
        populateDailyChart();
    }

    private void populateDailyChart() {
        lineChart.getData().clear();
        
        // --- PENYESUAIAN DI SINI ---
        // Langsung panggil metode getAnswers() dari manager Anda
        ArrayList<Answer> allAnswers = answerManager.getAnswers();
        
        if (allAnswers.isEmpty()) {
            lineChart.setTitle("Belum ada riwayat assessment untuk ditampilkan");
            return;
        } else {
            lineChart.setTitle("Rata-Rata Skor Harian");
        }
        
        // (Sisa logika untuk mengelompokkan data dan menggambar grafik tetap sama)
        Map<String, List<Answer>> groupedByDay = allAnswers.stream()
            .collect(Collectors.groupingBy(
                answer -> answer.getWaktuTes().substring(0, 10),
                LinkedHashMap::new,
                Collectors.toList()
            ));

        XYChart.Series<String, Number> moodSeries = new XYChart.Series<>();
        moodSeries.setName("Rata-rata Skor Mood");
        XYChart.Series<String, Number> sleepSeries = new XYChart.Series<>();
        sleepSeries.setName("Rata-rata Skor Tidur");

        for (Map.Entry<String, List<Answer>> entry : groupedByDay.entrySet()) {
            String day = entry.getKey();
            List<Answer> dailyAnswers = entry.getValue();

            double avgMood = dailyAnswers.stream().mapToInt(Answer::getSkorMood).average().orElse(0);
            double avgSleep = dailyAnswers.stream().mapToInt(Answer::getSkorTidur).average().orElse(0);

            moodSeries.getData().add(new XYChart.Data<>(day, avgMood));
            sleepSeries.getData().add(new XYChart.Data<>(day, avgSleep));
        }

        if (cbMood.isSelected()) {
            lineChart.getData().add(moodSeries);
        }
        if (cbSleep.isSelected()) {
            lineChart.getData().add(sleepSeries);
        }
    }
}
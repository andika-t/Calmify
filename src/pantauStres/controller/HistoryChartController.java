package pantauStres.controller;

import authenticator.model.User;
import authenticator.services.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import pantauStres.model.Answer;
import pantauStres.services.AnswerModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryChartController {

    @FXML private LineChart<String, Number> lineChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private CheckBox cbMood;
    @FXML private CheckBox cbSleep;
    @FXML private CheckBox cbBagikan;
    
    private final AnswerModel answerModel = new AnswerModel();
    private User currentUser;

    public void setData(User user) {
        this.currentUser = user;
        if (this.currentUser == null) {
            lineChart.setTitle("Gagal memuat riwayat: Pengguna tidak ditemukan.");
            return;
        }
        
        boolean hasConsented = UserManager.getShareData(currentUser.getUsername());
        cbBagikan.setSelected(hasConsented);

        populateDailyChart();
    }

    private void populateDailyChart() {
        if (currentUser == null) return;
        
        lineChart.getData().clear();
        ArrayList<Answer> allAnswers = answerModel.getAnswers();
        List<Answer> userAnswers = allAnswers.stream()
                .filter(answer -> currentUser.getUsername().equals(answer.getUsername()))
                .collect(Collectors.toList());
        
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

        if (cbMood.isSelected()) lineChart.getData().add(moodSeries);
        if (cbSleep.isSelected()) lineChart.getData().add(sleepSeries);
    }

    @FXML private void handleFilterChange(ActionEvent event) {
        populateDailyChart();
    }

    @FXML private void handleRefreshButton(ActionEvent event) {
        populateDailyChart();
    }

    @FXML
    private void handleShareConsent(ActionEvent event) {
        if (currentUser == null) return;
        boolean consentGiven = cbBagikan.isSelected();
        UserManager.updateShareData(currentUser.getUsername(), consentGiven);
    }
}
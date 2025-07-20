package selfCare.controller;

import authenticator.model.User;
import selfCare.model.SelfCareActivity;
import selfCare.model.SelfCareMission;
import selfCare.model.SelfCarePointHistory;
import selfCare.model.SelfCareUser;
import selfCare.services.ISelfCareUserService;
import selfCare.services.SelfCareUserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SCUserViewController {
    @FXML private Label userNameLabel;
    @FXML private Label totalPointsLabel;
    @FXML private Label currentLevelOrBadgeLabel;
    @FXML private CheckBox shareDataCheckBox;
    @FXML private ComboBox<SelfCareActivity> activityComboBox;
    @FXML private TableView<SelfCarePointHistory> historyTable;
    @FXML private TableColumn<SelfCarePointHistory, String> colHistoryActivity;
    @FXML private TableColumn<SelfCarePointHistory, Integer> colHistoryPoints;
    @FXML private TableColumn<SelfCarePointHistory, String> colHistoryTimestamp;
    @FXML private TableView<SelfCareMission> assignedMissionsTable;
    @FXML private TableColumn<SelfCareMission, String> colMissionName;
    @FXML private TableColumn<SelfCareMission, String> colMissionAssignedDate;
    @FXML private TableColumn<SelfCareMission, String> colMissionStatus;
    @FXML private TableColumn<SelfCareMission, String> colMissionCompletionDate;

    private final ISelfCareUserService selfCareService = SelfCareUserService.getInstance();
    private SelfCareUser selfCareUser;
    private User loggedInUser;
    private ObservableList<SelfCarePointHistory> historyObservableList;
    private ObservableList<SelfCareMission> assignedMissionsObservableList;

    @FXML
    public void initialize() {
        historyObservableList = FXCollections.observableArrayList();
        assignedMissionsObservableList = FXCollections.observableArrayList();
        historyTable.setItems(historyObservableList);
        assignedMissionsTable.setItems(assignedMissionsObservableList);
        setupTableColumns();
        loadAvailableActivities();
    }
    
    public void setData(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        if (this.loggedInUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User tidak valid.");
            return;
        }
        this.selfCareUser = selfCareService.getOrCreateUser(
            this.loggedInUser.getUsername(),
            this.loggedInUser.getFirstName() + " " + this.loggedInUser.getLastName()
        );
        updateAllUI();
    }

    private void setupTableColumns() {
        colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("pointsGained"));
        colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
        colMissionName.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colMissionAssignedDate.setCellValueFactory(new PropertyValueFactory<>("formattedAssignedDate"));
        colMissionStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().isCompleted() ? "Selesai" : "Tertunda"));
        colMissionCompletionDate.setCellValueFactory(new PropertyValueFactory<>("formattedCompletionDate"));
    }

    private void loadAvailableActivities() {
        activityComboBox.setItems(FXCollections.observableArrayList(
            new SelfCareActivity("act001", "Meditasi 10 Menit", "Luangkan waktu...", 10),
            new SelfCareActivity("act002", "Jurnal Syukur", "Tulis 3 hal...", 15)
        ));
    }

    private void updateAllUI() {
        if (selfCareUser == null) return;
        userNameLabel.setText("Selamat datang, " + selfCareUser.getName());
        totalPointsLabel.setText(String.valueOf(selfCareUser.getTotalPoints()));
        currentLevelOrBadgeLabel.setText(selfCareUser.getCurrentLevelOrBadge());
        shareDataCheckBox.setSelected(selfCareUser.isShareData());

        List<SelfCarePointHistory> history = new ArrayList<>(selfCareUser.getPointHistory());
        history.sort(Comparator.comparingLong(SelfCarePointHistory::getTimestamp).reversed());
        historyObservableList.setAll(history);
        
        List<SelfCareMission> missions = new ArrayList<>(selfCareUser.getAssignedMissions());
        missions.sort((m1, m2) -> {
            int status = Boolean.compare(m1.isCompleted(), m2.isCompleted());
            if (status != 0) return status;
            return Long.compare(m2.getAssignedDate(), m1.getAssignedDate());
        });
        assignedMissionsObservableList.setAll(missions);
    }

    @FXML
    private void handleShareDataChanged() {
        if (selfCareUser == null) return;
        selfCareUser.setShareData(shareDataCheckBox.isSelected());
        if (selfCareService.updateUser(selfCareUser)) {
            showAlert(Alert.AlertType.INFORMATION, "Pengaturan Disimpan", "Status berbagi data Anda telah diperbarui.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan pengaturan.");
        }
    }

    @FXML
    private void handleCompleteActivity() {
        SelfCareActivity selected = activityComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih aktivitas.");
            return;
        }
        selfCareService.addPointsToUser(selfCareUser.getUsername(), selected.getDefaultPoints(), selected.getName())
            .ifPresent(updatedUser -> {
                this.selfCareUser = updatedUser;
                updateAllUI();
            });
    }

    @FXML
    private void handleCompleteAssignedMission() {
        SelfCareMission selected = assignedMissionsTable.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isCompleted()) return;
        selfCareService.completeAssignedMission(selfCareUser.getUsername(), selected.getId())
            .ifPresent(updatedUser -> {
                this.selfCareUser = updatedUser;
                showAlert(Alert.AlertType.INFORMATION, "Misi Selesai!", "Kerja bagus!");
                updateAllUI();
            });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
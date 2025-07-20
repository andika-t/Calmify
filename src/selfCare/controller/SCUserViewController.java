package selfCare.controller;

import selfCare.model.*;
import selfCare.services.ISelfCareUserService;
import selfCare.services.SelfCareUserService;
import authenticator.model.User;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SCUserViewController {
    private ISelfCareUserService selfCareService;
    private User currentUser;
    private SelfCareUser selfCareData;

    @FXML private Label userNameLabel, totalPointsLabel, currentLevelOrBadgeLabel;
    @FXML private CheckBox shareDataCheckBox;
    @FXML private ComboBox<SelfCareActivity> activityComboBox;
    @FXML private TableView<PointHistory> historyTable;
    @FXML private TableColumn<PointHistory, String> colHistoryActivity;
    @FXML private TableColumn<PointHistory, Integer> colHistoryPoints;
    @FXML private TableColumn<PointHistory, String> colHistoryTimestamp;
    @FXML private TableView<AssignedMission> assignedMissionsTable;
    @FXML private TableColumn<AssignedMission, String> colMissionName;
    @FXML private TableColumn<AssignedMission, String> colMissionStatus;
    @FXML private TableColumn<AssignedMission, String> colMissionAssignedDate;
    @FXML private TableColumn<AssignedMission, String> colMissionCompletionDate;

    @FXML
    public void initialize() {
        this.selfCareService = SelfCareUserService.getInstance();
        setupTableBindings();
        populateActivityList();
    }

    public void setData(User user) {
        this.currentUser = user;
        if (currentUser == null) return;
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        this.selfCareData = selfCareService.getOrCreateUser(currentUser.getUsername(), fullName, currentUser.getUserType());
        userNameLabel.setText("Selamat datang, " + fullName);
        refreshUI();
    }

    private void setupTableBindings() {
        colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
        colMissionName.setCellValueFactory(new PropertyValueFactory<>("missionName"));
        colMissionStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colMissionAssignedDate.setCellValueFactory(new PropertyValueFactory<>("formattedAssignedDate"));
        colMissionCompletionDate.setCellValueFactory(new PropertyValueFactory<>("formattedCompletionDate"));
    }
    
    private void populateActivityList() {
        List<SelfCareActivity> activities = new ArrayList<>();
        activities.add(new SelfCareActivity("Meditasi 10 menit", 15));
        activities.add(new SelfCareActivity("Jalan pagi 30 menit", 20));
        activities.add(new SelfCareActivity("Membaca buku non-fiksi", 10));
        activityComboBox.setItems(FXCollections.observableArrayList(activities));
    }

    private void refreshUI() {
        totalPointsLabel.setText(String.valueOf(selfCareData.getTotalPoints()));
        currentLevelOrBadgeLabel.setText(selfCareData.getLevel());
        shareDataCheckBox.setSelected(selfCareData.isShareData());
        historyTable.setItems(FXCollections.observableArrayList(selfCareData.getPointHistory()));
        assignedMissionsTable.setItems(FXCollections.observableArrayList(selfCareData.getAssignedMissions()));
    }

    @FXML
    private void handleCompleteActivity() {
        SelfCareActivity selected = activityComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Peringatan", "Pilih aktivitas."); return; }
        selfCareService.addPointsToUser(currentUser.getUsername(), selected.getPoints(), selected.getName());
        showAlert("Sukses", "Selamat! Anda mendapatkan " + selected.getPoints() + " poin.");
        refreshUI();
    }

    @FXML
    private void handleShareDataChanged() {
        selfCareData.setShareData(shareDataCheckBox.isSelected());
        selfCareService.updateUser(selfCareData);
        showAlert("Info", "Pengaturan berbagi data telah diperbarui.");
    }

    @FXML
    private void handleCompleteAssignedMission() {
        AssignedMission selected = assignedMissionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Peringatan", "Pilih misi."); return; }
        if (selected.getStatus() == AssignedMission.MissionStatus.SELESAI) { showAlert("Info", "Misi ini sudah selesai."); return; }
        selfCareService.completeAssignedMission(currentUser.getUsername(), selected.getMissionName());
        showAlert("Selamat!", "Misi '" + selected.getMissionName() + "' selesai! Anda mendapat poin bonus.");
        refreshUI();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
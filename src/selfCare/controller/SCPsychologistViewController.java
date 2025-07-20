package selfCare.controller;

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
import javafx.scene.layout.VBox;
import java.util.Comparator;
import java.util.List;

public class SCPsychologistViewController {
    private final ISelfCareUserService selfCareService = SelfCareUserService.getInstance();

    @FXML private TextField userNameField;
    @FXML private VBox leaderboardContainer;
    @FXML private ComboBox<SelfCareUser> assignUserComboBox;
    @FXML private ComboBox<SelfCareActivity> assignActivityComboBox;
    @FXML private TableView<SelfCareUser> userTable;
    @FXML private TableColumn<SelfCareUser, String> colUserId;
    @FXML private TableColumn<SelfCareUser, String> colUserName;
    @FXML private TableColumn<SelfCareUser, Integer> colUserPoints;
    @FXML private TableColumn<SelfCareUser, String> colUserLevel;
    @FXML private Label detailUserIdLabel;
    @FXML private Label detailUserNameLabel;
    @FXML private Label detailUserPointsLabel;
    @FXML private Label detailUserLevelLabel;
    @FXML private TableView<SelfCarePointHistory> historyTable;
    @FXML private TableColumn<SelfCarePointHistory, String> colHistoryActivity;
    @FXML private TableColumn<SelfCarePointHistory, Integer> colHistoryPoints;
    @FXML private TableColumn<SelfCarePointHistory, String> colHistoryTimestamp;
    @FXML private TableView<SelfCareMission> assignedMissionsTable;
    @FXML private TableColumn<SelfCareMission, String> colAssignedMissionName;
    @FXML private TableColumn<SelfCareMission, String> colAssignedMissionAssignedDate;
    @FXML private TableColumn<SelfCareMission, String> colAssignedMissionStatus;
    @FXML private TableColumn<SelfCareMission, String> colAssignedMissionCompletionDate;

    private ObservableList<SelfCareUser> userObservableList;
    private ObservableList<SelfCarePointHistory> historyObservableList;
    private ObservableList<SelfCareMission> assignedMissionsObservableList;

    @FXML
    public void initialize() {
        setupLists();
        setupTableColumns();
        userTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> showUserDetails(newVal));
        loadAvailableActivities();
        refreshUI();
    }

    private void setupLists() {
        userObservableList = FXCollections.observableArrayList();
        historyObservableList = FXCollections.observableArrayList();
        assignedMissionsObservableList = FXCollections.observableArrayList();
        userTable.setItems(userObservableList);
        historyTable.setItems(historyObservableList);
        assignedMissionsTable.setItems(assignedMissionsObservableList);
        assignUserComboBox.setItems(userObservableList);
    }

    private void setupTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUserPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        colUserLevel.setCellValueFactory(new PropertyValueFactory<>("currentLevelOrBadge"));
        colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("pointsGained"));
        colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
        colAssignedMissionName.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colAssignedMissionAssignedDate.setCellValueFactory(new PropertyValueFactory<>("formattedAssignedDate"));
        colAssignedMissionStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().isCompleted() ? "Selesai" : "Tertunda"));
        colAssignedMissionCompletionDate.setCellValueFactory(new PropertyValueFactory<>("formattedCompletionDate"));
    }
    
    private void loadAvailableActivities() {
        assignActivityComboBox.setItems(FXCollections.observableArrayList(
            new SelfCareActivity("act01", "Meditasi Pagi", "Latihan meditasi singkat.", 10),
            new SelfCareActivity("act02", "Jalan Kaki", "Jalan santai di luar ruangan.", 15)
        ));
    }
    
    private void showUserDetails(SelfCareUser user) {
        if (user != null) {
            userNameField.setText(user.getName());
            detailUserIdLabel.setText(user.getUsername());
            detailUserNameLabel.setText(user.getName());
            detailUserPointsLabel.setText(String.valueOf(user.getTotalPoints()));
            detailUserLevelLabel.setText(user.getCurrentLevelOrBadge());
            historyObservableList.setAll(user.getPointHistory());
            assignedMissionsObservableList.setAll(user.getAssignedMissions());
        } else {
            clearUserDetails();
        }
    }
    
    @FXML
    private void refreshUI() {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        List<SelfCareUser> sharedUsers = selfCareService.getSharedDataUsers();
        userObservableList.setAll(sharedUsers);
        updateLeaderboard(sharedUsers);
        
        if (selectedIndex >= 0 && selectedIndex < userObservableList.size()) {
            userTable.getSelectionModel().select(selectedIndex);
        } else {
            clearUserDetails();
        }
    }

    private void updateLeaderboard(List<SelfCareUser> users) {
        leaderboardContainer.getChildren().remove(1, leaderboardContainer.getChildren().size());
        users.stream()
            .sorted(Comparator.comparingInt(SelfCareUser::getTotalPoints).reversed())
            .limit(5)
            .forEach(user -> {
                Label entry = new Label((leaderboardContainer.getChildren().size()) + ". " + user.getName() + " (" + user.getTotalPoints() + " poin)");
                leaderboardContainer.getChildren().add(entry);
            });
    }

    private void clearUserDetails() {
        userNameField.clear();
        detailUserIdLabel.setText("-");
        detailUserNameLabel.setText("-");
        detailUserPointsLabel.setText("-");
        detailUserLevelLabel.setText("-");
        historyObservableList.clear();
        assignedMissionsObservableList.clear();
    }
    
    @FXML
    private void handleAssignMission() {
        SelfCareUser user = assignUserComboBox.getValue();
        SelfCareActivity activity = assignActivityComboBox.getValue();
        if (user == null || activity == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Pilih pengguna dan aktivitas.");
            return;
        }
        if (selfCareService.assignMissionToUser(user.getUsername(), activity.getId(), activity.getName())) {
            showUserDetails(userTable.getSelectionModel().getSelectedItem()); 
        }
    }
    
    // --- PENAMBAHAN @FXML DI SINI ---
    @FXML
    private void handleUpdateUser() {
        SelfCareUser selected = userTable.getSelectionModel().getSelectedItem();
        String newName = userNameField.getText().trim();
        if (selected == null || newName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Pilih pengguna dari tabel dan pastikan nama tidak kosong.");
            return;
        }

        selected.setName(newName);
        if (selfCareService.updateUser(selected)) {
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Nama pengguna berhasil diperbarui.");
            refreshUI();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui data pengguna.");
        }
    }
    
    // Metode ini juga memerlukan @FXML
    @FXML
    private void handleDeleteUser() {
        SelfCareUser selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih pengguna yang datanya ingin dihapus.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin menghapus data Self-Care untuk " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES && selfCareService.deleteUserSelfCareData(selected.getUsername())) {
                refreshUI();
            }
        });
    }

    // Metode ini juga memerlukan @FXML
    @FXML
    private void handleClearInput() {
        userTable.getSelectionModel().clearSelection();
        assignUserComboBox.getSelectionModel().clearSelection();
        assignActivityComboBox.getSelectionModel().clearSelection();
        clearUserDetails();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
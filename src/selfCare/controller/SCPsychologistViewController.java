package selfCare.controller;

import selfCare.model.*;
import selfCare.services.ISelfCareUserService;
import selfCare.services.MainAccountService;
import selfCare.services.SelfCareUserService;
import authenticator.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SCPsychologistViewController {
    private ISelfCareUserService selfCareService;
    private MainAccountService mainAccountService;

    public static class UserDisplay {
        private final String username, fullName, level;
        private final int totalPoints;

        public UserDisplay(String u, String n, int p, String l) {
            username = u;
            fullName = n;
            totalPoints = p;
            level = l;
        }

        public String getUsername() {
            return username;
        }

        public String getFullName() {
            return fullName;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public String getLevel() {
            return level;
        }
    }

    @FXML
    private TableView<UserDisplay> userTable;
    @FXML
    private TableColumn<UserDisplay, String> colUserId, colUserName, colUserLevel;
    @FXML
    private TableColumn<UserDisplay, Integer> colUserPoints;
    @FXML
    private Label detailUserIdLabel, detailUserNameLabel, detailUserPointsLabel, detailUserLevelLabel;
    @FXML
    private TextField userNameField;
    @FXML
    private Button saveNameButton;
    @FXML
    private TableView<PointHistory> historyTable;
    @FXML
    private TableColumn<PointHistory, String> colHistoryActivity;
    @FXML
    private TableColumn<PointHistory, Integer> colHistoryPoints;
    @FXML
    private TableColumn<PointHistory, String> colHistoryTimestamp;
    @FXML
    private TableView<AssignedMission> assignedMissionsTable;
    @FXML
    private ComboBox<UserDisplay> assignUserComboBox;
    @FXML
    private ComboBox<SelfCareActivity> assignActivityComboBox;
    @FXML
    private VBox leaderboardContainer;

    @FXML
    public void initialize() {
        this.selfCareService = SelfCareUserService.getInstance();
        this.mainAccountService = MainAccountService.getInstance();
        setupTableColumns();
        setupTableSelectionListener();
        userNameField.setDisable(true);
        saveNameButton.setDisable(true);
        populateActivityList();
        refreshUI();
    }

    private void populateActivityList() {
        List<SelfCareActivity> activities = new ArrayList<>();
        activities.add(new SelfCareActivity("Meditasi 10 menit", 15));
        activities.add(new SelfCareActivity("Jalan pagi 30 menit", 20));
        activities.add(new SelfCareActivity("Membaca buku non-fiksi", 10));
        assignActivityComboBox.setItems(FXCollections.observableArrayList(activities));
    }

    private void setupTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUserPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        colUserLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
    }

    private void setupTableSelectionListener() {
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, old, item) -> showUserDetails(item));
    }

    @FXML
    public void refreshUI() {
        Map<String, User> allUsersMap = mainAccountService.getAllUsers().stream()
                .collect(Collectors.toMap(User::getUsername, user -> user));
        List<SelfCareUser> sharedSelfCareUsers = selfCareService.getSharedDataUsers();
        List<UserDisplay> displayList = sharedSelfCareUsers.stream()
                .map(scUser -> {
                    User mainUser = allUsersMap.get(scUser.getUsername());
                    String fullName = mainUser != null ? (mainUser.getFirstName() + " " + mainUser.getLastName())
                            : scUser.getName();
                    return new UserDisplay(scUser.getUsername(), fullName, scUser.getTotalPoints(), scUser.getLevel());
                })
                .collect(Collectors.toList());
        ObservableList<UserDisplay> oList = FXCollections.observableArrayList(displayList);
        userTable.setItems(oList);
        assignUserComboBox.setItems(oList);
        Callback<ListView<UserDisplay>, ListCell<UserDisplay>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(UserDisplay item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getFullName());
            }
        };
        assignUserComboBox.setCellFactory(factory);
        assignUserComboBox.setButtonCell(factory.call(null));
        updateLeaderboard(displayList);
        clearUserDetails();
    }

    private void showUserDetails(UserDisplay userDisplay) {
        if (userDisplay == null) {
            clearUserDetails();
            return;
        }
        User mainUser = mainAccountService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(userDisplay.getUsername())).findFirst().orElse(null);
        String userType = mainUser != null ? mainUser.getUserType() : "Pengguna Umum";
        SelfCareUser scUser = selfCareService.getOrCreateUser(userDisplay.getUsername(), userDisplay.getFullName(),
                userType);
        detailUserIdLabel.setText(userDisplay.getUsername());
        detailUserNameLabel.setText(userDisplay.getFullName());
        detailUserPointsLabel.setText(String.valueOf(userDisplay.getTotalPoints()));
        detailUserLevelLabel.setText(userDisplay.getLevel());
        historyTable.setItems(FXCollections.observableArrayList(scUser.getPointHistory()));
        assignedMissionsTable.setItems(FXCollections.observableArrayList(scUser.getAssignedMissions()));
    }

    @FXML
private void handleAssignMission() {
    UserDisplay selectedUser = assignUserComboBox.getSelectionModel().getSelectedItem();
    SelfCareActivity selectedActivity = assignActivityComboBox.getSelectionModel().getSelectedItem();
    
    if (selectedUser == null || selectedActivity == null) {
        showAlert("Error", "Harap pilih pengguna dan aktivitas untuk misi.");
        return;
    }

    // === PERBAIKAN DIMULAI DI SINI ===
    // Panggil service dan tangkap hasilnya (true jika sukses, false jika gagal)
    boolean success = selfCareService.assignMissionToUser(selectedUser.getUsername(), selectedActivity.getName());
    
    // Periksa hasilnya dan berikan feedback yang sesuai ke pengguna
    if (success) {
        showAlert("Sukses", "Misi '" + selectedActivity.getName() + "' berhasil ditugaskan kepada " + selectedUser.getFullName());
        // Refresh detail view jika pengguna yang sama sedang dipilih
        if (userTable.getSelectionModel().getSelectedItem() != null && 
            userTable.getSelectionModel().getSelectedItem().getUsername().equals(selectedUser.getUsername())) {
            showUserDetails(selectedUser);
        }
    } else {
        // Tampilkan alert jika service mengembalikan false
        showAlert("Gagal", "Terjadi kesalahan saat menugaskan misi. Data pengguna mungkin bermasalah.");
    }
    // === PERBAIKAN SELESAI ===
}

    @FXML
    private void handleDeleteUser() {
        UserDisplay selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data pengguna.");
            return;
        }
        selfCareService.deleteUserSelfCareData(selected.getUsername());
        showAlert("Sukses", "Data self-care untuk " + selected.getFullName() + " telah dihapus.");
        refreshUI();
    }

    @FXML
    private void handleClearInput() {
        userTable.getSelectionModel().clearSelection();
    }

    @FXML 
    private void handleUpdateUser() { 
        // Fitur ini dinonaktifkan. Tidak melakukan apa-apa.
        showAlert("Info", "Fitur edit nama pengguna tidak tersedia dari halaman ini.");
    }

    private void updateLeaderboard(List<UserDisplay> users) {
        leaderboardContainer.getChildren().remove(1, leaderboardContainer.getChildren().size());
        users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getTotalPoints(), u1.getTotalPoints()))
                .limit(5)
                .forEach(u -> leaderboardContainer.getChildren()
                        .add(new Text(String.format("%s (%d poin)", u.getFullName(), u.getTotalPoints()))));
    }

    private void clearUserDetails() {
        detailUserIdLabel.setText("-");
        detailUserNameLabel.setText("-");
        detailUserPointsLabel.setText("-");
        detailUserLevelLabel.setText("-");
        historyTable.getItems().clear();
        assignedMissionsTable.getItems().clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
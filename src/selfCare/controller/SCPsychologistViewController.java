package selfCare.controller;

// --- Impor yang Diperlukan ---
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import authenticator.model.User;
import selfCare.model.AssignedMission;
import selfCare.model.PointHistory;
import selfCare.model.SelfCareActivity;
import selfCare.model.SelfCareUser;
import selfCare.services.ISelfCareUserService;
import selfCare.services.MainAccountService;
import selfCare.services.SelfCareUserService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SCPsychologistViewController implements Initializable {

    // --- Inner Class: UserDisplay (untuk menampilkan data pengguna di TableView)
    // ---
    public static class UserDisplay {
        private final String username, fullName, level;
        private final int totalPoints;

        public UserDisplay(String u, String n, int p, String l) {
            this.username = u;
            this.fullName = n;
            this.totalPoints = p;
            this.level = l;
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

    // --- Deklarasi Service (pastikan diinisialisasi di initialize) ---
    private ISelfCareUserService selfCareService;
    private MainAccountService mainAccountService;

    // --- Deklarasi FXML Components (pastikan fx:id di FXML cocok persis) ---

    // Komponen untuk Tabel Pengguna
    @FXML
    private TableView<UserDisplay> userTable;
    @FXML
    private TableColumn<UserDisplay, String> colUserId;
    @FXML
    private TableColumn<UserDisplay, String> colUserName;
    @FXML
    private TableColumn<UserDisplay, String> colUserLevel;
    @FXML
    private TableColumn<UserDisplay, Integer> colUserPoints;

    // Komponen untuk Detail Pengguna Terpilih
    @FXML
    private Label detailUserIdLabel;
    @FXML
    private Label detailUserNameLabel;
    @FXML
    private Label detailUserPointsLabel;
    @FXML
    private Label detailUserLevelLabel;
    @FXML
    private TextField userNameField;
    @FXML
    private Button saveNameButton; // fx:id ini sekarang ada di FXML

    // Komponen untuk Riwayat Poin
    @FXML
    private TableView<PointHistory> historyTable;
    @FXML
    private TableColumn<PointHistory, String> colHistoryActivity;
    @FXML
    private TableColumn<PointHistory, Integer> colHistoryPoints;
    @FXML
    private TableColumn<PointHistory, String> colHistoryTimestamp;

    // Komponen untuk Misi yang Ditugaskan
    @FXML
    private TableView<AssignedMission> assignedMissionsTable;
    @FXML
    private TableColumn<AssignedMission, String> colAssignedMissionName;
    @FXML
    private TableColumn<AssignedMission, String> colAssignedMissionAssignedDate;
    @FXML
    private TableColumn<AssignedMission, String> colAssignedMissionStatus;
    @FXML
    private TableColumn<AssignedMission, String> colAssignedMissionCompletionDate;

    // Komponen untuk Penugasan Misi
    @FXML
    private ComboBox<UserDisplay> assignUserComboBox;
    @FXML
    private ComboBox<SelfCareActivity> assignActivityComboBox;
    @FXML
    private Button assignMissionButton;

    // Komponen Lain
    @FXML
    private VBox leaderboardContainer;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button updateButton; // Ini ada di FXML contoh Anda sebelumnya, meski tidak terhubung ke aksi

    // --- initialize() Method (dipanggil otomatis oleh FXMLLoader) ---
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DEBUG SCPsychologist: initialize() dipanggil.");

        // Inisialisasi service
        this.selfCareService = SelfCareUserService.getInstance();
        this.mainAccountService = MainAccountService.getInstance();

        // Setup TableColumn cell value factories
        setupTableColumns();

        // Setup Listener untuk pemilihan baris di userTable
        setupTableSelectionListener();

        // Populate ComboBox aktivitas (misi yang bisa ditugaskan)
        populateActivityList();

        // Mengatur status awal komponen UI
        // Memastikan komponen tidak null sebelum digunakan (meskipun seharusnya tidak
        // dengan fx:id yang benar)
        if (userNameField != null)
            userNameField.setDisable(true);
        if (saveNameButton != null)
            saveNameButton.setDisable(true);

        // Memuat data awal ke tabel pengguna
        refreshUI();
    }

    // --- Helper Methods ---

    // Mempopulasi daftar aktivitas yang bisa ditugaskan
    private void populateActivityList() {
        List<SelfCareActivity> activities = new ArrayList<>();
        activities.add(new SelfCareActivity("Meditasi 10 menit", 15));
        activities.add(new SelfCareActivity("Jalan pagi 30 menit", 20));
        activities.add(new SelfCareActivity("Membaca buku non-fiksi", 10));
        if (assignActivityComboBox != null) {
            assignActivityComboBox.setItems(FXCollections.observableArrayList(activities));
            System.out.println("DEBUG SCPsychologist: Activity list dipopulasi.");
        } else {
            System.err.println("ERROR: assignActivityComboBox is NULL during populateActivityList!");
        }
    }

    // Menghubungkan kolom tabel dengan properti model
    private void setupTableColumns() {
        // Pemeriksaan null tambahan untuk keamanan, meskipun @FXML seharusnya mengisi
        // ini
        if (colUserId != null)
            colUserId.setCellValueFactory(new PropertyValueFactory<>("username"));
        else
            System.err.println("ERROR: colUserId is NULL!");
        if (colUserName != null)
            colUserName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        else
            System.err.println("ERROR: colUserName is NULL!");
        if (colUserPoints != null)
            colUserPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        else
            System.err.println("ERROR: colUserPoints is NULL!");
        if (colUserLevel != null)
            colUserLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        else
            System.err.println("ERROR: colUserLevel is NULL!");
        System.out.println("DEBUG SCPsychologist: User table columns diatur.");

        if (colHistoryActivity != null)
            colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        else
            System.err.println("ERROR: colHistoryActivity is NULL!");
        if (colHistoryPoints != null)
            colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        else
            System.err.println("ERROR: colHistoryPoints is NULL!");
        if (colHistoryTimestamp != null)
            colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
        else
            System.err.println("ERROR: colHistoryTimestamp is NULL!");
        System.out.println("DEBUG SCPsychologist: History table columns diatur.");

        if (colAssignedMissionName != null)
            colAssignedMissionName.setCellValueFactory(new PropertyValueFactory<>("missionName"));
        else
            System.err.println("ERROR: colAssignedMissionName is NULL!");
        if (colAssignedMissionStatus != null)
            colAssignedMissionStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        else
            System.err.println("ERROR: colAssignedMissionStatus is NULL!");
        if (colAssignedMissionAssignedDate != null)
            colAssignedMissionAssignedDate.setCellValueFactory(new PropertyValueFactory<>("formattedAssignedDate"));
        else
            System.err.println("ERROR: colAssignedMissionAssignedDate is NULL!");
        if (colAssignedMissionCompletionDate != null)
            colAssignedMissionCompletionDate.setCellValueFactory(new PropertyValueFactory<>("formattedCompletionDate"));
        else
            System.err.println("ERROR: colAssignedMissionCompletionDate is NULL!");
        System.out.println("DEBUG SCPsychologist: Assigned Missions table columns diatur.");
    }

    // Mengatur listener untuk TableView agar memuat detail saat baris dipilih
    private void setupTableSelectionListener() {
        if (userTable != null) {
            userTable.getSelectionModel().selectedItemProperty().addListener((obs, old, item) -> {
                System.out.println("DEBUG SCPsychologist: userTable selection changed. New item: "
                        + (item != null ? item.getUsername() : "null"));
                showUserDetails(item);
            });
            System.out.println("DEBUG SCPsychologist: Table selection listener diatur.");
        } else {
            System.err.println("ERROR: userTable is NULL during setupTableSelectionListener!");
        }
    }

    // Memperbarui UI utama, termasuk tabel pengguna dan leaderboard
    @FXML
    public void refreshUI() {
        System.out.println("DEBUG SCPsychologist: refreshUI() dipanggil.");

        Map<String, User> allUsersMap = mainAccountService.getAllUsers().stream()
                .collect(Collectors.toMap(User::getUsername, Function.identity()));

        List<SelfCareUser> sharedSelfCareUsers = selfCareService.getSharedDataUsers();
        System.out.println("DEBUG SCPsychologist: Jumlah shared SelfCareUsers: " + sharedSelfCareUsers.size());

        List<UserDisplay> displayList = sharedSelfCareUsers.stream()
                .map(scUser -> {
                    User mainUser = allUsersMap.get(scUser.getUsername());
                    String fullName;
                    if (mainUser != null) {
                        fullName = mainUser.getFirstName() + " " + mainUser.getLastName();
                    } else {
                        System.err.println("Peringatan: User '" + scUser.getUsername()
                                + "' dari selfCareData.xml tidak ditemukan di users.xml.");
                        fullName = scUser.getName(); // Fallback ke nama dari SelfCareUser
                    }
                    return new UserDisplay(scUser.getUsername(), fullName, scUser.getTotalPoints(), scUser.getLevel());
                })
                .collect(Collectors.toList());

        if (userTable != null) {
            ObservableList<UserDisplay> oList = FXCollections.observableArrayList(displayList);
            userTable.setItems(oList);
            System.out.println("DEBUG SCPsychologist: userTable diperbarui dengan " + oList.size() + " item.");
        } else {
            System.err.println("ERROR: userTable is NULL during refreshUI, cannot set items!");
        }

        if (assignUserComboBox != null) {
            ObservableList<UserDisplay> oListForComboBox = FXCollections.observableArrayList(displayList);
            assignUserComboBox.setItems(oListForComboBox);
            Callback<ListView<UserDisplay>, ListCell<UserDisplay>> factory = lv -> new ListCell<>() {
                @Override
                protected void updateItem(UserDisplay item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item.getFullName());
                }
            };
            assignUserComboBox.setCellFactory(factory);
            assignUserComboBox.setButtonCell(factory.call(null));
            System.out.println("DEBUG SCPsychologist: assignUserComboBox diperbarui.");
        } else {
            System.err.println("ERROR: assignUserComboBox is NULL during refreshUI!");
        }

        updateLeaderboard(displayList);
        clearUserDetails(); // Pastikan detail dibersihkan saat refresh UI utama
    }

    // Menampilkan detail pengguna yang dipilih di panel kanan
    private void showUserDetails(UserDisplay userDisplay) {
        System.out.println("DEBUG SCPsychologist: showUserDetails() dipanggil.");
        if (userDisplay == null) {
            System.out.println("DEBUG SCPsychologist: userDisplay adalah null, membersihkan detail.");
            clearUserDetails();
            return;
        }

        User mainUser = mainAccountService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(userDisplay.getUsername()))
                .findFirst()
                .orElse(null);

        String userTypeToPass;
        String fullNameToPass;

        if (mainUser != null) {
            userTypeToPass = mainUser.getUserType();
            fullNameToPass = mainUser.getFirstName() + " " + mainUser.getLastName();
            System.out.println("DEBUG SCPsychologist: User '" + userDisplay.getUsername()
                    + "' ditemukan di users.xml. Tipe: " + userTypeToPass + ", Nama Lengkap: " + fullNameToPass);
        } else {
            System.err.println("Peringatan: User '" + userDisplay.getUsername()
                    + "' tidak ditemukan di users.xml. Menggunakan data dari UserDisplay sebagai fallback.");
            userTypeToPass = "Pengguna Umum"; // Default yang aman untuk tipe pengguna

            fullNameToPass = userDisplay.getFullName();
            if (fullNameToPass == null || fullNameToPass.trim().isEmpty()) {
                fullNameToPass = userDisplay.getUsername(); // Fallback ke username jika nama lengkap kosong/null
                System.err.println("Peringatan: FullName untuk user '" + userDisplay.getUsername()
                        + "' juga kosong. Menggunakan username sebagai nama.");
            }
        }

        System.out.println(
                "DEBUG SCPsychologist: Memanggil getOrCreateUser dengan username='" + userDisplay.getUsername() +
                        "', fullName='" + fullNameToPass + "', userType='" + userTypeToPass + "'");

        SelfCareUser scUser = selfCareService.getOrCreateUser(userDisplay.getUsername(), fullNameToPass,
                userTypeToPass);
        System.out.println("DEBUG SCPsychologist: selfCareUser berhasil didapatkan/dibuat. PointHistory size: "
                + scUser.getPointHistory().size() + ", AssignedMissions size: " + scUser.getAssignedMissions().size());

        // --- PENTING: Pemeriksaan Null untuk komponen FXML sebelum digunakan ---
        // Ini adalah bagian yang paling mungkin menyebabkan NPE sebelumnya jika fx:id
        // tidak terhubung
        if (detailUserIdLabel == null)
            System.err.println("ERROR: detailUserIdLabel is NULL!");
        if (detailUserNameLabel == null)
            System.err.println("ERROR: detailUserNameLabel is NULL!");
        if (detailUserPointsLabel == null)
            System.err.println("ERROR: detailUserPointsLabel is NULL!");
        if (detailUserLevelLabel == null)
            System.err.println("ERROR: detailUserLevelLabel is NULL!");
        if (historyTable == null)
            System.err.println("ERROR: historyTable is NULL!");
        if (assignedMissionsTable == null)
            System.err.println("ERROR: assignedMissionsTable is NULL!");
        // (Anda dapat menghapus pemeriksaan ini setelah yakin semua terhubung dengan
        // benar)

        // Mengisi label detail pengguna
        if (detailUserIdLabel != null)
            detailUserIdLabel.setText(userDisplay.getUsername());
        if (detailUserNameLabel != null)
            detailUserNameLabel.setText(fullNameToPass);
        if (detailUserPointsLabel != null)
            detailUserPointsLabel.setText(String.valueOf(userDisplay.getTotalPoints()));
        if (detailUserLevelLabel != null)
            detailUserLevelLabel.setText(userDisplay.getLevel());
        System.out.println("DEBUG SCPsychologist: Detail labels diperbarui.");

        // Mengisi tabel riwayat poin dan misi yang ditugaskan
        if (historyTable != null) historyTable.setItems(FXCollections.observableArrayList(scUser.getPointHistory())); else System.err.println("ERROR: historyTable is NULL!"); // Mungkin baris 241
    if (assignedMissionsTable != null) assignedMissionsTable.setItems(FXCollections.observableArrayList(scUser.getAssignedMissions())); else System.err.println("ERROR: assignedMissionsTable is NULL!"); // Mungkin baris 242
    System.out.println("DEBUG SCPsychologist: History dan Assigned Missions tables diperbarui.");
    }

    // --- Event Handlers ---

    @FXML
    private void handleAssignMission() {
        System.out.println("DEBUG SCPsychologist: handleAssignMission() dipanggil.");
        UserDisplay selectedUser = assignUserComboBox.getSelectionModel().getSelectedItem();
        SelfCareActivity selectedActivity = assignActivityComboBox.getSelectionModel().getSelectedItem();

        if (selectedUser == null || selectedActivity == null) {
            showAlert("Error", "Harap pilih pengguna dan aktivitas untuk misi.");
            return;
        }

        boolean success = selfCareService.assignMissionToUser(selectedUser.getUsername(), selectedActivity.getName());

        if (success) {
            showAlert("Sukses", "Misi '" + selectedActivity.getName() + "' berhasil ditugaskan kepada "
                    + selectedUser.getFullName());
            if (userTable.getSelectionModel().getSelectedItem() != null &&
                    userTable.getSelectionModel().getSelectedItem().getUsername().equals(selectedUser.getUsername())) {
                showUserDetails(selectedUser);
            }
        } else {
            showAlert("Gagal", "Terjadi kesalahan saat menugaskan misi. Data pengguna mungkin bermasalah.");
        }
        refreshUI();
    }

    @FXML
    private void handleDeleteUser() {
        System.out.println("DEBUG SCPsychologist: handleDeleteUser() dipanggil.");
        UserDisplay selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data pengguna.");
            return;
        }
        boolean deleted = selfCareService.deleteUserSelfCareData(selected.getUsername());
        if (deleted) {
            showAlert("Sukses", "Data self-care untuk " + selected.getFullName() + " telah dihapus.");
        } else {
            showAlert("Gagal",
                    "Gagal menghapus data self-care untuk " + selected.getFullName() + ". Pengguna mungkin tidak ada.");
        }
        refreshUI();
    }

    @FXML
    private void handleClearInput() {
        System.out.println("DEBUG SCPsychologist: handleClearInput() dipanggil.");
        userTable.getSelectionModel().clearSelection();
        clearUserDetails();
    }

    @FXML
    private void handleUpdateUser() { // Aksi untuk tombol update, jika ada
        System.out.println("DEBUG SCPsychologist: handleUpdateUser() (dinonaktifkan) dipanggil.");
        showAlert("Info", "Fitur edit nama pengguna tidak tersedia dari halaman ini.");
    }

    // Memperbarui leaderboard (panel kanan)
    private void updateLeaderboard(List<UserDisplay> users) {
        System.out.println("DEBUG SCPsychologist: updateLeaderboard() dipanggil.");
        // Membersihkan hanya Text node (item leaderboard)
        if (leaderboardContainer != null) {
            leaderboardContainer.getChildren().removeIf(node -> node instanceof Text);

            users.stream()
                    .sorted((u1, u2) -> Integer.compare(u2.getTotalPoints(), u1.getTotalPoints()))
                    .limit(5)
                    .forEach(u -> leaderboardContainer.getChildren()
                            .add(new Text(String.format("%s (%d poin)", u.getFullName(), u.getTotalPoints()))));
        } else {
            System.err.println("ERROR: leaderboardContainer is NULL during updateLeaderboard!");
        }
    }

    // Mengosongkan detail pengguna di panel kanan
    private void clearUserDetails() {
        System.out.println("DEBUG SCPsychologist: clearUserDetails() dipanggil.");
        if (detailUserIdLabel != null)
            detailUserIdLabel.setText("-");
        else
            System.err.println("ERROR: detailUserIdLabel is NULL in clearUserDetails!");
        if (detailUserNameLabel != null)
            detailUserNameLabel.setText("-");
        else
            System.err.println("ERROR: detailUserNameLabel is NULL in clearUserDetails!");
        if (detailUserPointsLabel != null)
            detailUserPointsLabel.setText("-");
        else
            System.err.println("ERROR: detailUserPointsLabel is NULL in clearUserDetails!");
        if (detailUserLevelLabel != null)
            detailUserLevelLabel.setText("-");
        else
            System.err.println("ERROR: detailUserLevelLabel is NULL in clearUserDetails!");

        if (historyTable != null)
            historyTable.setItems(FXCollections.emptyObservableList());
        else
            System.err.println("ERROR: historyTable is NULL in clearUserDetails!");
        if (assignedMissionsTable != null)
            assignedMissionsTable.setItems(FXCollections.emptyObservableList());
        else
            System.err.println("ERROR: assignedMissionsTable is NULL in clearUserDetails!");
    }

    // Menampilkan alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
// src/Selfcare/controller/PsychologistViewController.java
package selfCare.controller;

import selfCare.model.Activity;
import selfCare.model.AssignedMission; // Import AssignedMission
import selfCare.model.User;
import selfCare.model.PointHistoryEntry;
import selfCare.service.IUserService;
import selfCare.util.SortUtil; // Import SortUtil
import javafx.beans.property.ReadOnlyStringWrapper; // Import ReadOnlyStringWrapper
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Untuk menghasilkan ID unik

/**
 * Controller untuk tampilan Psikolog.
 * Memungkinkan psikolog untuk mengelola pengguna (CRUD), melihat detail pengguna,
 * melihat papan peringkat, dan MENUGASKAN MISI kepada pengguna.
 */
public class PsychologistViewController {

    private IUserService userService;
    private ObservableList<User> userObservableList;
    private ObservableList<PointHistoryEntry> historyObservableList;
    private ObservableList<Activity> availableActivities; // Untuk ComboBox aktivitas misi
    private ObservableList<AssignedMission> assignedMissionsObservableList; // Daftar misi yang ditugaskan untuk detail

    @FXML private TextField userNameField;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colUserId;
    @FXML private TableColumn<User, String> colUserName;
    @FXML private TableColumn<User, Integer> colUserPoints;
    @FXML private TableColumn<User, String> colUserLevel;

    @FXML private Label detailUserIdLabel;
    @FXML private Label detailUserNameLabel;
    @FXML private Label detailUserPointsLabel;
    @FXML private Label detailUserLevelLabel;
    @FXML private TableView<PointHistoryEntry> historyTable;
    @FXML private TableColumn<PointHistoryEntry, String> colHistoryActivity;
    @FXML private TableColumn<PointHistoryEntry, Integer> colHistoryPoints;
    @FXML private TableColumn<PointHistoryEntry, String> colHistoryTimestamp;

    // Elemen UI baru untuk Misi yang Ditugaskan di tampilan psikolog
    @FXML private TableView<AssignedMission> assignedMissionsTable;
    @FXML private TableColumn<AssignedMission, String> colAssignedMissionName;
    @FXML private TableColumn<AssignedMission, String> colAssignedMissionAssignedDate;
    @FXML private TableColumn<AssignedMission, String> colAssignedMissionStatus;
    @FXML private TableColumn<AssignedMission, String> colAssignedMissionCompletionDate;


    @FXML private VBox leaderboardContainer; // Container untuk papan peringkat

    // Elemen UI baru untuk Penugasan Misi (sudah ada sebelumnya)
    @FXML private ComboBox<User> assignUserComboBox;
    @FXML private ComboBox<Activity> assignActivityComboBox;
    @FXML private Button assignMissionButton;

    /**
     * Mengatur layanan pengguna untuk controller ini.
     * Ini adalah contoh Dependency Injection (D di SOLID).
     * @param userService Implementasi IUserService.
     */
    public void setUserService(IUserService userService) {
        this.userService = userService;
        initializeData();
        initializeActivitiesForAssignment(); // Inisialisasi aktivitas untuk penugasan
    }

    @FXML
    public void initialize() {
        // Inisialisasi kolom tabel pengguna
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUserPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        colUserLevel.setCellValueFactory(new PropertyValueFactory<>("currentLevelOrBadge"));

        // Inisialisasi kolom tabel riwayat poin
        colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("pointsGained"));
        colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));

        // Inisialisasi kolom tabel misi yang ditugaskan (BARU)
        colAssignedMissionName.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colAssignedMissionAssignedDate.setCellValueFactory(new PropertyValueFactory<>("formattedAssignedDate"));
        colAssignedMissionStatus.setCellValueFactory(cellData -> {
            boolean completed = cellData.getValue().isCompleted();
            return new ReadOnlyStringWrapper(completed ? "Selesai" : "Belum Selesai");
        });
        colAssignedMissionCompletionDate.setCellValueFactory(new PropertyValueFactory<>("formattedCompletionDate"));


        // Listener untuk pemilihan baris di tabel pengguna
        userTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showUserDetails(newValue));

        // Inisialisasi ObservableList (akan diisi di initializeData)
        userObservableList = FXCollections.observableArrayList();
        userTable.setItems(userObservableList);

        historyObservableList = FXCollections.observableArrayList();
        historyTable.setItems(historyObservableList);

        assignedMissionsObservableList = FXCollections.observableArrayList(); // Inisialisasi
        assignedMissionsTable.setItems(assignedMissionsObservableList); // Set items

        // Inisialisasi ComboBox untuk penugasan misi
        assignUserComboBox.setItems(userObservableList); // Menggunakan daftar pengguna yang sama
        assignUserComboBox.setPromptText("Pilih Pengguna");

        availableActivities = FXCollections.observableArrayList();
        assignActivityComboBox.setItems(availableActivities);
        assignActivityComboBox.setPromptText("Pilih Aktivitas");
    }

    /**
     * Memuat data pengguna dari layanan dan memperbarui tabel serta ComboBox penugasan.
     */
    private void initializeData() {
        if (userService != null) {
            userObservableList.clear();
            userObservableList.addAll(userService.getAllUsers());
            updateLeaderboard(); // Perbarui papan peringkat saat data dimuat
            // Perbarui ComboBox pengguna untuk penugasan misi juga
            assignUserComboBox.setItems(userObservableList);
        }
    }

    /**
     * Menginisialisasi ComboBox aktivitas untuk penugasan misi.
     * Asumsi aktivitas statis untuk demo. Dalam aplikasi nyata, ini akan dimuat dari database.
     */
    private void initializeActivitiesForAssignment() {
        availableActivities.clear();
        availableActivities.addAll(
            new Activity("act001", "Meditasi 10 Menit", "Luangkan waktu untuk menenangkan pikiran.", 10),
            new Activity("act002", "Jurnal Syukur", "Tulis 3 hal yang Anda syukuri.", 15),
            new Activity("act003", "Minum Air Putih Cukup", "Pastikan hidrasi yang optimal.", 5),
            new Activity("act004", "Olahraga Ringan", "Lakukan peregangan atau jalan kaki singkat.", 20),
            new Activity("act005", "Tidur Cukup (7-8 Jam)", "Prioritaskan istirahat berkualitas.", 25),
            new Activity("act006", "Membaca Buku 30 Menit", "Luangkan waktu untuk membaca.", 12),
            new Activity("act007", "Berinteraksi Positif", "Kirim pesan positif ke teman/keluarga.", 8)
        );
        if (!availableActivities.isEmpty()) {
            assignActivityComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Menampilkan detail pengguna yang dipilih di panel detail.
     * @param user Objek User yang dipilih.
     */
    private void showUserDetails(User user) {
        if (user != null) {
            detailUserIdLabel.setText(user.getId());
            detailUserNameLabel.setText(user.getName());
            detailUserPointsLabel.setText(String.valueOf(user.getTotalPoints()));
            detailUserLevelLabel.setText(user.getCurrentLevelOrBadge());

            historyObservableList.clear();
            List<PointHistoryEntry> userHistory = new ArrayList<>(user.getPointHistory());
            // Urutkan riwayat poin dari yang terbaru menggunakan Quicksort
            SortUtil.quickSort(userHistory, Comparator.comparingLong(PointHistoryEntry::getTimestamp).reversed());
            historyObservableList.addAll(userHistory);

            assignedMissionsObservableList.clear();
            List<AssignedMission> userMissions = new ArrayList<>(user.getAssignedMissions());
            // Urutkan misi yang belum selesai di atas, lalu berdasarkan tanggal penugasan terbaru menggunakan Quicksort
            SortUtil.quickSort(userMissions, Comparator
                .comparing(AssignedMission::isCompleted) // Belum selesai di atas (false < true)
                .thenComparingLong(AssignedMission::getAssignedDate).reversed()); // Tanggal terbaru
            assignedMissionsObservableList.addAll(userMissions);

        } else {
            clearUserDetails();
        }
    }

    /**
     * Mengosongkan panel detail pengguna.
     */
    private void clearUserDetails() {
        detailUserIdLabel.setText("");
        detailUserNameLabel.setText("");
        detailUserPointsLabel.setText("");
        detailUserLevelLabel.setText("");
        historyObservableList.clear();
        assignedMissionsObservableList.clear(); // Bersihkan juga daftar misi
    }

    /**
     * Menangani aksi tombol "Tambah Pengguna".
     */
    @FXML
    private void handleAddUser() {
        String name = userNameField.getText().trim();
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Nama pengguna tidak boleh kosong.");
            return;
        }

        String id = UUID.randomUUID().toString().substring(0, 8); // ID unik 8 karakter
        User newUser = new User(id, name);
        if (userService.addUser(newUser)) {
            userObservableList.add(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Pengguna " + name + " berhasil ditambahkan.");
            clearInputFields();
            updateLeaderboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menambahkan pengguna. Mungkin ID sudah ada.");
        }
    }

    /**
     * Menangani aksi tombol "Perbarui Pengguna".
     */
    @FXML
    private void handleUpdateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih pengguna yang akan diperbarui dari tabel.");
            return;
        }

        String newName = userNameField.getText().trim();
        if (newName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Nama pengguna tidak boleh kosong.");
            return;
        }

        selectedUser.setName(newName); // Hanya nama yang bisa diubah dari sini
        if (userService.updateUser(selectedUser)) {
            userTable.refresh(); // Refresh tabel untuk menampilkan perubahan
            showUserDetails(selectedUser); // Perbarui detail panel
            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Pengguna " + selectedUser.getName() + " berhasil diperbarui.");
            clearInputFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui pengguna.");
        }
    }

    /**
     * Menangani aksi tombol "Hapus Pengguna".
     */
    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih pengguna yang akan dihapus dari tabel.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Hapus Pengguna?");
        confirmAlert.setContentText("Anda yakin ingin menghapus pengguna " + selectedUser.getName() + "?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userService.deleteUser(selectedUser.getId())) {
                userObservableList.remove(selectedUser);
                clearUserDetails();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Pengguna " + selectedUser.getName() + " berhasil dihapus.");
                updateLeaderboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghapus pengguna.");
            }
        }
    }

    /**
     * Menangani aksi tombol "Bersihkan Input".
     */
    @FXML
    private void handleClearInput() {
        clearInputFields();
        userTable.getSelectionModel().clearSelection();
        clearUserDetails();
    }

    /**
     * Mengosongkan field input.
     */
    private void clearInputFields() {
        userNameField.setText("");
    }

    /**
     * Menampilkan dialog alert.
     * @param type Tipe Alert.
     * @param title Judul Alert.
     * @param message Pesan Alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Memperbarui tampilan papan peringkat.
     * Mengurutkan pengguna berdasarkan poin dan menampilkannya.
     */
    private void updateLeaderboard() {
        leaderboardContainer.getChildren().clear();

        List<User> sortedUsers = new ArrayList<>(userService.getAllUsers());
        // Urutkan pengguna berdasarkan poin menggunakan Quicksort (descending)
        SortUtil.quickSort(sortedUsers, Comparator.comparingInt(User::getTotalPoints).reversed());

        Label title = new Label("Papan Peringkat");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 5px;");
        leaderboardContainer.getChildren().add(title);

        if (sortedUsers.isEmpty()) {
            leaderboardContainer.getChildren().add(new Label("Tidak ada pengguna untuk ditampilkan."));
            return;
        }

        for (int i = 0; i < Math.min(sortedUsers.size(), 5); i++) {
            User user = sortedUsers.get(i);
            Label entry = new Label(
                (i + 1) + ". " + user.getName() + " (" + user.getTotalPoints() + " poin, " + user.getCurrentLevelOrBadge() + ")");
            leaderboardContainer.getChildren().add(entry);
        }
    }

    /**
     * Menangani aksi tombol "Tugaskan Misi".
     */
    @FXML
    private void handleAssignMission() {
        User selectedUser = assignUserComboBox.getSelectionModel().getSelectedItem();
        Activity selectedActivity = assignActivityComboBox.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih pengguna untuk menugaskan misi.");
            return;
        }
        if (selectedActivity == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih aktivitas untuk ditugaskan.");
            return;
        }

        boolean assigned = userService.assignMissionToUser(selectedUser.getId(), selectedActivity.getId(), selectedActivity.getName());

        if (assigned) {
            showAlert(Alert.AlertType.INFORMATION, "Misi Ditugaskan",
                      "Misi '" + selectedActivity.getName() + "' berhasil ditugaskan kepada " + selectedUser.getName() + ".");
            initializeData();
            showUserDetails(selectedUser);
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menugaskan misi. Mungkin misi sudah ditugaskan atau pengguna tidak ditemukan.");
        }
    }
}
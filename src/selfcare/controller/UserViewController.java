// src/Selfcare/controller/UserViewController.java
package selfCare.controller;

import selfCare.model.Activity;
import selfCare.model.AssignedMission;
import selfCare.model.PointHistoryEntry;
import selfCare.model.User;
import selfCare.service.IUserService;
import selfCare.util.SortUtil; // Import SortUtil
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList; // Import ArrayList
import java.util.Comparator;
import java.util.List; // Import List
import java.util.Optional;

/**
 * Controller untuk tampilan Pengguna Individu.
 * Memungkinkan pengguna untuk menandai aktivitas selesai, melihat profil,
 * riwayat poin mereka, dan menyelesaikan misi yang ditugaskan.
 */
public class UserViewController {

    private IUserService userService;
    private User currentUser;
    private ObservableList<PointHistoryEntry> historyObservableList;
    private ObservableList<Activity> availableActivities;
    private ObservableList<AssignedMission> assignedMissionsObservableList;

    @FXML private Label userNameLabel;
    @FXML private Label totalPointsLabel;
    @FXML private Label currentLevelOrBadgeLabel;
    @FXML private ComboBox<Activity> activityComboBox;
    @FXML private Button completeActivityButton;
    @FXML private TableView<PointHistoryEntry> historyTable;
    @FXML private TableColumn<PointHistoryEntry, String> colHistoryActivity;
    @FXML private TableColumn<PointHistoryEntry, Integer> colHistoryPoints;
    @FXML private TableColumn<PointHistoryEntry, String> colHistoryTimestamp;

    @FXML private VBox notificationContainer;

    @FXML private TableView<AssignedMission> assignedMissionsTable;
    @FXML private TableColumn<AssignedMission, String> colMissionName;
    @FXML private TableColumn<AssignedMission, String> colMissionAssignedDate;
    @FXML private TableColumn<AssignedMission, String> colMissionStatus;
    @FXML private TableColumn<AssignedMission, String> colMissionCompletionDate;


    /**
     * Mengatur layanan pengguna dan pengguna saat ini untuk controller ini.
     * Ini adalah contoh Dependency Injection (D di SOLID).
     * @param userService Implementasi IUserService.
     * @param userId ID pengguna yang akan dimuat.
     */
    public void initData(IUserService userService, String userId) {
        this.userService = userService;
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            this.currentUser = userOptional.get();
            updateUserProfileUI();
            initializeActivities();
            initializeHistoryTable();
            initializeAssignedMissionsTable(); // Inisialisasi tabel misi yang ditugaskan
        } else {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Pengguna dengan ID " + userId + " tidak ditemukan.");
        }
    }

    @FXML
    public void initialize() {
        // Inisialisasi awal komponen UI
        // Tabel riwayat dan misi akan diinisialisasi setelah currentUser diset di initData
    }

    /**
     * Menginisialisasi ComboBox aktivitas.
     * Asumsi aktivitas statis untuk demo. Dalam aplikasi nyata, ini akan dimuat dari database.
     */
    private void initializeActivities() {
        availableActivities = FXCollections.observableArrayList(
            new Activity("act001", "Meditasi 10 Menit", "Luangkan waktu untuk menenangkan pikiran.", 10),
            new Activity("act002", "Jurnal Syukur", "Tulis 3 hal yang Anda syukuri.", 15),
            new Activity("act003", "Minum Air Putih Cukup", "Pastikan hidrasi yang optimal.", 5),
            new Activity("act004", "Olahraga Ringan", "Lakukan peregangan atau jalan kaki singkat.", 20),
            new Activity("act005", "Tidur Cukup (7-8 Jam)", "Prioritaskan istirahat berkualitas.", 25)
        );
        activityComboBox.setItems(availableActivities);
        if (!availableActivities.isEmpty()) {
            activityComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Menginisialisasi tabel riwayat poin.
     */
    private void initializeHistoryTable() {
        colHistoryActivity.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colHistoryPoints.setCellValueFactory(new PropertyValueFactory<>("pointsGained"));
        colHistoryTimestamp.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));

        historyObservableList = FXCollections.observableArrayList();
        historyTable.setItems(historyObservableList);
        updateHistoryTable();
    }

    /**
     * Menginisialisasi tabel misi yang ditugaskan.
     */
    private void initializeAssignedMissionsTable() {
        colMissionName.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        colMissionAssignedDate.setCellValueFactory(new PropertyValueFactory<>("formattedAssignedDate"));
        colMissionStatus.setCellValueFactory(cellData -> {
            boolean completed = cellData.getValue().isCompleted();
            return new ReadOnlyStringWrapper(completed ? "Selesai" : "Belum Selesai");
        });
        colMissionCompletionDate.setCellValueFactory(new PropertyValueFactory<>("formattedCompletionDate"));

        assignedMissionsObservableList = FXCollections.observableArrayList();
        assignedMissionsTable.setItems(assignedMissionsObservableList);
        updateAssignedMissionsTable();
    }

    /**
     * Memperbarui tampilan UI profil pengguna.
     */
    private void updateUserProfileUI() {
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getName());
            totalPointsLabel.setText(String.valueOf(currentUser.getTotalPoints()));
            currentLevelOrBadgeLabel.setText(currentUser.getCurrentLevelOrBadge());
        }
    }

    /**
     * Memperbarui tabel riwayat poin pengguna.
     */
    private void updateHistoryTable() {
        if (currentUser != null) {
            historyObservableList.clear();
            List<PointHistoryEntry> userHistory = new ArrayList<>(currentUser.getPointHistory());
            // Urutkan riwayat poin dari yang terbaru menggunakan Quicksort
            SortUtil.quickSort(userHistory, Comparator.comparingLong(PointHistoryEntry::getTimestamp).reversed());
            historyObservableList.addAll(userHistory);
        }
    }

    /**
     * Memperbarui tabel misi yang ditugaskan.
     */
    private void updateAssignedMissionsTable() {
        if (currentUser != null) {
            assignedMissionsObservableList.clear();
            List<AssignedMission> userMissions = new ArrayList<>(currentUser.getAssignedMissions());
            // Urutkan misi yang belum selesai di atas, lalu berdasarkan tanggal penugasan terbaru menggunakan Quicksort
            SortUtil.quickSort(userMissions, Comparator
                .comparing(AssignedMission::isCompleted) // Belum selesai di atas (false < true)
                .thenComparingLong(AssignedMission::getAssignedDate).reversed()); // Tanggal terbaru
            assignedMissionsObservableList.addAll(userMissions);
        }
    }

    /**
     * Menangani aksi tombol "Selesaikan Aktivitas" (Umum).
     */
    @FXML
    private void handleCompleteActivity() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Pengguna belum dimuat.");
            return;
        }

        Activity selectedActivity = activityComboBox.getSelectionModel().getSelectedItem();
        if (selectedActivity == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih aktivitas yang telah diselesaikan.");
            return;
        }

        String oldLevel = currentUser.getCurrentLevelOrBadge();

        Optional<User> updatedUserOptional = userService.addPointsToUser(
            currentUser.getId(),
            selectedActivity.getDefaultPoints(),
            selectedActivity.getName()
        );

        if (updatedUserOptional.isPresent()) {
            this.currentUser = updatedUserOptional.get();
            updateUserProfileUI();
            updateHistoryTable();
            showAlert(Alert.AlertType.INFORMATION, "Aktivitas Selesai!",
                      "Anda mendapatkan " + selectedActivity.getDefaultPoints() + " poin untuk " + selectedActivity.getName() + "!");

            if (!oldLevel.equals(currentUser.getCurrentLevelOrBadge())) {
                showNotification("Selamat!", "Anda mencapai level/badge baru: " + currentUser.getCurrentLevelOrBadge() + "!");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyelesaikan aktivitas. Pengguna tidak ditemukan.");
        }
    }

    /**
     * Menangani aksi tombol "Selesaikan Misi Terpilih".
     */
    @FXML
    private void handleCompleteAssignedMission() {
        AssignedMission selectedMission = assignedMissionsTable.getSelectionModel().getSelectedItem();

        if (selectedMission == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih misi yang ingin Anda selesaikan dari tabel.");
            return;
        }
        if (selectedMission.isCompleted()) {
            showAlert(Alert.AlertType.INFORMATION, "Misi Sudah Selesai", "Misi ini sudah ditandai sebagai selesai.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Penyelesaian Misi");
        confirmAlert.setHeaderText("Selesaikan Misi?");
        confirmAlert.setContentText("Anda yakin ingin menandai misi '" + selectedMission.getActivityName() + "' sebagai selesai?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String oldLevel = currentUser.getCurrentLevelOrBadge();

            Optional<User> updatedUserOptional = userService.completeAssignedMission(currentUser.getId(), selectedMission.getId());

            if (updatedUserOptional.isPresent()) {
                this.currentUser = updatedUserOptional.get();

                Optional<Activity> originalActivity = availableActivities.stream()
                    .filter(a -> a.getId().equals(selectedMission.getActivityId()))
                    .findFirst();

                if (originalActivity.isPresent()) {
                    userService.addPointsToUser(
                        currentUser.getId(),
                        originalActivity.get().getDefaultPoints(),
                        selectedMission.getActivityName() + " (Misi Selesai)"
                    );
                    this.currentUser = userService.getUserById(currentUser.getId()).orElse(currentUser);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Peringatan", "Poin untuk misi ini tidak dapat ditentukan. Tambahkan secara manual jika perlu.");
                }

                updateUserProfileUI();
                updateHistoryTable();
                updateAssignedMissionsTable();
                showAlert(Alert.AlertType.INFORMATION, "Misi Selesai!",
                          "Misi '" + selectedMission.getActivityName() + "' berhasil diselesaikan!");

                if (!oldLevel.equals(currentUser.getCurrentLevelOrBadge())) {
                    showNotification("Selamat!", "Anda mencapai level/badge baru: " + currentUser.getCurrentLevelOrBadge() + "!");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyelesaikan misi. Pengguna atau misi tidak ditemukan.");
            }
        }
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
     * Menampilkan notifikasi pop-up di dalam aplikasi (bukan Alert standar).
     * Ini adalah contoh implementasi K08.3.
     * @param title Judul notifikasi.
     * @param message Pesan notifikasi.
     */
    private void showNotification(String title, String message) {
        Label notificationLabel = new Label(message);
        notificationLabel.setStyle("-fx-background-color: #e0ffe0; -fx-border-color: #a0c0a0; -fx-border-radius: 5; " +
                                   "-fx-padding: 10; -fx-font-weight: bold; -fx-text-fill: #333; -fx-alignment: center;");
        notificationLabel.setMaxWidth(Double.MAX_VALUE);
        notificationLabel.setWrapText(true);

        notificationContainer.getChildren().add(0, notificationLabel);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            javafx.application.Platform.runLater(() -> notificationContainer.getChildren().remove(notificationLabel));
        }).start();
    }
}

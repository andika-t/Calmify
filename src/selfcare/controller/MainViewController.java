package selfcare.controller;

import selfcare.model.Activity;
import selfcare.model.User;
import selfcare.service.SelfCareManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainViewController implements Initializable {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label totalPointsLabel;
    @FXML
    private Label currentLevelLabel;
    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private TextField newActivityNameField;
    @FXML
    private TextField newActivityPointsField;
    @FXML
    private Button addActivityButton;
    @FXML
    private Button editActivityButton;
    @FXML
    private Button deleteActivityButton;

    private SelfCareManager selfCareManager;
    private ObservableList<Activity> activities;

    public void setSelfCareManager(SelfCareManager selfCareManager) {
        this.selfCareManager = selfCareManager;
        updateUserInfo();
        loadActivities();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activities = FXCollections.observableArrayList();
        activityListView.setItems(activities);

        activityListView.setCellFactory(param -> new javafx.scene.control.ListCell<Activity>() {
            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (+" + item.getDefaultPoints() + " poin)");
                }
            }
        });

        activityListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isActivitySelected = newSelection != null;
            editActivityButton.setDisable(!isActivitySelected);
            deleteActivityButton.setDisable(!isActivitySelected);
            if (isActivitySelected) {
                newActivityNameField.setText(newSelection.getName());
                newActivityPointsField.setText(String.valueOf(newSelection.getDefaultPoints()));
            } else {
                newActivityNameField.clear();
                newActivityPointsField.clear();
            }
        });

        editActivityButton.setDisable(true);
        deleteActivityButton.setDisable(true);
    }

    public void updateUserInfo() {
        if (selfCareManager != null && selfCareManager.getCurrentUser() != null) {
            User currentUser = selfCareManager.getCurrentUser();
            userNameLabel.setText("Pengguna: " + currentUser.getName());
            totalPointsLabel.setText("Poin: " + currentUser.getTotalPoints());
            currentLevelLabel.setText("Level: " + currentUser.getCurrentLevel());
        }
    }

    private void loadActivities() {
        if (selfCareManager != null) {
            activities.setAll(selfCareManager.getAllActivities());
        }
    }

    @FXML
    private void handleAddActivity(ActionEvent event) {
        String name = newActivityNameField.getText().trim();
        String pointsText = newActivityPointsField.getText().trim();

        if (name.isEmpty() || pointsText.isEmpty()) {
            System.err.println("Peringatan: Nama aktivitas dan poin tidak boleh kosong.");
            return;
        }

        try {
            int points = Integer.parseInt(pointsText);
            if (points <= 0) {
                System.err.println("Peringatan: Poin harus angka positif.");
                return;
            }

            String newActivityId = UUID.randomUUID().toString();
            Activity newActivity = new Activity(newActivityId, name, points);
            selfCareManager.addActivity(newActivity);
            loadActivities();
            newActivityNameField.clear();
            newActivityPointsField.clear();
            System.out.println("Berhasil: Aktivitas '" + name + "' berhasil ditambahkan.");

        } catch (NumberFormatException e) {
            System.err.println("Peringatan: Poin harus berupa angka.");
        }
    }

    @FXML
    private void handleEditActivity(ActionEvent event) {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            String newName = newActivityNameField.getText().trim();
            String newPointsText = newActivityPointsField.getText().trim();

            if (newName.isEmpty() || newPointsText.isEmpty()) {
                System.err.println("Peringatan: Nama aktivitas dan poin tidak boleh kosong untuk mengedit.");
                return;
            }

            try {
                int newPoints = Integer.parseInt(newPointsText);
                if (newPoints <= 0) {
                    System.err.println("Peringatan: Poin harus angka positif.");
                    return;
                }

                Activity updatedActivity = new Activity(selectedActivity.getId(), newName, newPoints);
                selfCareManager.updateActivity(updatedActivity);
                loadActivities();
                newActivityNameField.clear();
                newActivityPointsField.clear();
                System.out.println("Berhasil: Aktivitas '" + newName + "' berhasil diperbarui.");

            } catch (NumberFormatException e) {
                System.err.println("Peringatan: Poin harus berupa angka.");
            }
        } else {
            System.err.println("Peringatan: Pilih aktivitas yang ingin diedit.");
        }
    }

    @FXML
    private void handleDeleteActivity(ActionEvent event) {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            selfCareManager.deleteActivity(selectedActivity.getId());
            loadActivities();
            System.out.println("Berhasil: Aktivitas '" + selectedActivity.getName() + "' berhasil dihapus.");
        } else {
            System.err.println("Peringatan: Pilih aktivitas yang ingin dihapus.");
        }
    }
}
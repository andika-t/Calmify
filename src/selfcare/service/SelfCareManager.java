package selfcare.service;

import selfcare.model.Activity;
import selfcare.model.User;
import java.util.List;
import java.util.UUID;


public class SelfCareManager {
    private IDataStore dataStore;

    private User currentUser;

    public SelfCareManager(IDataStore dataStore) {
        this.dataStore = dataStore;
        initializeData();
    }

    private void initializeData() {
        List<Activity> loadedActivities = dataStore.loadActivities(); 


        List<User> users = dataStore.loadUsers();

        if (users.isEmpty()) {
            currentUser = new User(UUID.randomUUID().toString(), "Pengguna Individu", 0, "Pemula");
            users.add(currentUser);
            dataStore.saveUsers(users); 
        } else {
            currentUser = users.get(0);
        }

        if (loadedActivities.isEmpty()) {
            List<Activity> defaultActivities = List.of(
                    new Activity(UUID.randomUUID().toString(), "Minum Air Putih", 10),
                    new Activity(UUID.randomUUID().toString(), "Meditasi 5 Menit", 15),
                    new Activity(UUID.randomUUID().toString(), "Jalan Kaki 15 Menit", 20)
            );
            dataStore.saveActivities(defaultActivities); 
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<Activity> getAllActivities() {
        return dataStore.loadActivities();
    }

    public void addActivity(Activity activity) {
        List<Activity> activities = dataStore.loadActivities();
        activities.add(activity);
        dataStore.saveActivities(activities);
    }

    public void updateActivity(Activity activity) {
        List<Activity> activities = dataStore.loadActivities();
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getId().equals(activity.getId())) {
                activities.set(i, activity);
                break;
            }
        }
        dataStore.saveActivities(activities);
    }

    public void deleteActivity(String activityId) {
        List<Activity> activities = dataStore.loadActivities();
        activities.removeIf(activity -> activity.getId().equals(activityId));
        dataStore.saveActivities(activities);
    }
}
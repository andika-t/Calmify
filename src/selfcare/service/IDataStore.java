package selfcare.service;

import selfcare.model.Activity;
import selfcare.model.User;

import java.util.List;
import java.util.Optional;

public interface IDataStore {
    List<User> loadUsers();

    void saveUsers(List<User> users);

    List<Activity> loadActivities();

    void saveActivities(List<Activity> activities);

    Optional<User> loadUserById(String userId);

    void updateUser(User user);

    void addActivity(Activity activity);
    void updateActivity(Activity activity);
    void deleteActivity(String activityId);
}
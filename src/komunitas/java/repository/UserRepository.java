package komunitas.java.repository;

import komunitas.java.model.User;
import komunitas.java.model.UserSettings;

public interface UserRepository {
    User findByUsername(String username);
    boolean save(User user);
    void update(User user);
    UserSettings getUserSettings(String userId);
    void saveUserSettings(UserSettings settings);
}
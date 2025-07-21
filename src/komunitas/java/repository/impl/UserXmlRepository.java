package komunitas.java.repository.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import komunitas.java.model.User;
import komunitas.java.model.UserSettings;
import komunitas.java.repository.UserRepository;
import komunitas.java.util.XmlParser;

public class UserXmlRepository implements UserRepository {
    private static final String USERS_FILE = "src/main/resources/data/users.xml";
    private static final String SETTINGS_FILE = "src/main/resources/data/user_settings.xml";
    private static final String USERS_ROOT_ELEMENT = "users";
    private static final String USER_ELEMENT = "user";
    private static final String SETTINGS_ROOT_ELEMENT = "userSettings";
    private static final String SETTING_ELEMENT = "setting";
    
    private XmlParser<User> userXmlParser;
    private XmlParser<UserSettings> settingsXmlParser;

    public UserXmlRepository() {
        this.userXmlParser = new XmlParser<>(User.class);
        this.settingsXmlParser = new XmlParser<>(UserSettings.class);
    }

    @Override
public User findByUsername(String username) {
    try {
        List<User> users = userXmlParser.unmarshal(USERS_FILE, "user"); // Perhatikan element name
        if (users == null || users.isEmpty()) {
            System.out.println("No users found in XML");
            return null;
        }
        
        System.out.println("All users in XML:");
        users.forEach(u -> System.out.println(u.getUsername() + " - " + u.getPassword()));
        
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    } catch (Exception e) {
        System.err.println("Error reading users XML:");
        e.printStackTrace();
        return null;
    }
}

    @Override
    public boolean save(User user) {  // Ubah return type menjadi boolean
        try {
            List<User> users = userXmlParser.unmarshal(USERS_FILE, USER_ELEMENT);
            if (users == null) users = new ArrayList<>();
            
            users.add(user);
            userXmlParser.marshal(users, USERS_FILE, USERS_ROOT_ELEMENT, USER_ELEMENT);
            return true; // Return true jika berhasil
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false jika gagal
        }
    }

    @Override
    public void update(User user) {
        try {
            List<User> users = userXmlParser.unmarshal(USERS_FILE, USER_ELEMENT);
            if (users == null) return;
            
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
            userXmlParser.marshal(users, USERS_FILE, USERS_ROOT_ELEMENT, USER_ELEMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserSettings getUserSettings(String userId) {
        List<UserSettings> settingsList = settingsXmlParser.unmarshal(SETTINGS_FILE, SETTING_ELEMENT);
        if (settingsList == null) return null;
        
        Optional<UserSettings> settings = settingsList.stream()
                .filter(s -> s.getUserId().equals(userId))
                .findFirst();
        return settings.orElse(null);
    }

    @Override
    public void saveUserSettings(UserSettings settings) {
        try {
            List<UserSettings> settingsList = settingsXmlParser.unmarshal(SETTINGS_FILE, SETTING_ELEMENT);
            if (settingsList == null) settingsList = new ArrayList<>();
            
            settingsList.removeIf(s -> s.getUserId().equals(settings.getUserId()));
            settingsList.add(settings);
            settingsXmlParser.marshal(settingsList, SETTINGS_FILE, SETTINGS_ROOT_ELEMENT, SETTING_ELEMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
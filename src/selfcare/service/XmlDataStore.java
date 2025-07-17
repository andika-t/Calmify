package selfcare.service;

import selfcare.model.Activity;
import selfcare.model.User;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XmlDataStore implements IDataStore {
    private static final String DATA_FILE = "data.xml";
    private XStream xstream;

    public XmlDataStore() {
        xstream = new XStream(new StaxDriver());
        xstream.addPermission(AnyTypePermission.ANY);

        xstream.alias("appData", AppData.class);
        xstream.alias("user", User.class);
        xstream.alias("activity", Activity.class);

        xstream.useAttributeFor(User.class, "id");
        xstream.useAttributeFor(Activity.class, "id");

        xstream.addImplicitCollection(AppData.class, "users");
        xstream.addImplicitCollection(AppData.class, "activities");

        xstream.processAnnotations(AppData.class);
        xstream.processAnnotations(User.class);
        xstream.processAnnotations(Activity.class);
    }

    private static class AppData {
        private List<User> users = new ArrayList<>();
        private List<Activity> activities = new ArrayList<>();

        public AppData() { }

        public List<User> getUsers() {
            if (users == null) {
                users = new ArrayList<>();
            }
            return users;
        }

        public List<Activity> getActivities() {
            if (activities == null) {
                activities = new ArrayList<>();
            }
            return activities;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public void setActivities(List<Activity> activities) {
            this.activities = activities;
        }

        public void validateAndCorrectLists() {
            if (this.users != null) {
                List<User> validUsers = new ArrayList<>();
                for (Object item : this.users) {
                    if (item instanceof User) {
                        validUsers.add((User) item);
                    } else {
                    }
                }
                this.users = validUsers;
            } else {
                this.users = new ArrayList<>();
            }

            if (this.activities != null) {
                List<Activity> validActivities = new ArrayList<>();
                for (Object item : this.activities) {
                    if (item instanceof Activity) {
                        validActivities.add((Activity) item);
                    } else {
                    }
                }
                this.activities = validActivities;
            } else {
                this.activities = new ArrayList<>();
            }
        }
    }

    private AppData loadAppData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            AppData newAppData = new AppData();
            saveAppData(newAppData);
            return newAppData;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            Object loadedObject = xstream.fromXML(fis);
            if (loadedObject instanceof AppData) {
                AppData loadedAppData = (AppData) loadedObject;
                loadedAppData.validateAndCorrectLists(); 
                return loadedAppData;
            } else {
                System.err.println("Warning: XML data is not of expected AppData type or is malformed. Deleting old file and creating new AppData.");
                file.delete();
                return new AppData();
            }
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            System.err.println("Error loading app data from XML: " + e.getMessage() + ". Deleting old file and creating new AppData.");
            file.delete();
            return new AppData();
        }
    }

    private void saveAppData(AppData appData) {
        try (FileOutputStream fos = new FileOutputStream(DATA_FILE)) {
            xstream.toXML(appData, fos);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving app data to XML: " + e.getMessage());
        }
    }

    @Override
    public List<User> loadUsers() {
        return loadAppData().getUsers();
    }

    @Override
    public void saveUsers(List<User> users) {
        AppData appData = loadAppData();
        appData.setUsers(users);
        saveAppData(appData);
    }

    @Override
    public List<Activity> loadActivities() {
        return loadAppData().getActivities();
    }

    @Override
    public void saveActivities(List<Activity> activities) {
        AppData appData = loadAppData();
        appData.setActivities(activities);
        saveAppData(appData);
    }

    @Override
    public Optional<User> loadUserById(String userId) {
        return loadUsers().stream().filter(u -> u.getId().equals(userId)).findFirst();
    }

    @Override
    public void updateUser(User user) {
        List<User> users = loadUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                found = true;
                break;
            }
        }
        if (!found) {
            users.add(user);
        }
        saveUsers(users);
    }

    @Override
    public void addActivity(Activity activity) {
        List<Activity> activities = loadActivities();
        activities.add(activity);
        saveActivities(activities);
    }

    @Override
    public void updateActivity(Activity activity) {
        List<Activity> activities = loadActivities();
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getId().equals(activity.getId())) {
                activities.set(i, activity);
                saveActivities(activities);
                return;
            }
        }
        System.err.println("Activity with ID " + activity.getId() + " not found for update.");
    }

    @Override
    public void deleteActivity(String activityId) {
        List<Activity> activities = loadActivities();
        if (activities.removeIf(activity -> activity.getId().equals(activityId))) {
            saveActivities(activities);
        } else {
            System.err.println("Activity with ID " + activityId + " not found for deletion.");
        }
    }
}
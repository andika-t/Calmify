package authenticator.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import authenticator.model.User;
import authenticator.model.UserDataXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManager {

    private static final String DATABASE_FILE = "data/users.xml";
    private static final XStream xstream = new XStream(new StaxDriver());

    static {
        xstream.alias("user-data", UserDataXML.class);
        xstream.alias("user", User.class);
        xstream.allowTypes(new Class[] { UserDataXML.class, User.class });
    }

    private static UserDataXML loadData() {
        File file = new File(DATABASE_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                return (UserDataXML) xstream.fromXML(fis);
            } catch (Exception e) {
                System.err.println("Error memuat database: " + e.getMessage());
                return new UserDataXML();
            }
        }
        return new UserDataXML();
    }

    private static boolean saveDatabase(UserDataXML data) {
        try (FileOutputStream fos = new FileOutputStream(DATABASE_FILE)) {
            xstream.toXML(data, fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<User> findUserByUsername(String username) {
        UserDataXML data = loadData();
        return data.getUsers().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public static boolean addUser(User newUser) {
        if (newUser == null || newUser.getUsername() == null || newUser.getUsername().isEmpty()) {
            return false;
        }
        UserDataXML data = loadData();
        boolean usernameExists = data.getUsers().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(newUser.getUsername()));
        if (usernameExists) {
            return false;
        }
        data.addUser(newUser);
        return saveDatabase(data);
    }

    public static boolean updateUser(User updatedUser) {
        if (updatedUser == null || updatedUser.getUsername() == null || updatedUser.getUsername().isEmpty()) {
            return false;
        }
        UserDataXML data = loadData();
        List<User> users = new ArrayList<>(data.getUsers());

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (found) {
            data.setUsers(users);
            return saveDatabase(data);
        }
        return false;
    }

    public static boolean deleteUser(String username) {
        UserDataXML data = loadData();
        boolean removed = data.getUsers().removeIf(user -> user.getUsername().equalsIgnoreCase(username));
        if (removed) {
            saveDatabase(data);
        }
        return removed;
    }

    public static void updateShareData(String username, boolean consentStatus) {
        UserDataXML data = loadData();
        
        Optional<User> userToUpdateOpt = data.getUsers().stream()
            .filter(user -> user.getUsername().equalsIgnoreCase(username))
            .findFirst();

        if (userToUpdateOpt.isPresent()) {
            userToUpdateOpt.get().setShareData(consentStatus);
            saveDatabase(data);
            System.out.println("Status persetujuan untuk '" + username + "' berhasil diubah ke: " + consentStatus);
        } else {
            System.err.println("Gagal mengubah persetujuan: Pengguna '" + username + "' tidak ditemukan.");
        }
    }

    public static boolean getShareData(String username) {
        Optional<User> userOpt = findUserByUsername(username);
        return userOpt.map(user -> user.isShareData()).orElse(false);
    }
}

package authenticator.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import authenticator.model.User;
import authenticator.model.UserDataXML;
import pantauStres.services.AnswerModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
                System.err.println("Error memuat database pengguna: " + e.getMessage());
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

        if (newUser.getUsername().contains(" ")) {
            System.err.println("VALIDATION FAILED: Username tidak boleh mengandung spasi.");
            return false;
        }

        if (newUser.getPassword() == null || newUser.getPassword().length() < 6) {
            System.err.println("VALIDATION FAILED: Password minimal harus 6 karakter.");
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
        if (updatedUser == null || updatedUser.getUsername() == null) {
            System.err.println("DEBUG: updateUser failed - updatedUser or username is null.");
            return false;
        }

        UserDataXML data = loadData();
        if (data == null || data.getUsers() == null) {
            System.err.println("DEBUG: updateUser failed - Failed to load UserDataXML or users list is null.");
            return false;
        }
        List<User> users = data.getUsers();

        boolean found = false;
        System.out.println("DEBUG: Users loaded from data:");
        for (User u : users) {
            System.out.println("  - " + u.getUsername());
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                System.out.println(
                        "DEBUG: User '" + updatedUser.getUsername() + "' found at index " + i + ". Updating...");
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("DEBUG: User found and updated in memory. Attempting to save database...");
            boolean saveResult = saveDatabase(data);
            System.out.println("DEBUG: saveDatabase returned: " + saveResult);
            return saveResult;
        } else {
            System.err.println(
                    "DEBUG: updateUser failed - User '" + updatedUser.getUsername() + "' not found in loaded data.");
            return false;
        }
    }

    public static boolean updateUsername(String oldUsername, String newUsername, String password) {
        if (oldUsername == null || newUsername == null || password == null ||
            oldUsername.trim().isEmpty() || newUsername.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("DEBUG: updateUsername failed - input parameters cannot be null or empty.");
            return false;
        }

        UserDataXML data = loadData();
        if (data == null || data.getUsers() == null) {
            System.err.println("DEBUG: updateUsername failed - Failed to load UserDataXML or users list is null.");
            return false;
        }

        List<User> users = data.getUsers();
        User userToUpdate = null;
        int userIndex = -1;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(oldUsername)) {
                userToUpdate = users.get(i);
                userIndex = i;
                break;
            }
        }

        if (userToUpdate == null) {
            System.err.println("updateUsername failed - User with old username '" + oldUsername + "' not found.");
            return false;
        }
        if (!userToUpdate.getPassword().equals(password)) {
            System.err.println("updateUsername failed - Incorrect password for user '" + oldUsername + "'.");
            return false;
        }

        if (!newUsername.equalsIgnoreCase(oldUsername)) {
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(newUsername)) {
                    System.err.println("DEBUG: updateUsername failed - New username '" + newUsername + "' already exists.");
                    return false;
                }
            }
        }

        userToUpdate.setUsername(newUsername);
        users.set(userIndex, userToUpdate);
        System.out.println("DEBUG: updateUsername - User '" + oldUsername + "' username changed to '" + newUsername + "'. Attempting to save...");
        return saveDatabase(data);
    }


    public static boolean deleteUser(String username) {
        UserDataXML data = loadData();
        Optional<User> userToDeleteOpt = data.getUsers().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();

        if (!userToDeleteOpt.isPresent()) {
            return false;
        }

        User userToDelete = userToDeleteOpt.get();

        if ("Psikolog".equalsIgnoreCase(userToDelete.getUserType())) {
            boolean removed = data.getUsers().removeIf(user -> user.getUsername().equalsIgnoreCase(username));
            if (removed) {
                System.out.println("Akun psikolog '" + username + "' telah dinonaktifkan, data tetap tersimpan.");
                return saveDatabase(data);
            }
            return false;
        } else {
            AnswerModel answerModel = new AnswerModel();
            answerModel.deleteAnswersByUsername(username);

            boolean removed = data.getUsers().removeIf(user -> user.getUsername().equalsIgnoreCase(username));
            if (removed) {
                System.out.println("Akun pengguna umum '" + username + "' dan semua datanya telah dihapus.");
                return saveDatabase(data);
            }
            return false;
        }
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
        return userOpt.map(User::isShareData).orElse(false);
    }

    public static List<User> getAllUsers() {
        return loadData().getUsers();
    }
}
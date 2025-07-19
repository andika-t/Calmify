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
        UserDataXML data = loadData();
        boolean usernameExists = data.getUsers().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(newUser.getUsername()));
        if (usernameExists) {
            return false;
        }
        data.addUser(newUser);
        return saveDatabase(data);
    }

    /**
     * Memperbarui informasi pengguna yang ada di database.
     * @param updatedUser Objek pengguna dengan data yang sudah diperbarui.
     * @return true jika berhasil, false jika pengguna tidak ditemukan atau gagal menyimpan.
     */
    public static boolean updateUser(User updatedUser) {
        if (updatedUser == null || updatedUser.getUsername() == null || updatedUser.getUsername().isEmpty()) {
            return false;
        }
        
        // PERBAIKAN KRUSIAL: Memuat data yang ada, bukan membuat baru.
        UserDataXML data = loadData();
        List<User> users = data.getUsers();

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                // Ganti objek user lama dengan yang baru
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (found) {
            // Simpan seluruh daftar pengguna yang sudah diperbarui
            return saveDatabase(data);
        }
        
        // Kembalikan false jika pengguna dengan username tersebut tidak ditemukan
        return false;
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
            userToDelete.setPassword(null);
            System.out.println("Akun psikolog '" + username + "' telah dinonaktifkan, data tetap tersimpan.");
            return saveDatabase(data);
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
}
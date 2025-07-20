package selfCare.services;

import authenticator.model.User;
import selfCare.model.UserContainer; // <-- PASTIKAN IMPORT INI ADA
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainAccountService {
    private static MainAccountService instance;
    private List<User> allUsers;

    private static final String USER_DATA_FILE_PATH = "data/users.xml";

    private MainAccountService() {
        this.allUsers = loadUsersFromXml();
    }

    public static synchronized MainAccountService getInstance() {
        if (instance == null) {
            instance = new MainAccountService();
        }
        return instance;
    }

    private List<User> loadUsersFromXml() {
        XStream xstream = new XStream(new StaxDriver());
        xstream.addPermission(AnyTypePermission.ANY);

        // ================= PERBAIKAN DI SINI =================
        // Beri tahu XStream untuk mengenali anotasi di kelas model kita.
        // Ini akan secara otomatis membuat alias "user-data" ke UserContainer
        // dan "user" ke User.
        xstream.processAnnotations(UserContainer.class);
        xstream.processAnnotations(User.class);
        // ======================================================

        xstream.ignoreUnknownElements();

        File file = new File(USER_DATA_FILE_PATH);
        if (!file.exists()) {
            System.err.println("File tidak ditemukan: " + USER_DATA_FILE_PATH);
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            // 1. Baca XML sebagai UserContainer
            UserContainer container = (UserContainer) xstream.fromXML(reader);

            // 2. Ambil list of users dari dalam container
            if (container != null && container.getUsers() != null) {
                return container.getUsers();
            }
            return new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<User> getAllUsers() {
        return allUsers != null ? allUsers : new ArrayList<>();
    }
}
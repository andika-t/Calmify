package selfCare.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import selfCare.model.GeneralUser;
import selfCare.model.UserContainer;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainAccountService {
    private static MainAccountService instance;
    private List<GeneralUser> allUsers;

    private static final String USER_DATA_FILE_PATH = "data/user_data.xml";

    private MainAccountService() {
        this.allUsers = loadUsersFromXml();
    }

    public static MainAccountService getInstance() {
        if (instance == null) {
            instance = new MainAccountService();
        }
        return instance;
    }

    private List<GeneralUser> loadUsersFromXml() {
        XStream xstream = new XStream(new StaxDriver());
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.processAnnotations(GeneralUser.class);
        xstream.processAnnotations(UserContainer.class);

        File file = new File(USER_DATA_FILE_PATH);
        if (!file.exists()) {
            System.err.println("File tidak ditemukan: " + USER_DATA_FILE_PATH);
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            UserContainer container = (UserContainer) xstream.fromXML(reader);
            return container.getUsers() != null ? container.getUsers() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<GeneralUser> getAllGeneralUsers() {
        return allUsers.stream()
                .filter(user -> "Pengguna Umum".equals(user.getUserType()))
                .collect(Collectors.toList());
    }
}
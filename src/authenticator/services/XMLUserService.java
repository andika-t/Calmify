package authenticator.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import authenticator.model.User;

public class XMLUserService implements UserService {
    private static final String XML_FILE = "src/authenticator/data/users.xml";
    private List<User> users;

    public XMLUserService() {
        loadDataUser();
    }

    private void loadDataUser() {
        File xmlFile = new File(XML_FILE);
        File parent = xmlFile.getParentFile();

        if (!xmlFile.exists() || xmlFile.length() == 0) {
            users = new ArrayList<>();
            saveDataUser();
            return;
        }

        try (FileReader reader = new FileReader(XML_FILE)) {
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("User", User.class);
            xstream.allowTypesByWildcard(new String[] {"authenticator.model.*"});

            Object obj = xstream.fromXML(reader);
            if (obj instanceof List) {
                users = (List<User>) obj;
                System.out.println("Berhasil load " + users.size() + " user data");
            } else {
                users = new ArrayList<>();
            }
        } catch (Exception e) {
            System.out.println("Gagal load data: " + e.getMessage());
            users = new ArrayList<>();
        }
    }

    private void saveDataUser() {
        try (FileWriter writer = new FileWriter(XML_FILE)) {
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("User", User.class);
            xstream.toXML(users, writer);
            System.out.println("Berhasil simpan data");
        } catch (Exception e) {
            System.out.println("Gagal simpan data");
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(User user) {
        users.add(user);
        saveDataUser();
    }

    @Override
    public void updateUser(User user) {
        deleteUser(user.getUsername());
        users.add(user);
        saveDataUser();
    }

    @Override
    public void deleteUser(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
    }

    @Override
    public User findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users);
    }
}

package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

public class UserXML {

    private static final String filePath = "src/data/userData.xml";
    private XStream xstream;

    public UserXML() {
        xstream = new XStream(new StaxDriver());
        xstream.addPermission(NoTypePermission.NONE);
        xstream.allowTypes(new Class[] { User.class, List.class });
        xstream.alias("user", User.class);
    }

    public boolean saveUser(User user) {
        List<User> userList = loadUsers();
        userList.add(user);

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        System.out.println("DEBUG: File path = " + file.getAbsolutePath());
        System.out.println("DEBUG: Total user = " + userList.size());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            xstream.toXML(userList, fos);
            System.out.println("Menyimpan di: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.out.println("Gagal menyimpan user: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> loadUsers() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            return (List<User>) xstream.fromXML(fis);
        } catch (IOException e) {
            System.out.println("Gagal membaca user: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isUsernameTaken(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String username, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}

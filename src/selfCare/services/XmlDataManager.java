package selfCare.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import selfCare.model.SelfCareUser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlDataManager implements IDataManager {
    private final String filePath;
    private final XStream xstream;

    public XmlDataManager(String filePath) {
        this.filePath = filePath;
        this.xstream = new XStream(new StaxDriver());
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("users", List.class);
        xstream.processAnnotations(SelfCareUser.class);
    }

    @Override
    public List<SelfCareUser> loadUsers() {
        File file = new File(filePath);
        if (!file.exists()) {
            new File("data").mkdirs();
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Object result = xstream.fromXML(reader);
            if (result instanceof List) {
                return (List<SelfCareUser>) result;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void saveUsers(List<SelfCareUser> users) {
        try (FileWriter writer = new FileWriter(filePath)) {
            xstream.toXML(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
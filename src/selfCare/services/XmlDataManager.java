package selfCare.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import selfCare.model.SelfCareData;
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
        xstream.processAnnotations(SelfCareData.class);
        xstream.processAnnotations(SelfCareUser.class);
    }

    @Override
    public List<SelfCareUser> loadUsers() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("DEBUG: File " + filePath + " tidak ditemukan. Mengembalikan list kosong.");
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            SelfCareData data = (SelfCareData) xstream.fromXML(reader);
            System.out.println("DEBUG: Memuat data dari " + filePath + ". Jumlah user: "
                    + (data != null ? data.getSelfCareUsers().size() : 0));
            if (data != null && !data.getSelfCareUsers().isEmpty()) {
                SelfCareUser akunUmum = data.getSelfCareUsers().stream()
                        .filter(u -> "AkunUmum".equals(u.getUsername()))
                        .findFirst()
                        .orElse(null);
                if (akunUmum != null) {
                    System.out.println("DEBUG: AkunUmum PointHistory size: " + akunUmum.getPointHistory().size());
                    System.out
                            .println("DEBUG: AkunUmum AssignedMissions size: " + akunUmum.getAssignedMissions().size());
                }
            }
            return data != null ? data.getSelfCareUsers() : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("ERROR: Terjadi kesalahan saat memuat data dari " + filePath);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void saveUsers(List<SelfCareUser> users) {
        SelfCareData data = new SelfCareData();
        data.setSelfCareUsers(users);
        try (FileWriter writer = new FileWriter(filePath)) {
            xstream.toXML(data, writer);
            System.out.println("DEBUG: Data berhasil disimpan ke " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR: Terjadi kesalahan saat menyimpan data ke " + filePath);
            e.printStackTrace();
        }
    }
}
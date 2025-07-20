// src/Selfcare/service/XmlDataManager.java
package selfCare.service;

import selfCare.model.User;
import selfCare.model.PointHistoryEntry;
import selfCare.model.AssignedMission;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver; // Menggunakan StaxDriver
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlDataManager implements IDataManager {
    private static final Logger LOGGER = Logger.getLogger(XmlDataManager.class.getName());
    private final String filePath;
    private final XStream xstream;

    public XmlDataManager(String filePath) {
        this.filePath = filePath;
        this.xstream = new XStream(new StaxDriver());
        configureXStream();
    }

    private void configureXStream() {
        xstream.addPermission(AnyTypePermission.ANY);

        xstream.alias("users", List.class);
        xstream.alias("user", User.class);
        xstream.alias("pointHistoryEntry", PointHistoryEntry.class);
        xstream.alias("assignedMission", AssignedMission.class);
    }

    @Override
    public List<User> loadUsers() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            LOGGER.info("File data tidak ditemukan atau kosong, mengembalikan daftar pengguna kosong.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Object obj = xstream.fromXML(reader);
            if (obj instanceof List) {
                List<?> rawList = (List<?>) obj;
                List<User> users = new ArrayList<>();
                for (Object item : rawList) {
                    if (item instanceof User) {
                        users.add((User) item);
                    } else {
                        LOGGER.warning("Objek non-User ditemukan dalam data XML: " + item.getClass().getName());
                    }
                }
                LOGGER.info("Data pengguna berhasil dimuat dari: " + filePath);
                return users;
            } else {
                LOGGER.severe("Root objek dalam XML bukan List: " + obj.getClass().getName());
                return new ArrayList<>();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Gagal memuat data pengguna dari file: " + filePath, e);
            return new ArrayList<>();
        } catch (com.thoughtworks.xstream.XStreamException e) {
            LOGGER.log(Level.SEVERE, "Kesalahan deserialisasi XStream dari file: " + filePath, e);
            return new ArrayList<>();
        }
    }

    @Override
    public void saveUsers(List<User> users) {
        try (FileWriter writer = new FileWriter(filePath)) {
            xstream.toXML(users, writer);
            LOGGER.info("Data pengguna berhasil disimpan ke: " + filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Gagal menyimpan data pengguna ke file: " + filePath, e);
        }
    }
}

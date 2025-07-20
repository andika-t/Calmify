package selfCare.services;

import selfCare.model.SelfCareUser;
import java.util.List;

/**
 * Interface ini adalah "kontrak" untuk semua kelas yang bertugas mengelola
 * data. Setiap kelas yang mengimplementasikan IDataManager WAJIB memiliki
 * metode loadUsers() dan saveUsers().
 */
public interface IDataManager {
    List<SelfCareUser> loadUsers();
    void saveUsers(List<SelfCareUser> users);
}
package selfCare.services;
import selfCare.model.SelfCareUser;
import java.util.List;

public interface IDataManager {
    List<SelfCareUser> loadUsers();
    void saveUsers(List<SelfCareUser> users);
}
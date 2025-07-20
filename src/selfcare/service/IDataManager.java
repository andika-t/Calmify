package selfCare.service;

import selfCare.model.User;
import java.util.List;

public interface IDataManager {

    List<User> loadUsers();

    void saveUsers(List<User> users);
}

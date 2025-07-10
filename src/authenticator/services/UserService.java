package authenticator.services;

import java.util.List;

import authenticator.model.User;

public interface UserService {

    void createUser(User user);
    void updateUser(User user);
    void deleteUser(String username);
    User findByUsername(String username);   
    List<User> findAllUsers();
}

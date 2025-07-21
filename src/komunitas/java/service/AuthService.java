package komunitas.java.service;

import komunitas.java.model.User;

public interface AuthService {
    User login(String username, String password);
    boolean register(String username, String password) throws Exception;
    void logout();
    User getCurrentUser();
}
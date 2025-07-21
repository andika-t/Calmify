package komunitas.java.service.impl;

import komunitas.java.model.User;
import komunitas.java.repository.UserRepository;
import komunitas.java.service.AuthService;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
public User login(String username, String password) {
    System.out.println("Login attempt for: " + username);
    try {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
            return null;
        }
        
        System.out.println("Stored pass: " + user.getPassword() + " | Input pass: " + password);
        
        if (user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful");
            return user;
        }
        
        System.out.println("Password mismatch");
        return null;
    } catch (Exception e) {
        System.err.println("Login error:");
        e.printStackTrace();
        return null;
    }
}

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }
        
        User newUser = new User(username, password);
        return userRepository.save(newUser);
    }
}
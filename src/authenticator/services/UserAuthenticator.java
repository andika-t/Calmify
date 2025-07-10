package authenticator.services;

import authenticator.model.User;

public class UserAuthenticator {
    private final UserService USER_SERVICE;

    public UserAuthenticator(UserService userService){
        this.USER_SERVICE = userService;
    }

    public boolean register(User user){
        if(USER_SERVICE.findByUsername(user.getUsername()) != null){
            return false;
        }
        USER_SERVICE.createUser(user);
        return true;
    }

    public boolean login(String username, String password){
        User u = USER_SERVICE.findByUsername(username);
        return u != null && u.getPassword().equals(password);
    }

    public User findUser(String username){
        return USER_SERVICE.findByUsername(username);
    }
}

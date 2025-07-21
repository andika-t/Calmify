package komunitas.java.model;

import java.time.LocalDate;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private LocalDate birthDate;

    public User(String id, String username, String password, String email, LocalDate birthDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
    }

    public User(String username, String password) {
        this(UUID.randomUUID().toString(), username, password, null, null);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}
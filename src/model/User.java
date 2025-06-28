package model;

public class User {
    private String firstName, lastName, username;
    private String email;
    private String password;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmailAddress() {
        return email;
    }
    public void setEmailAddress(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("User data [ ");
        stringBuilder.append("\nfirstName: ");
        stringBuilder.append(firstName);
        stringBuilder.append("\nlastName: ");
        stringBuilder.append(lastName);
        stringBuilder.append("\nusername: ");
        stringBuilder.append(username);
        stringBuilder.append("\ne-mail: ");
        stringBuilder.append(email);
        stringBuilder.append("\npassword: ");
        stringBuilder.append(password);
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }
}

package authenticator.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String userType;
    private String profilePictureBase64;
    private String phoneNumber;
    private String bio;
    private int totalPoints;
    private List<String> activityHistory;
    private List<String> recoveryCodes;
    private boolean shareData;

    private String address;
    private String country;
    private String city;
    private String postalCode;

    public User() {
    }

    public User(String firstName, String lastName, String username, String email, String password, String userType,
            boolean shadeData) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.profilePictureBase64 = null;
        this.phoneNumber = "";
        this.bio = "";
        this.totalPoints = 0;
        this.activityHistory = new ArrayList<>();
        UserRecoveryCode generator = new UserRecoveryCode();
        this.recoveryCodes = generator.generateRecoveryCodes(2, 8);
        this.shareData = false;

        this.address = "";
        this.country = "";
        this.city = "";
        this.postalCode = "";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isShareData() {
        return shareData;
    }

    public void setShareData(boolean shareData) {
        this.shareData = shareData;
    }

    public String getProfilePictureBase64() {
        return profilePictureBase64;
    }

    public void setProfilePictureBase64(String profilePictureBase64) {
        this.profilePictureBase64 = profilePictureBase64;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<String> getActivityHistory() {
        return activityHistory;
    }

    public void setActivityHistory(List<String> activityHistory) {
        this.activityHistory = activityHistory;
    }

    public List<String> getRecoveryCodes() {
        return recoveryCodes;
    }
}

package selfCare.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

@XStreamAlias("user")
public class GeneralUser implements Serializable {
    @XStreamAlias("username") private String username;
    @XStreamAlias("firstName") private String firstName;
    @XStreamAlias("lastName") private String lastName;
    @XStreamAlias("userType") private String userType;
    @XStreamAlias("shareData") private boolean shareData;

    public GeneralUser() {}

    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUserType() { return userType; }
    public boolean isShareData() { return shareData; }
    
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}
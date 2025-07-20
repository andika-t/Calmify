package selfCare.model; // atau package authenticator.model

import authenticator.model.User;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("user-data")
public class UserContainer {

    // Anotasi ini memberitahu XStream untuk mencari tag <users>
    // dan memasukkan semua <user> di dalamnya ke dalam List ini.
    @XStreamImplicit(itemFieldName = "user")
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }
}
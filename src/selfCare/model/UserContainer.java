package selfCare.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("user-data")
public class UserContainer {
    @XStreamImplicit(itemFieldName = "users")
    private List<GeneralUser> users;

    public List<GeneralUser> getUsers() {
        return users;
    }
}
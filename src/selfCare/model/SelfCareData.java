package selfCare.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("selfCareData")
public class SelfCareData {
    private List<SelfCareUser> selfCareUsers = new ArrayList<>();
    public List<SelfCareUser> getSelfCareUsers() { return selfCareUsers; }
    public void setSelfCareUsers(List<SelfCareUser> users) { this.selfCareUsers = users; }
}
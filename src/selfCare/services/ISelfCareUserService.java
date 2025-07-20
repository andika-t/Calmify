package selfCare.services;
import selfCare.model.SelfCareUser;
import java.util.List;
import java.util.Optional;

public interface ISelfCareUserService {
    List<SelfCareUser> getSharedDataUsers();
    SelfCareUser getOrCreateUser(String username, String name, String userType);
    boolean updateUser(SelfCareUser user);
    boolean deleteUserSelfCareData(String username);
    Optional<SelfCareUser> addPointsToUser(String username, int points, String activityName);
    boolean assignMissionToUser(String username, String activityName);
    Optional<SelfCareUser> completeAssignedMission(String username, String missionName);
}
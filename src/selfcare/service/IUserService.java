package selfCare.service;

import selfCare.model.User;
import selfCare.model.AssignedMission;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();

    boolean addUser(User user);

    boolean deleteUser(String userId);

    boolean updateUser(User user);

    Optional<User> addPointsToUser(String userId, int points, String activityName);

    Optional<User> getUserById(String userId);

    void saveAllUsers();

    boolean assignMissionToUser(String userId, String activityId, String activityName);

    Optional<User> completeAssignedMission(String userId, String missionId);
}

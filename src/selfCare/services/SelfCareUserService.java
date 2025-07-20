package selfCare.services;

import selfCare.model.GeneralUser;
import selfCare.model.SelfCareMission;
import selfCare.model.SelfCareUser;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SelfCareUserService implements ISelfCareUserService {
    private static ISelfCareUserService instance;
    private final IDataManager dataManager;
    private final MainAccountService mainAccountService;
    private List<SelfCareUser> selfCareUsers;

    private SelfCareUserService(IDataManager dataManager) {
        this.dataManager = dataManager;
        this.mainAccountService = MainAccountService.getInstance();
        this.selfCareUsers = this.dataManager.loadUsers();
    }

    public static ISelfCareUserService getInstance() {
        if (instance == null) {
            IDataManager dataManager = new XmlDataManager("data/selfcare_users.xml");
            instance = new SelfCareUserService(dataManager);
        }
        return instance;
    }

    private void save() {
        dataManager.saveUsers(selfCareUsers);
    }
    
    @Override
    public SelfCareUser getOrCreateUser(String username, String name) {
        return selfCareUsers.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElseGet(() -> {
                SelfCareUser newUser = new SelfCareUser(username, name);
                selfCareUsers.add(newUser);
                save();
                return newUser;
            });
    }
    
    @Override
    public List<SelfCareUser> getSharedDataUsers() {
        Set<String> generalUsernames = mainAccountService.getAllGeneralUsers().stream()
            .map(GeneralUser::getUsername)
            .collect(Collectors.toSet());

        return selfCareUsers.stream()
            .filter(scUser -> generalUsernames.contains(scUser.getUsername()) && scUser.isShareData())
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateUser(SelfCareUser updatedUser) {
        for (int i = 0; i < selfCareUsers.size(); i++) {
            if (selfCareUsers.get(i).getUsername().equals(updatedUser.getUsername())) {
                selfCareUsers.set(i, updatedUser);
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteUserSelfCareData(String username) {
        boolean removed = selfCareUsers.removeIf(user -> user.getUsername().equals(username));
        if (removed) save();
        return removed;
    }

    @Override
    public Optional<SelfCareUser> addPointsToUser(String username, int points, String activityName) {
        return selfCareUsers.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .map(user -> {
                user.addPoints(points, activityName);
                updateUser(user);
                return user;
            });
    }

    @Override
    public boolean assignMissionToUser(String username, String activityId, String activityName) {
        return selfCareUsers.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .map(user -> {
                user.addAssignedMission(new SelfCareMission(activityId, activityName));
                return updateUser(user);
            }).orElse(false);
    }

    @Override
    public Optional<SelfCareUser> completeAssignedMission(String username, String missionId) {
        return selfCareUsers.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .flatMap(user -> user.getAssignedMissions().stream()
                .filter(m -> m.getId().equals(missionId) && !m.isCompleted())
                .findFirst()
                .flatMap(mission -> {
                    mission.markAsCompleted();
                    return addPointsToUser(username, 25, "Misi Selesai: " + mission.getActivityName());
                })
            );
    }
}
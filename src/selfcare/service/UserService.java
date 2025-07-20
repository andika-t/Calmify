package selfCare.service;

import selfCare.model.User;
import selfCare.model.AssignedMission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserService implements IUserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final IDataManager dataManager;
    private List<User> users;

    public UserService(IDataManager dataManager) {
        this.dataManager = dataManager;
        this.users = dataManager.loadUsers();
        LOGGER.info("UserService diinisialisasi. " + users.size() + " pengguna dimuat.");
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public boolean addUser(User user) {
        if (users.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            LOGGER.warning("Gagal menambahkan pengguna: ID " + user.getId() + " sudah ada.");
            return false;
        }
        users.add(user);
        LOGGER.info("Pengguna baru ditambahkan: " + user.getName() + " (ID: " + user.getId() + ")");
        saveAllUsers();
        return true;
    }

    @Override
    public boolean deleteUser(String userId) {
        boolean removed = users.removeIf(user -> user.getId().equals(userId));
        if (removed) {
            LOGGER.info("Pengguna dengan ID " + userId + " berhasil dihapus.");
            saveAllUsers();
        } else {
            LOGGER.warning("Gagal menghapus pengguna: ID " + userId + " tidak ditemukan.");
        }
        return removed;
    }

    @Override
    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                LOGGER.info("Pengguna dengan ID " + updatedUser.getId() + " berhasil diperbarui.");
                saveAllUsers();
                return true;
            }
        }
        LOGGER.warning("Gagal memperbarui pengguna: ID " + updatedUser.getId() + " tidak ditemukan.");
        return false;
    }

    @Override
    public Optional<User> addPointsToUser(String userId, int points, String activityName) {
        Optional<User> userOptional = users.stream().filter(u -> u.getId().equals(userId)).findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.addPoints(points, activityName);
            boolean levelChanged = user.updateLevelOrBadge();
            LOGGER.info("Poin ditambahkan ke pengguna " + user.getName() + ". Total poin: " + user.getTotalPoints());
            if (levelChanged) {
                LOGGER.info(
                        "Pengguna " + user.getName() + " mencapai level/badge baru: " + user.getCurrentLevelOrBadge());

            }
            saveAllUsers();
            return Optional.of(user);
        } else {
            LOGGER.warning("Gagal menambahkan poin: Pengguna dengan ID " + userId + " tidak ditemukan.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();
    }

    @Override
    public void saveAllUsers() {
        dataManager.saveUsers(users);
        LOGGER.info("Semua data pengguna disimpan.");
    }

    @Override
    public boolean assignMissionToUser(String userId, String activityId, String activityName) {
        Optional<User> userOptional = users.stream().filter(u -> u.getId().equals(userId)).findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            AssignedMission newMission = new AssignedMission(activityId, activityName);
            user.addAssignedMission(newMission);
            LOGGER.info("Misi '" + activityName + "' ditugaskan kepada " + user.getName() + ".");
            saveAllUsers();
            return true;
        } else {
            LOGGER.warning("Gagal menugaskan misi: Pengguna dengan ID " + userId + " tidak ditemukan.");
            return false;
        }
    }

    @Override
    public Optional<User> completeAssignedMission(String userId, String missionId) {
        Optional<User> userOptional = users.stream().filter(u -> u.getId().equals(userId)).findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<AssignedMission> missionOptional = user.getAssignedMissions().stream()
                    .filter(m -> m.getId().equals(missionId) && !m.isCompleted()).findFirst();

            if (missionOptional.isPresent()) {
                AssignedMission mission = missionOptional.get();
                mission.markAsCompleted();
                LOGGER.info("Misi '" + mission.getActivityName() + "' ditandai selesai untuk " + user.getName() + ".");
                saveAllUsers();
                return Optional.of(user);
            } else {
                LOGGER.warning("Gagal menyelesaikan misi: Misi dengan ID " + missionId
                        + " tidak ditemukan atau sudah selesai untuk pengguna " + userId + ".");
                return Optional.empty();
            }
        } else {
            LOGGER.warning("Gagal menyelesaikan misi: Pengguna dengan ID " + userId + " tidak ditemukan.");
            return Optional.empty();
        }
    }
}

package selfCare.services;

import selfCare.model.AssignedMission;
import selfCare.model.PointHistory;
import selfCare.model.SelfCareUser;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SelfCareUserService implements ISelfCareUserService {
    private static SelfCareUserService instance;
    private final IDataManager dataManager;
    private final Map<String, SelfCareUser> users;

    private SelfCareUserService(IDataManager dataManager) {
        this.dataManager = dataManager;
        this.users = loadData();
    }

    public static synchronized SelfCareUserService getInstance() {
        if (instance == null) {
            instance = new SelfCareUserService(new XmlDataManager("data/selfCareData.xml"));
        }
        return instance;
    }

    private Map<String, SelfCareUser> loadData() {
        return dataManager.loadUsers().stream()
                .collect(Collectors.toMap(SelfCareUser::getUsername, Function.identity()));
    }

    private void saveData() {
        dataManager.saveUsers(new ArrayList<>(users.values()));
    }

    @Override
    public List<SelfCareUser> getSharedDataUsers() {
        return users.values().stream()
                .filter(SelfCareUser::isShareData)
                .collect(Collectors.toList());
    }

    @Override
    public SelfCareUser getOrCreateUser(String username, String name, String userType) {
        return users.computeIfAbsent(username, u -> {
            SelfCareUser newUser = new SelfCareUser(u, name, userType);
            saveData();
            return newUser;
        });
    }

    @Override
    public boolean updateUser(SelfCareUser user) {
        if (user == null || !users.containsKey(user.getUsername()))
            return false;
        users.put(user.getUsername(), user);
        saveData();
        return true;
    }

    @Override
    public boolean deleteUserSelfCareData(String username) {
        if (users.remove(username) != null) {
            saveData();
            return true;
        }
        return false;
    }

    @Override
    public Optional<SelfCareUser> addPointsToUser(String username, int points, String activityName) {
        SelfCareUser user = users.get(username);
        if (user != null) {
            user.addPoints(points);
            user.getPointHistory().add(new PointHistory(activityName, points));
            saveData();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
public boolean assignMissionToUser(String username, String activityName) {
    SelfCareUser user = users.get(username);
    if (user == null) {
        return false; // User tidak ditemukan
    }

    // === PERBAIKAN DIMULAI DI SINI ===
    List<AssignedMission> missions = user.getAssignedMissions();

    // Periksa apakah list-nya null sebelum digunakan
    if (missions == null) {
        // Ini adalah kondisi data yang tidak diharapkan.
        // Kita bisa catat sebagai error dan kembalikan false agar aplikasi tidak crash.
        System.err.println("FATAL: List 'assignedMissions' untuk user '" + username + "' adalah null!");
        return false;
    }

    // Jika tidak null, baru tambahkan misi
    missions.add(new AssignedMission(activityName));
    // === PERBAIKAN SELESAI ===
    
    saveData();
    return true;
}


    @Override
    public Optional<SelfCareUser> completeAssignedMission(String username, String missionName) {
        SelfCareUser user = users.get(username);
        if (user == null)
            return Optional.empty();

        Optional<AssignedMission> missionOpt = user.getAssignedMissions().stream()
                .filter(m -> m.getMissionName().equals(missionName)
                        && m.getStatus() == AssignedMission.MissionStatus.DITUGASKAN)
                .findFirst();

        if (missionOpt.isPresent()) {
            AssignedMission mission = missionOpt.get();
            mission.setStatus(AssignedMission.MissionStatus.SELESAI);
            mission.setCompletionDate(LocalDate.now());
            addPointsToUser(username, 50, "Misi Selesai: " + missionName);
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
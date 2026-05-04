package repo;

import models.Locker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockerRepository {
    private final Map<String, Locker> lockerMap = new HashMap<>();
    private final Map<String, List<Locker>> zipToLockerMap = new HashMap<>();

    public void save(Locker locker) {
        lockerMap.put(locker.getName(), locker);
        zipToLockerMap.computeIfAbsent(locker.getZipCode(), k -> new ArrayList<>()).add(locker);
    }

    public Locker getLockerByName(String lockerName) {
        return lockerMap.get(lockerName);
    }

    public List<Locker> getLockersByZip(String zipCode) {
        return zipToLockerMap.getOrDefault(zipCode, List.of());
    }
}

package repo;

import models.Package;

import java.util.HashMap;
import java.util.Map;

public class PackageRepository {
    private final Map<String, Package> packages = new HashMap<>();

    public void save(Package pkg) {
        packages.put(pkg.getId(), pkg);
    }

    public Package getById(String id) {
        return packages.get(id);
    }
}

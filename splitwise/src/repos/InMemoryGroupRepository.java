package repos;

import models.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryGroupRepository implements GroupRepository {
    private final Map<String, Group> groupMap = new HashMap<>();

    @Override
    public Optional<Group> findById(String id) {
        return Optional.ofNullable(groupMap.get(id));
    }

    @Override
    public void save(Group group) {
        groupMap.put(group.getId(), group);
    }
}

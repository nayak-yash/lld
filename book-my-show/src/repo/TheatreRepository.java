package repo;

import models.Theatre;

import java.util.HashMap;
import java.util.Map;

public class TheatreRepository {
    private final Map<String, Theatre> theatreMap = new HashMap<>();

    public void save(Theatre theatre) {
        theatreMap.put(theatre.getId(), theatre);
    }

    public Theatre getById(String id) {
        return theatreMap.get(id);
    }
}

package repo;

import models.Show;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowRepository {
    private final Map<String, Show> showMap = new HashMap<>();

    public void save(Show show) {
        showMap.put(show.getId(), show);
    }

    public Show getById(String id) {
        return showMap.get(id);
    }

    public List<Show> getAll() {
        return showMap.values().stream().toList();
    }
}

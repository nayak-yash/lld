package repo;

import models.ATM;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ATMRepository {
    private final Map<String, ATM> atms = new HashMap<>();

    public void save(ATM atm) {
        atms.put(atm.getId(), atm);
    }

    public Optional<ATM> getById(String id) {
        return Optional.ofNullable(atms.get(id));
    }
}

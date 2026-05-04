package models;

import java.util.ArrayList;
import java.util.List;

public class Locker {
    private final String name;
    private final String zipCode;
    private final List<Slot> slots;

    public Locker(String name, String zipCode) {
        this.name = name;
        this.zipCode = zipCode;
        this.slots = new ArrayList<>();
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public Slot getSlotById(String slotId) {
        return slots.stream()
                .filter(slot -> slot.getId().equals(slotId))
                .findFirst()
                .orElse(null);
    }

    public List<Slot> getSlots() { return slots; }
    public String getName() { return name; }
    public String getZipCode() { return zipCode; }
}

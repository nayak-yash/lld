package strategy;

import models.Slot;

import java.util.List;

public class FirstFitSlotStrategy implements SlotAssignmentStrategy {

    @Override
    public Slot assign(List<Slot> eligibleSlots) {
        for (Slot slot : eligibleSlots) {
            if (slot.acquire()) {
                return slot;
            }
        }
        return null;
    }
}

package strategy;

import models.Slot;

import java.util.List;

public interface SlotAssignmentStrategy {

    Slot assign(List<Slot> eligibleSlots);
}

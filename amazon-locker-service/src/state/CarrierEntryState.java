package state;

import enums.LockerStatus;
import service.LockerMachine;

public class CarrierEntryState implements LockerState {
    @Override
    public void touch(LockerMachine lockerMachine) {
        System.out.println("[CarrierEntry] Screen already active.");
    }

    @Override
    public void selectPickup(LockerMachine lockerMachine) {
        System.out.println("[CarrierEntry] Cannot switch to pickup from here.");
    }

    @Override
    public void selectCarrierEntry(LockerMachine lockerMachine) {
        System.out.println("[CarrierEntry] Already in carrier entry state.");
    }

    @Override
    public void selectOption(String option, LockerMachine lockerMachine) {
        if ("DROP_OPTION".equals(option)) {
            System.out.println("[CarrierEntry] Drop option selected. Transitioning to AGENT_DELIVERY.");
            lockerMachine.setLockerState(new AgentDeliveryState());
        } else {
            System.out.println("[CarrierEntry] Unknown option: " + option);
        }
    }

    @Override
    public void validateCode(String code, String lockerName, LockerMachine lockerMachine) {
        System.out.println("[CarrierEntry] Code validation not applicable. Please select an option first.");
    }

    @Override
    public void closeDoor(String lockerName, String slotId, LockerMachine lockerMachine) {
        System.out.println("[CarrierEntry] Door close not applicable in carrier entry state.");
    }

    @Override
    public LockerStatus getStatus() {
        return LockerStatus.CARRIER_ENTRY;
    }
}

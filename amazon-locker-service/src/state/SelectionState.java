package state;

import enums.LockerStatus;
import service.LockerMachine;

public class SelectionState implements LockerState {

    @Override
    public void touch(LockerMachine lockerMachine) {
        System.out.println("[Selection] Screen already active. Please choose an option.");
    }

    @Override
    public void selectPickup(LockerMachine lockerMachine) {
        System.out.println("[Selection] Customer pickup selected. Transitioning to CUSTOMER_PICKUP.");
        lockerMachine.setLockerState(new CustomerPickupState());
    }

    @Override
    public void selectCarrierEntry(LockerMachine lockerMachine) {
        System.out.println("[Selection] Carrier entry selected. Transitioning to CARRIER_ENTRY.");
        lockerMachine.setLockerState(new CarrierEntryState());
    }

    @Override
    public void selectOption(String option, LockerMachine lockerMachine) {
        System.out.println("[Selection] Invalid action. Please select 'Pick up' or 'Carrier entry'.");
    }

    @Override
    public void validateCode(String code, String lockerName, LockerMachine lockerMachine) {
        System.out.println("[Selection] Invalid action. Please select an option first.");
    }

    @Override
    public void closeDoor(String lockerName, String slotId, LockerMachine lockerMachine) {
        System.out.println("[Selection] Invalid action. No door operation in selection state.");
    }

    @Override
    public LockerStatus getStatus() {
        return LockerStatus.SELECTION;
    }
}

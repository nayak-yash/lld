package state;

import enums.LockerStatus;
import service.LockerMachine;

public class IdleState implements LockerState {
    @Override
    public void touch(LockerMachine lockerMachine) {
        System.out.println("[Idle] Screen touched. Transitioning to SELECTION.");
        lockerMachine.setLockerState(new SelectionState());
    }

    @Override
    public void selectPickup(LockerMachine lockerMachine) {
        System.out.println("[Idle] Please touch the screen first.");
    }

    @Override
    public void selectCarrierEntry(LockerMachine lockerMachine) {
        System.out.println("[Idle] Please touch the screen first.");
    }

    @Override
    public void selectOption(String option, LockerMachine lockerMachine) {
        System.out.println("[Idle] Please touch the screen first.");
    }

    @Override
    public void validateCode(String code, String lockerName, LockerMachine lockerMachine) {
        System.out.println("[Idle] Please touch the screen first.");
    }

    @Override
    public void closeDoor(String lockerName, String slotId, LockerMachine lockerMachine) {
        System.out.println("[Idle] No door operation in idle state.");
    }

    @Override
    public LockerStatus getStatus() {
        return LockerStatus.IDLE;
    }
}

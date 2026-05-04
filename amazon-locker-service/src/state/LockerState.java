package state;

import enums.LockerStatus;
import service.LockerMachine;

public interface LockerState {
    void touch(LockerMachine lockerMachine);
    void selectPickup(LockerMachine lockerMachine);
    void selectCarrierEntry(LockerMachine lockerMachine);
    void selectOption(String option, LockerMachine lockerMachine);
    void validateCode(String code, String lockerName, LockerMachine lockerMachine);
    void closeDoor(String lockerName, String slotId, LockerMachine lockerMachine);
    LockerStatus getStatus();
}

package state;

import enums.LockerStatus;
import enums.PackageStatus;
import models.Locker;
import models.Package;
import models.Slot;
import service.LockerMachine;

public class CustomerPickupState implements LockerState {
    @Override
    public void touch(LockerMachine lockerMachine) {
        System.out.println("[CustomerPickup] Screen already active.");
    }

    @Override
    public void selectPickup(LockerMachine lockerMachine) {
        System.out.println("[CustomerPickup] Already in pickup mode.");
    }

    @Override
    public void selectCarrierEntry(LockerMachine lockerMachine) {
        System.out.println("[CustomerPickup] Cannot switch to carrier entry from here. Please go back to idle first.");
    }

    @Override
    public void validateCode(String otp, String lockerName, LockerMachine lockerMachine) {
        try {
            String slotId = lockerMachine.getOtpService().validateAndGetSlotId(otp, lockerName);
            System.out.println("[CustomerPickup] OTP valid! Locker opened for pickup: Slot '" + slotId + "'.");
            lockerMachine.getOtpService().invalidateOtp(otp);
        } catch (Exception e) {
            System.out.println("[CustomerPickup] OTP validation failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeDoor(String lockerName, String slotId, LockerMachine lockerMachine) {
        Locker locker = lockerMachine.getLockerService().getLockerByName(lockerName);
        Slot slot = locker.getSlotById(slotId);
        Package pkg = slot.getStoredPackage();
        if (pkg != null) {
            pkg.setPackageStatus(PackageStatus.PICKED_UP);
            System.out.println("[CustomerPickup] Package '" + pkg.getId() + "' picked up successfully.");
        }
        slot.setStoredPackage(null);
        slot.release();
        System.out.println("[CustomerPickup] Door closed after pickup. Slot '" + slotId + "' is now available. Transitioning to IDLE.");
        lockerMachine.setLockerState(new IdleState());
    }

    @Override
    public void selectOption(String option, LockerMachine lockerMachine) {
        System.out.println("[CustomerPickup] No options available in pickup state.");
    }

    @Override
    public LockerStatus getStatus() {
        return LockerStatus.CUSTOMER_PICKUP;
    }
}

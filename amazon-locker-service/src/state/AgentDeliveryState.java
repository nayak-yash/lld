package state;

import enums.LockerStatus;
import enums.PackageStatus;
import models.Locker;
import models.OtpInfo;
import models.Package;
import models.Slot;
import service.LockerMachine;

import java.util.Objects;

public class AgentDeliveryState implements LockerState {
    @Override
    public void touch(LockerMachine lockerMachine) {
        System.out.println("[AgentDelivery] Screen already active for delivery.");
    }

    @Override
    public void selectPickup(LockerMachine lockerMachine) {
        System.out.println("[AgentDelivery] Cannot switch to pickup during delivery.");
    }

    @Override
    public void selectCarrierEntry(LockerMachine lockerMachine) {
        System.out.println("[AgentDelivery] Already in delivery mode.");
    }

    @Override
    public void selectOption(String option, LockerMachine lockerMachine) {
        System.out.println("[AgentDelivery] No options available in delivery state.");
    }

    @Override
    public void validateCode(String packageId, String lockerName, LockerMachine lockerMachine) {
        Package pkg = lockerMachine.getPackageRepository().getById(packageId);
        if (pkg == null) {
            throw new RuntimeException("Package with id '" + packageId + "' does not exist.");
        }
        if (!Objects.equals(pkg.getLockerName(), lockerName)) {
            throw new RuntimeException("Package '" + packageId + "' belongs to a different locker.");
        }
        Locker locker = lockerMachine.getLockerService().getLockerByName(lockerName);
        Slot slot = locker.getSlotById(pkg.getSlotId());
        slot.setStoredPackage(pkg);
        System.out.println("[AgentDelivery] Package '" + packageId + "' verified. Locker slot '" + slot.getId() + "' opened for delivery.");
    }

    @Override
    public void closeDoor(String lockerName, String slotId, LockerMachine lockerMachine) {
        Locker locker = lockerMachine.getLockerService().getLockerByName(lockerName);
        Slot slot = locker.getSlotById(slotId);
        Package pkg = slot.getStoredPackage();
        pkg.setPackageStatus(PackageStatus.STORED_IN_LOCKER);
        OtpInfo otpInfo = lockerMachine.getOtpService().generateOtp(lockerName, slotId);
        System.out.println("[AgentDelivery] Package '" + pkg.getId() + "' stored in locker. Door closed. Transitioning to IDLE.");
        lockerMachine.getNotificationService().notifyCustomer(pkg, otpInfo);
        lockerMachine.setLockerState(new IdleState());
    }

    @Override
    public LockerStatus getStatus() {
        return LockerStatus.AGENT_DELIVERY;
    }
}

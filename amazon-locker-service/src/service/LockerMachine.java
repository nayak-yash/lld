package service;

import enums.LockerStatus;
import models.Locker;
import repo.PackageRepository;
import state.IdleState;
import state.LockerState;

public class LockerMachine {
    private final LockerService lockerService;
    private final PackageRepository packageRepository;
    private final OtpService otpService;
    private final NotificationService notificationService;
    private final Locker locker;
    private LockerState lockerState;

    public LockerMachine(LockerService lockerService, PackageRepository packageRepository,
                         OtpService otpService, NotificationService notificationService, Locker locker) {
        this.lockerService = lockerService;
        this.packageRepository = packageRepository;
        this.otpService = otpService;
        this.notificationService = notificationService;
        this.locker = locker;
        this.lockerState = new IdleState();
    }

    public void touch() {
        lockerState.touch(this);
    }

    public void selectPickup() {
        lockerState.selectPickup(this);
    }

    public void selectCarrierEntry() {
        lockerState.selectCarrierEntry(this);
    }

    public void selectOption(String option) {
        lockerState.selectOption(option, this);
    }

    public void validateCode(String code) {
        lockerState.validateCode(code, locker.getName(), this);
    }

    public void closeDoor(String slotId) {
        lockerState.closeDoor(locker.getName(), slotId, this);
    }

    public LockerStatus getStatus() {
        return lockerState.getStatus();
    }

    public LockerService getLockerService() { return lockerService; }
    public PackageRepository getPackageRepository() { return packageRepository; }
    public OtpService getOtpService() { return otpService; }
    public NotificationService getNotificationService() { return notificationService; }
    public Locker getLocker() { return locker; }

    // Setter only for state (changes at runtime via state pattern)
    public void setLockerState(LockerState newLockerState) { this.lockerState = newLockerState; }
}

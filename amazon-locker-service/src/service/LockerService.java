package service;

import enums.PackageSize;
import models.Locker;
import models.Package;
import models.Slot;
import repo.LockerRepository;
import strategy.SlotAssignmentStrategy;

import java.util.List;

public class LockerService {
    private final LockerRepository lockerRepository;
    private final SlotAssignmentStrategy slotStrategy;

    public LockerService(LockerRepository lockerRepository, SlotAssignmentStrategy slotStrategy) {
        this.lockerRepository = lockerRepository;
        this.slotStrategy = slotStrategy;
    }

    public void save(Locker locker) {
        lockerRepository.save(locker);
    }

    public Locker getLockerByName(String lockerName) {
        return lockerRepository.getLockerByName(lockerName);
    }

    public List<Locker> getLockersByZipAndSize(String zipCode, PackageSize packageSize) {
        List<Locker> lockers = lockerRepository.getLockersByZip(zipCode);
        return lockers.stream()
                .filter(locker -> locker.getSlots().stream()
                        .anyMatch(slot -> slot.getPackageSize().canFit(packageSize) && slot.isAvailable()))
                .toList();
    }

    public void reserveSlotForPackage(Locker locker, Package pkg) {
        List<Slot> eligibleSlots = getEligibleSlots(locker, pkg);
        Slot reservedSlot = slotStrategy.assign(eligibleSlots);
        if (reservedSlot == null) {
            throw new RuntimeException("No available slot in locker '" + locker.getName() + "' for package '" + pkg.getId() + "'.");
        }
        pkg.setLockerName(locker.getName());
        pkg.setSlotId(reservedSlot.getId());
        System.out.println("[LockerService] Reserved slot '" + reservedSlot.getId() + "' in locker '" + locker.getName() + "' for package '" + pkg.getId() + "'.");
    }

    public List<Slot> getEligibleSlots(Locker locker, Package pkg) {
        return locker.getSlots().stream()
                .filter(slot -> slot.getPackageSize().canFit(pkg.getPackageSize()) && slot.isAvailable())
                .toList();
    }
}

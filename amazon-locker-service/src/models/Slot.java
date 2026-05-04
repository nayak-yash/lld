package models;

import enums.PackageSize;

import java.util.concurrent.atomic.AtomicBoolean;

public class Slot {
    private final String id;
    private final PackageSize packageSize;
    private final AtomicBoolean available = new AtomicBoolean(true);
    private Package storedPackage;

    public Slot(String id, PackageSize packageSize) {
        this.id = id;
        this.packageSize = packageSize;
    }

    public boolean isAvailable() { return available.get(); }
    public boolean acquire() { return available.compareAndSet(true, false); }
    public void release() { available.set(true); }

    public String getId() { return id; }
    public PackageSize getPackageSize() { return packageSize; }
    public Package getStoredPackage() { return storedPackage; }

    // Setter only for stored package (changes at runtime)
    public void setStoredPackage(Package storedPackage) { this.storedPackage = storedPackage; }
}

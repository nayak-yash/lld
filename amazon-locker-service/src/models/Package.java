package models;

import enums.PackageSize;
import enums.PackageStatus;

public class Package {
    private final String id;
    private final PackageSize packageSize;
    private final String customerId;
    private String lockerName;
    private String slotId;
    private String deliveryAgentId;
    private PackageStatus packageStatus;

    public Package(String id, PackageSize packageSize, String customerId) {
        this.id = id;
        this.packageSize = packageSize;
        this.customerId = customerId;
        this.packageStatus = PackageStatus.CREATED;
    }

    public String getId() { return id; }
    public PackageSize getPackageSize() { return packageSize; }
    public String getCustomerId() { return customerId; }
    public String getLockerName() { return lockerName; }
    public String getSlotId() { return slotId; }
    public String getDeliveryAgentId() { return deliveryAgentId; }
    public PackageStatus getPackageStatus() { return packageStatus; }

    // Setters only for fields that change after creation
    public void setLockerName(String lockerName) { this.lockerName = lockerName; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public void setDeliveryAgentId(String deliveryAgentId) { this.deliveryAgentId = deliveryAgentId; }
    public void setPackageStatus(PackageStatus packageStatus) { this.packageStatus = packageStatus; }
}

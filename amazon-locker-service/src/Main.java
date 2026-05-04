import enums.PackageSize;
import models.*;
import models.Package;
import repo.*;
import service.*;
import strategy.FirstFitSlotStrategy;
import strategy.RandomAgentAssignmentStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== Amazon Locker Service Demo =====\n");

        // --- Step 1: Initialize Repositories ---
        CustomerRepository customerRepo = new CustomerRepository();
        AgentRepository agentRepo = new AgentRepository();
        LockerRepository lockerRepo = new LockerRepository();
        PackageRepository packageRepo = new PackageRepository();

        // --- Step 2: Create and save a Customer ---
        Customer customer = new Customer("C1", "Alice");
        customerRepo.save(customer);
        System.out.println("[Setup] Customer created: " + customer.getName() + " (ID: " + customer.getId() + ")");

        // --- Step 3: Create and register a Delivery Agent ---
        DeliveryAgent agent = new DeliveryAgent("A1", "Bob");
        agentRepo.save(agent);
        agentRepo.assignAgentToZip(agent, "10001");
        System.out.println("[Setup] Delivery agent registered: " + agent.getName() + " (ID: " + agent.getId() + ") for zip code 10001");

        // --- Step 4: Create a Locker with slots ---
        Locker locker = new Locker("Locker-NYC-1", "10001");
        locker.addSlot(new Slot("S1", PackageSize.SMALL));
        locker.addSlot(new Slot("S2", PackageSize.MEDIUM));
        locker.addSlot(new Slot("S3", PackageSize.LARGE));
        lockerRepo.save(locker);
        System.out.println("[Setup] Locker created: '" + locker.getName() + "' with 3 slots (SMALL, MEDIUM, LARGE) at zip 10001");

        // --- Step 5: Initialize Services ---
        OtpService otpService = new OtpService();
        NotificationService notificationService = new NotificationService(customerRepo, agentRepo);
        LockerService lockerService = new LockerService(lockerRepo, new FirstFitSlotStrategy());
        AgentService agentService = new AgentService(agentRepo, new RandomAgentAssignmentStrategy(), notificationService);

        // --- Step 6: Create a Package ---
        Package pkg = new Package("PKG-001", PackageSize.MEDIUM, customer.getId());
        packageRepo.save(pkg);
        System.out.println("\n[Order] Package created: '" + pkg.getId() + "' (Size: " + pkg.getPackageSize() + ") for customer '" + customer.getName() + "'");

        // --- Step 7: Find available lockers and reserve a slot ---
        System.out.println("\n--- Reserving a locker slot for the package ---");
        List<Locker> availableLockers = lockerService.getLockersByZipAndSize("10001", pkg.getPackageSize());
        if (availableLockers.isEmpty()) {
            System.out.println("[Error] No available lockers found for zip 10001 with size " + pkg.getPackageSize());
            return;
        }
        Locker selectedLocker = availableLockers.get(0);
        lockerService.reserveSlotForPackage(selectedLocker, pkg);

        // --- Step 8: Assign a delivery agent ---
        System.out.println("\n--- Assigning a delivery agent ---");
        agentService.assignAgentToDelivery(selectedLocker, pkg);

        // --- Step 9: Locker Machine - Agent Delivery Flow ---
        // IDLE -> touch -> SELECTION -> selectCarrierEntry -> CARRIER_ENTRY -> DROP_OPTION -> AGENT_DELIVERY -> validateCode -> closeDoor -> IDLE
        System.out.println("\n--- Agent delivers the package to the locker ---");
        LockerMachine lockerMachine = new LockerMachine(lockerService, packageRepo, otpService, notificationService, selectedLocker);
        System.out.println("Current state: " + lockerMachine.getStatus());

        lockerMachine.touch();
        System.out.println("Current state: " + lockerMachine.getStatus());

        lockerMachine.selectCarrierEntry();
        System.out.println("Current state: " + lockerMachine.getStatus());

        lockerMachine.selectOption("DROP_OPTION");
        System.out.println("Current state: " + lockerMachine.getStatus());

        lockerMachine.validateCode(pkg.getId());

        lockerMachine.closeDoor(pkg.getSlotId());
        System.out.println("Current state: " + lockerMachine.getStatus());

        // --- Step 10: Locker Machine - Customer Pickup Flow ---
        // IDLE -> touch -> SELECTION -> selectPickup -> CUSTOMER_PICKUP -> validateCode(OTP) -> closeDoor -> IDLE
        System.out.println("\n--- Customer picks up the package ---");
        String customerOtp = otpService.getOtpBySlot(selectedLocker.getName(), pkg.getSlotId());
        System.out.println("Customer uses OTP received from notification: " + customerOtp);

        lockerMachine.touch();
        System.out.println("Current state: " + lockerMachine.getStatus());

        lockerMachine.selectPickup();
        System.out.println("Current state: " + lockerMachine.getStatus());

        lockerMachine.validateCode(customerOtp);

        lockerMachine.closeDoor(pkg.getSlotId());
        System.out.println("Current state: " + lockerMachine.getStatus());

        // --- Final status ---
        System.out.println("\n===== Demo Complete =====");
        System.out.println("Package '" + pkg.getId() + "' final status: " + pkg.getPackageStatus());
    }
}

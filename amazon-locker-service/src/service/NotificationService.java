package service;

import models.Customer;
import models.DeliveryAgent;
import models.OtpInfo;
import models.Package;
import repo.AgentRepository;
import repo.CustomerRepository;

public class NotificationService {
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;

    public NotificationService(CustomerRepository customerRepository, AgentRepository agentRepository) {
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
    }

    public void notifyCustomer(Package pkg, OtpInfo otpInfo) {
        Customer customer = customerRepository.getById(pkg.getCustomerId());
        System.out.println("[Notification] Dear customer '" + customer.getName() + "' (ID: " + customer.getId()
                + "), your package has been delivered to locker '" + pkg.getLockerName()
                + "' at slot '" + pkg.getSlotId() + "'. Use OTP: " + otpInfo.getOtp()
                + " to retrieve your package. Valid until: " + otpInfo.getExpiryTime());
    }

    public void notifyAgent(Package pkg) {
        DeliveryAgent deliveryAgent = agentRepository.getByAgentId(pkg.getDeliveryAgentId());
        System.out.println("[Notification] Agent '" + deliveryAgent.getName() + "' (ID: " + deliveryAgent.getId()
                + "), please deliver package '" + pkg.getId()
                + "' to locker '" + pkg.getLockerName() + "', slot '" + pkg.getSlotId() + "'.");
    }
}

package service;

import enums.PackageStatus;
import models.DeliveryAgent;
import models.Locker;
import models.Package;
import repo.AgentRepository;
import strategy.AgentAssignmentStrategy;

import java.util.List;

public class AgentService {
    private final AgentRepository agentRepository;
    private final AgentAssignmentStrategy agentAssignmentStrategy;
    private final NotificationService notificationService;

    public AgentService(AgentRepository agentRepository, AgentAssignmentStrategy agentAssignmentStrategy,
                        NotificationService notificationService) {
        this.agentRepository = agentRepository;
        this.agentAssignmentStrategy = agentAssignmentStrategy;
        this.notificationService = notificationService;
    }

    public void registerAgent(DeliveryAgent deliveryAgent) {
        agentRepository.save(deliveryAgent);
    }

    public DeliveryAgent assignAgentToDelivery(Locker locker, Package pkg) {
        String zipCode = locker.getZipCode();
        List<DeliveryAgent> availableAgents = agentRepository.getDeliveryAgentsByZip(zipCode);
        if (availableAgents == null || availableAgents.isEmpty()) {
            throw new RuntimeException("No delivery agents available in zip code: " + zipCode);
        }
        DeliveryAgent deliveryAgent = agentAssignmentStrategy.assign(availableAgents);
        pkg.setDeliveryAgentId(deliveryAgent.getId());
        pkg.setPackageStatus(PackageStatus.ASSIGNED);
        notificationService.notifyAgent(pkg);
        return deliveryAgent;
    }
}

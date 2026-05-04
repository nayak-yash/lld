package repo;

import models.DeliveryAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentRepository {
    private final Map<String, DeliveryAgent> agentsMap = new HashMap<>();
    private final Map<String, List<DeliveryAgent>> zipToAgentsMap = new HashMap<>();

    public void save(DeliveryAgent deliveryAgent) {
        agentsMap.put(deliveryAgent.getId(), deliveryAgent);
    }

    public void assignAgentToZip(DeliveryAgent agent, String zipCode) {
        zipToAgentsMap.computeIfAbsent(zipCode, k -> new ArrayList<>()).add(agent);
    }

    public DeliveryAgent getByAgentId(String agentId) {
        return agentsMap.get(agentId);
    }

    public List<DeliveryAgent> getDeliveryAgentsByZip(String zipCode) {
        return zipToAgentsMap.getOrDefault(zipCode, List.of());
    }
}

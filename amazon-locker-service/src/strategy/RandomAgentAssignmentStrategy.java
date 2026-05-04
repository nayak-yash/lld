package strategy;

import models.DeliveryAgent;

import java.util.List;
import java.util.Random;

public class RandomAgentAssignmentStrategy implements AgentAssignmentStrategy {

    private final Random random = new Random();

    @Override
    public DeliveryAgent assign(List<DeliveryAgent> agents) {
        if (agents.isEmpty()) {
            return null;
        }
        return agents.get(random.nextInt(agents.size()));
    }
}

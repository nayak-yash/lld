package strategy;

import models.DeliveryAgent;

import java.util.List;

public interface AgentAssignmentStrategy {

    DeliveryAgent assign(List<DeliveryAgent> agents);
}

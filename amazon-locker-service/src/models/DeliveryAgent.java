package models;

public class DeliveryAgent {
    private final String id;
    private final String name;

    public DeliveryAgent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
}

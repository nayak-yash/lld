package models;

public class Split {
    private final User user;
    private final double amount;

    public Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }
}

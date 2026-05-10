package models;

import java.util.HashMap;
import java.util.Map;

public class BalanceSheet {
    private double totalPaid;
    private double totalExpense;
    private final Map<User, Double> balances = new HashMap<>();

    public void addTotalPaid(double amount) {
        this.totalPaid += amount;
    }

    public void addTotalExpense(double amount) {
        this.totalExpense += amount;
    }

    public void addBalance(User otherUser, double amount) {
        balances.put(otherUser, balances.getOrDefault(otherUser, 0.0) + amount);
        if (Math.abs(balances.get(otherUser)) < 1e-6) {
            balances.remove(otherUser);
        }
    }

    public void clearBalances() {
        balances.clear();
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public Map<User, Double> getBalances() {
        return balances;
    }
}

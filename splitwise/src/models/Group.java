package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Group {
    private final String id;
    private final String name;
    private final List<User> members;
    private final List<Expense> expenses;
    private final Map<User, BalanceSheet> balanceSheets;

    public Group(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.balanceSheets = new HashMap<>();
    }

    public void addMember(User user) {
        members.add(user);
        balanceSheets.put(user, new BalanceSheet());
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public BalanceSheet getBalanceSheet(User user) {
        return balanceSheets.get(user);
    }

    public Map<User, BalanceSheet> getBalanceSheets() {
        return balanceSheets;
    }
}

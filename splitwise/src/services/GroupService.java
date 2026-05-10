package services;

import enums.SplitType;
import models.Expense;
import models.Group;
import models.User;
import repos.GroupRepository;

import java.util.List;
import java.util.Map;

public class GroupService {
    private final GroupRepository groupRepository;
    private final ExpenseService expenseService;
    private final DebtSimplificationService debtSimplificationService;

    public GroupService(GroupRepository groupRepository, ExpenseService expenseService, DebtSimplificationService debtSimplificationService) {
        this.groupRepository = groupRepository;
        this.expenseService = expenseService;
        this.debtSimplificationService = debtSimplificationService;
    }

    public String createGroup(String name, List<User> members) {
        Group group = new Group(name);
        members.forEach(group::addMember);
        groupRepository.save(group);
        return group.getId();
    }

    public void addMember(String groupId, User user) {
        getGroup(groupId).addMember(user);
    }

    public void addExpense(String groupId,
                           String description,
                           double amount,
                           User paidBy,
                           List<User> participants,
                           SplitType splitType,
                           Map<User, Double> meta) {
        expenseService.addExpense(getGroup(groupId), description, amount, paidBy, participants, splitType, meta);
    }

    public void simplifyDebts(String groupId) {
        debtSimplificationService.simplifyDebts(getGroup(groupId));
    }

    public void printBalances(String groupId) {
        Group group = getGroup(groupId);

        System.out.println("==========================================");
        System.out.println("Group: " + group.getName());
        System.out.println("------------------------------------------");
        System.out.println("Expenses:");
        for (Expense expense : group.getExpenses()) {
            System.out.printf("  [%s] %.2f paid by %s (%s split)%n",
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getPaidBy().getName(),
                    expense.getSplitType().name().toLowerCase());
        }
        System.out.println("------------------------------------------");

        group.getMembers().forEach(user -> {
            var balanceSheet = group.getBalanceSheet(user);

            double owe = 0, get = 0;
            for (double v : balanceSheet.getBalances().values()) {
                if (v < 0) owe -= v; else get += v;
            }
            System.out.printf("  %s | paid: %.2f | share: %.2f | owes: %.2f | gets back: %.2f%n",
                    user.getName(),
                    balanceSheet.getTotalPaid(),
                    balanceSheet.getTotalExpense(),
                    owe,
                    get);

            balanceSheet.getBalances().forEach((other, val) -> System.out.printf(
                    "      %s %s %.2f%n",
                    user.getName(),
                    val > 0 ? "gets back from " + other.getName() : "owes " + other.getName(),
                    Math.abs(val)));
        });
        System.out.println("==========================================");
    }

    private Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + groupId));
    }
}

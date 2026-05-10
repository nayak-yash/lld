package services;

import enums.SplitType;
import factory.SplitStrategyFactory;
import models.Expense;
import models.Group;
import models.Split;
import models.User;
import strategy.SplitStrategy;

import java.util.List;
import java.util.Map;

public class ExpenseService {
    private final BalanceSheetService balanceSheetService;

    public ExpenseService(BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    public void addExpense(Group group, String description, double amount, User paidBy, List<User> participants, SplitType splitType, Map<User, Double> meta) {
        SplitStrategy splitStrategy = SplitStrategyFactory.getSplitStrategy(splitType);
        List<Split> splits = splitStrategy.split(amount, participants, meta);
        Expense expense = new Expense(description, amount, paidBy, splits, splitType);
        group.addExpense(expense);
        balanceSheetService.updateBalances(group, paidBy, splits);
    }
}

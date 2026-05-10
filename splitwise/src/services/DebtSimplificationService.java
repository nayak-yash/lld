package services;

import models.BalanceSheet;
import models.Group;
import models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DebtSimplificationService {
    public void simplifyDebts(Group group) {
        List<User> users = new ArrayList<>(group.getMembers());
        Map<User, BalanceSheet> balanceSheets = group.getBalanceSheets();

        Map<User, Double> netBalance = new HashMap<>();
        for (User user: users) {
            double net = 0.0;
            Map<User, Double> balances = balanceSheets.get(user).getBalances();
            for (double amount: balances.values()) {
                net += amount;
            }
            netBalance.put(user, net);
            balanceSheets.get(user).clearBalances();
        }

        PriorityQueue<User> creditors = new PriorityQueue<>((a, b) -> Double.compare(netBalance.get(b), netBalance.get(a)));
        PriorityQueue<User> debtors = new PriorityQueue<>(Comparator.comparingDouble(netBalance::get));

        for (User user: users) {
            if (netBalance.get(user) > 0) {
                creditors.offer(user);
            } else if (netBalance.get(user) < 0) {
                debtors.offer(user);
            }
        }

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            User creditor = creditors.poll();
            User debtor = debtors.poll();

            double creditAmount = netBalance.get(creditor);
            double debitAmount = netBalance.get(debtor);

            double settleAmount = Math.min(creditAmount, - debitAmount);

            balanceSheets.get(creditor).addBalance(debtor, settleAmount);
            balanceSheets.get(debtor).addBalance(creditor, -settleAmount);

            netBalance.put(creditor, creditAmount - settleAmount);
            netBalance.put(debtor, debitAmount + settleAmount);

            if (netBalance.get(creditor) > 0) {
                creditors.offer(creditor);
            }
            if (netBalance.get(debtor) > 0) {
                debtors.offer(debtor);
            }
        }
    }
}

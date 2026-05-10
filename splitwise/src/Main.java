import enums.SplitType;
import models.User;
import repos.GroupRepository;
import repos.InMemoryGroupRepository;
import services.BalanceSheetService;
import services.DebtSimplificationService;
import services.ExpenseService;
import services.GroupService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*----------create users----------*/
        User alice = new User("Alice");
        User bob = new User("Bob");
        User tom = new User("Tom");
        User jake = new User("Jake");

        GroupRepository groupRepository = new InMemoryGroupRepository();
        BalanceSheetService balanceSheetService = new BalanceSheetService();
        ExpenseService expenseService = new ExpenseService(balanceSheetService);
        DebtSimplificationService debtSimplificationService = new DebtSimplificationService();

        GroupService groupService = new GroupService(groupRepository, expenseService, debtSimplificationService);

        /*----------create groups----------*/
        String goaGroupId = groupService.createGroup("Goa Trip", List.of(alice, bob, tom));
        String officeGroupId = groupService.createGroup("Office Party", List.of(alice, bob, tom, jake));

        /*----------add expense----------*/
        groupService.addExpense(goaGroupId, "Lunch Day-1", 100, alice, List.of(alice, bob), SplitType.EQUAL, null);
        groupService.addExpense(goaGroupId, "Lunch Day-2", 100, bob, List.of(bob, tom), SplitType.EQUAL, null);

        /*----------simplify debits----------*/
        groupService.simplifyDebts(goaGroupId);
        groupService.printBalances(goaGroupId);
    }
}
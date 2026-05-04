import enums.ATMStatus;
import models.ATM;
import models.Account;
import models.Card;
import repo.ATMRepository;
import service.ATMMachine;

public class Main {
    public static void main(String[] args) {

        // 1. Create an ATM with notes: 5x2000, 10x500, 20x100
        ATM atm = new ATM("ATM-001", ATMStatus.IDLE, 5, 10, 20);
        System.out.println("ATM created with cash available: " + atm.getCashAvailable());

        // 2. Save ATM to repository
        ATMRepository atmRepository = new ATMRepository();
        atmRepository.save(atm);

        // 3. Initialize the ATM Machine (state pattern entry point)
        ATMMachine atmMachine = new ATMMachine(atm);
        System.out.println("ATM State: " + atmMachine.getAtmState().getAtmStatus());

        // 4. Create account and card
        Account account = new Account("ACC-1001", 50000);
        Card card = new Card("4111-1111-1111-1111", "1234", account);

        // ---- Scenario 1: Successful withdrawal ----
        System.out.println("\n===== Scenario 1: Successful Withdrawal =====");
        atmMachine.insertCard(card);
        atmMachine.enterPin("1234");
        atmMachine.selectOption("withdraw");
        atmMachine.dispenseCash(3600);
        System.out.println("Account balance after withdrawal: " + account.getBalance());
        System.out.println("ATM cash remaining: " + atm.getCashAvailable());

        // ---- Scenario 2: Wrong PIN ----
        System.out.println("\n===== Scenario 2: Wrong PIN =====");
        atmMachine.insertCard(card);
        atmMachine.enterPin("0000");       // wrong pin
        atmMachine.enterPin("1234");       // correct pin
        atmMachine.selectOption("balance");
        atmMachine.ejectCard();

        // ---- Scenario 3: Insufficient account balance ----
        System.out.println("\n===== Scenario 3: Insufficient Account Balance =====");
        atmMachine.insertCard(card);
        atmMachine.enterPin("1234");
        atmMachine.selectOption("withdraw");
        atmMachine.dispenseCash(999999);

        // ---- Scenario 4: Invalid operations in wrong state ----
        System.out.println("\n===== Scenario 4: Invalid State Operations =====");
        atmMachine.enterPin("1234");       // no card inserted
        atmMachine.dispenseCash(1000);     // no card inserted
        atmMachine.ejectCard();            // no card to eject
    }
}

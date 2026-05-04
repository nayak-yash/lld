package state;

import enums.ATMStatus;
import models.Card;
import service.ATMMachine;

public class AuthenticatedState implements ATMState {
    @Override
    public void insertCard(Card card, ATMMachine atmMachine) {
        System.out.println("Card already inserted and authenticated");
    }

    @Override
    public void enterPin(String pin, ATMMachine atmMachine) {
        System.out.println("Already authenticated");
    }

    @Override
    public void selectOption(String option, ATMMachine atmMachine) {
        if ("withdraw".equalsIgnoreCase(option)) {
            System.out.println("Option selected: Withdrawal");
            atmMachine.setAtmState(new CashDispenseState());
        } else if ("balance".equalsIgnoreCase(option)) {
            int balance = atmMachine.getCurrentCard().getAccount().getBalance();
            System.out.println("Your account balance is: " + balance);
        } else {
            System.out.println("Invalid option. Available options: withdraw, balance");
        }
    }

    @Override
    public void dispenseCash(int amount, ATMMachine atmMachine) {
        System.out.println("Please select an option first");
    }

    @Override
    public void ejectCard(ATMMachine atmMachine) {
        atmMachine.setCurrentCard(null);
        System.out.println("Card Ejected");
        atmMachine.setAtmState(new IdleState());
    }

    @Override
    public ATMStatus getAtmStatus() {
        return ATMStatus.AUTHENTICATED;
    }
}

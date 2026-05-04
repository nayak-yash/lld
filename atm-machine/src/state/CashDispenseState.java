package state;

import cor.CashDispenser;
import cor.CashDispenserChainBuilder;
import enums.ATMStatus;
import models.Card;
import service.ATMMachine;

public class CashDispenseState implements ATMState {
    private final CashDispenser cashDispenser = CashDispenserChainBuilder.buildChain();

    @Override
    public void insertCard(Card card, ATMMachine atmMachine) {
        System.out.println("Transaction in progress. Please wait");
    }

    @Override
    public void enterPin(String pin, ATMMachine atmMachine) {
        System.out.println("Transaction in progress. Please wait");
    }

    @Override
    public void selectOption(String option, ATMMachine atmMachine) {
        System.out.println("Transaction in progress. Please wait");
    }

    @Override
    public void dispenseCash(int amount, ATMMachine atmMachine) {
        int atmBalance = atmMachine.getAtm().getCashAvailable();
        int accountBalance = atmMachine.getCurrentCard().getAccount().getBalance();

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value");
            ejectCard(atmMachine);
            return;
        }
        if (amount % 100 != 0) {
            System.out.println("Amount must be a multiple of 100");
            ejectCard(atmMachine);
            return;
        }
        if (amount > atmBalance) {
            System.out.println("Insufficient cash in ATM. Please try a smaller amount");
            ejectCard(atmMachine);
            return;
        }
        if (amount > accountBalance) {
            System.out.println("Insufficient account balance");
            ejectCard(atmMachine);
            return;
        }

        if (cashDispenser.canDispense(atmMachine.getAtm(), amount)) {
            cashDispenser.dispense(atmMachine.getAtm(), amount);

            atmMachine.getAtm().setCashAvailable(atmBalance - amount);
            atmMachine.getCurrentCard().getAccount().setBalance(accountBalance - amount);

            System.out.println("Cash dispensed: " + amount);
            ejectCard(atmMachine);
        } else {
            System.out.println("Cannot dispense requested amount with available denominations");
            ejectCard(atmMachine);
        }
    }

    @Override
    public void ejectCard(ATMMachine atmMachine) {
        atmMachine.setCurrentCard(null);
        System.out.println("Card Ejected");
        atmMachine.setAtmState(new IdleState());
    }

    @Override
    public ATMStatus getAtmStatus() {
        return ATMStatus.DISPENSE_CASH;
    }
}

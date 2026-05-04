package state;

import enums.ATMStatus;
import models.Card;
import service.ATMMachine;

public class CardInsertedState implements ATMState {
    @Override
    public void insertCard(Card card, ATMMachine atmMachine) {
        System.out.println("Card already inserted");
    }

    @Override
    public void enterPin(String pin, ATMMachine atmMachine) {
        if (atmMachine.getCurrentCard().getPin().equals(pin)) {
            System.out.println("Pin correct. Authenticated");
            atmMachine.setAtmState(new AuthenticatedState());
        } else {
            System.out.println("Incorrect pin. Try again");
        }
    }

    @Override
    public void selectOption(String option, ATMMachine atmMachine) {
        System.out.println("Please enter your PIN first");
    }

    @Override
    public void dispenseCash(int amount, ATMMachine atmMachine) {
        System.out.println("Please enter your PIN first");
    }

    @Override
    public void ejectCard(ATMMachine atmMachine) {
        atmMachine.setCurrentCard(null);
        System.out.println("Card Ejected");
        atmMachine.setAtmState(new IdleState());
    }

    @Override
    public ATMStatus getAtmStatus() {
        return ATMStatus.CARD_INSERTED;
    }
}

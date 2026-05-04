package state;

import enums.ATMStatus;
import models.Card;
import service.ATMMachine;

public class IdleState implements ATMState {
    @Override
    public void insertCard(Card card, ATMMachine atmMachine) {
        atmMachine.setCurrentCard(card);
        System.out.println("Card inserted");
        atmMachine.setAtmState(new CardInsertedState());
    }

    @Override
    public void enterPin(String pin, ATMMachine atmMachine) {
        System.out.println("Please insert a card first");
    }

    @Override
    public void selectOption(String option, ATMMachine atmMachine) {
        System.out.println("Please insert a card first");
    }

    @Override
    public void dispenseCash(int amount, ATMMachine atmMachine) {
        System.out.println("Please insert a card first");
    }

    @Override
    public void ejectCard(ATMMachine atmMachine) {
        System.out.println("No card to eject");
    }

    @Override
    public ATMStatus getAtmStatus() {
        return ATMStatus.IDLE;
    }
}

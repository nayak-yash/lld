package service;

import models.ATM;
import models.Card;
import state.ATMState;
import state.IdleState;

public class ATMMachine {

    private final ATM atm;
    private ATMState atmState;
    private Card currentCard;

    public ATMMachine(ATM atm) {
        this.atm = atm;
        this.atmState = new IdleState();
    }

    public ATM getAtm() {
        return atm;
    }

    public ATMState getAtmState() {
        return atmState;
    }

    public void setAtmState(ATMState atmState) {
        this.atmState = atmState;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void insertCard(Card card) {
        atmState.insertCard(card, this);
    }

    public void enterPin(String pin) {
        atmState.enterPin(pin, this);
    }

    public void selectOption(String option) {
        atmState.selectOption(option, this);
    }

    public void dispenseCash(int amount) {
        atmState.dispenseCash(amount, this);
    }

    public void ejectCard() {
        atmState.ejectCard(this);
    }
}

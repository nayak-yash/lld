package state;

import enums.ATMStatus;
import models.Card;
import service.ATMMachine;

public interface ATMState {
    void insertCard(Card card, ATMMachine atmMachine);
    void enterPin(String pin, ATMMachine atmMachine);
    void selectOption(String option, ATMMachine atmMachine);
    void dispenseCash(int amount, ATMMachine atmMachine);
    void ejectCard(ATMMachine atmMachine);
    ATMStatus getAtmStatus();
}

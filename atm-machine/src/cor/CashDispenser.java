package cor;

import models.ATM;

public interface CashDispenser {
    void setNextDispenser(CashDispenser dispenser);
    boolean canDispense(ATM atm, int amount);
    void dispense(ATM atm, int amount);
}
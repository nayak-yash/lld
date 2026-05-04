package cor;

import models.ATM;

public class OneHundredDispenser implements CashDispenser {
    private CashDispenser next;

    @Override
    public void setNextDispenser(CashDispenser dispenser) {
        this.next = dispenser;
    }

    @Override
    public boolean canDispense(ATM atm, int amount) {
        int count = atm.getOneHundredNotes();
        int notes = Math.min(count, amount / 100);
        int remainder = amount -  notes * 100;
        return remainder == 0;
    }

    @Override
    public void dispense(ATM atm, int amount) {
        int count = atm.getOneHundredNotes();
        int notes = Math.min(count, amount / 100);
        atm.setOneHundredNotes(count - notes);
        int remainder = amount -  notes * 100;
        if (notes > 0) {
            System.out.println("Dispensed " + notes + " x one hundred notes");
        }
        if (remainder > 0) {
            throw new IllegalStateException("Cannot dispense the remaining amount: " + remainder);
        }
    }
}

package cor;

import models.ATM;

public class FiveHundredDispenser implements CashDispenser {
    private CashDispenser next;

    @Override
    public void setNextDispenser(CashDispenser dispenser) {
        this.next = dispenser;
    }

    @Override
    public boolean canDispense(ATM atm, int amount) {
        int count = atm.getFiveHundredNotes();
        int notes = Math.min(count, amount / 500);
        int remainder = amount -  notes * 500;
        return remainder == 0 || (next != null && next.canDispense(atm, remainder));
    }

    @Override
    public void dispense(ATM atm, int amount) {
        int count = atm.getFiveHundredNotes();
        int notes = Math.min(count, amount / 500);
        atm.setFiveHundredNotes(count - notes);
        int remainder = amount -  notes * 500;
        if (notes > 0) {
            System.out.println("Dispensed " + notes + " x five hundred notes");
        }
        if (remainder > 0 && next != null) {
            next.dispense(atm, remainder);
        }
    }
}

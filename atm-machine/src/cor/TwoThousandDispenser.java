package cor;

import models.ATM;

public class TwoThousandDispenser implements CashDispenser {
    private CashDispenser next;

    @Override
    public void setNextDispenser(CashDispenser dispenser) {
        this.next = dispenser;
    }

    @Override
    public boolean canDispense(ATM atm, int amount) {
        int count = atm.getTwoThousandNotes();
        int notes = Math.min(count, amount / 2000);
        int remainder = amount -  notes * 2000;
        return remainder == 0 || (next != null && next.canDispense(atm, remainder));
    }

    @Override
    public void dispense(ATM atm, int amount) {
        int count = atm.getTwoThousandNotes();
        int notes = Math.min(count, amount / 2000);
        atm.setTwoThousandNotes(count - notes);
        int remainder = amount -  notes * 2000;
        if (notes > 0) {
            System.out.println("Dispensed " + notes + " x two thousand notes");
        }
        if (remainder > 0 && next != null) {
            next.dispense(atm, remainder);
        }
    }
}

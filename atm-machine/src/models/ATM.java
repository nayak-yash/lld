package models;

import enums.ATMStatus;

public class ATM {

    private final String id;
    private final ATMStatus atmStatus;
    private int cashAvailable;
    private int twoThousandNotes;
    private int fiveHundredNotes;
    private int oneHundredNotes;

    public ATM(String id, ATMStatus atmStatus, int twoThousandNotes, int fiveHundredNotes, int oneHundredNotes) {
        this.id = id;
        this.atmStatus = atmStatus;
        this.twoThousandNotes = twoThousandNotes;
        this.fiveHundredNotes = fiveHundredNotes;
        this.oneHundredNotes = oneHundredNotes;
        this.cashAvailable = twoThousandNotes * 2000 + fiveHundredNotes * 500 + oneHundredNotes * 100;
    }

    public String getId() {
        return id;
    }

    public ATMStatus getAtmStatus() {
        return atmStatus;
    }

    public int getCashAvailable() {
        return cashAvailable;
    }

    public void setCashAvailable(int cashAvailable) {
        this.cashAvailable = cashAvailable;
    }

    public int getTwoThousandNotes() {
        return twoThousandNotes;
    }

    public void setTwoThousandNotes(int twoThousandNotes) {
        this.twoThousandNotes = twoThousandNotes;
    }

    public int getFiveHundredNotes() {
        return fiveHundredNotes;
    }

    public void setFiveHundredNotes(int fiveHundredNotes) {
        this.fiveHundredNotes = fiveHundredNotes;
    }

    public int getOneHundredNotes() {
        return oneHundredNotes;
    }

    public void setOneHundredNotes(int oneHundredNotes) {
        this.oneHundredNotes = oneHundredNotes;
    }
}

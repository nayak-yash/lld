package cor;

public class CashDispenserChainBuilder {
    public static CashDispenser buildChain() {
        TwoThousandDispenser twoThousand = new TwoThousandDispenser();
        FiveHundredDispenser fiveHundred = new FiveHundredDispenser();
        OneHundredDispenser oneHundred = new OneHundredDispenser();

        twoThousand.setNextDispenser(fiveHundred);
        fiveHundred.setNextDispenser(oneHundred);

        return twoThousand;
    }
}

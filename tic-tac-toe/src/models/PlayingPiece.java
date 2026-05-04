package models;

public abstract class PlayingPiece {
    private final char symbol;

    protected PlayingPiece(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

package strategy;

import models.Board;

public interface WinningStrategy {
    boolean checkWinner(Board board, int lastRow, int lastCol, char symbol);
}

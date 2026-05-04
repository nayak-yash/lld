package strategy;

import models.Board;

public class StandardWinningStrategy implements WinningStrategy {

    @Override
    public boolean checkWinner(Board board, int row, int col, char symbol) {
        int size = board.getSize();
        boolean rowMatch = true, colMatch = true, diagMatch = true, antiDiagMatch = true;

        for (int i = 0; i < size; i++) {
            // Check Row
            if (board.getGrid()[row][i] == null || board.getGrid()[row][i].getSymbol() != symbol) rowMatch = false;
            // Check Column
            if (board.getGrid()[i][col] == null || board.getGrid()[i][col].getSymbol() != symbol) colMatch = false;
            // Check Diagonal
            if (board.getGrid()[i][i] == null || board.getGrid()[i][i].getSymbol() != symbol) diagMatch = false;
            // Check Anti-Diagonal
            if (board.getGrid()[i][size - 1 - i] == null || board.getGrid()[i][size - 1 - i].getSymbol() != symbol) antiDiagMatch = false;
        }
        return rowMatch || colMatch || diagMatch || antiDiagMatch;
    }
}

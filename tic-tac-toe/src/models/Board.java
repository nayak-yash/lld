package models;

public class Board {
    private final int size;
    private final PlayingPiece[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new PlayingPiece[size][size];
    }

    public boolean addPiece(int row, int col, PlayingPiece piece) {
        if (row < 0 || row >= size || col < 0 || col >= size || grid[row][col] != null) {
            return false;
        }
        grid[row][col] = piece;
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j] != null ? " " + grid[i][j].getSymbol() + " " : " - ");
                if (j < size - 1) System.out.print("|");
            }
            System.out.println();
            if (i < size - 1) {
                System.out.println("-".repeat(size * 4 - 1));
            }
        }
    }

    public int getSize() { return size; }

    public PlayingPiece[][] getGrid() { return grid; }
}

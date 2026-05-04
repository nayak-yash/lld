import models.Board;
import models.Player;
import strategy.StandardWinningStrategy;
import strategy.WinningStrategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class TicTacToeGame {
    private final Deque<Player> players;
    private final Board board;
    private final WinningStrategy winningStrategy;
    private int totalMoves;

    public TicTacToeGame(int boardSize, List<Player> playerList) {
        this.board = new Board(boardSize);
        this.players = new ArrayDeque<>(playerList);
        this.winningStrategy = new StandardWinningStrategy();
        this.totalMoves = 0;
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean isGameOver = false;

        while (!isGameOver) {
            Player currentPlayer = players.pollFirst();
            board.printBoard();
            System.out.println(currentPlayer.getName() + "'s turn. Enter row and column (e.g., 0 1): ");

            int row = scanner.nextInt();
            int col = scanner.nextInt();

            boolean success = board.addPiece(row, col, currentPlayer.getPiece());
            if (!success) {
                System.out.println("Invalid move! Try again.");
                players.addFirst(currentPlayer);
                continue;
            }

            totalMoves++;
            if (winningStrategy.checkWinner(board, row, col, currentPlayer.getPiece().getSymbol())) {
                board.printBoard();
                System.out.println("🎉 Congratulations " + currentPlayer.getName() + "! You won!");
                isGameOver = true;
            } else if (totalMoves == board.getSize() * board.getSize()) {
                board.printBoard();
                System.out.println("🤝 It's a draw!");
                isGameOver = true;
            } else {
                players.addLast(currentPlayer);
            }
        }
    }
}

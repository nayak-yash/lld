import models.PieceO;
import models.PieceX;
import models.Player;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("Alice", new PieceX());
        Player p2 = new Player("Bob", new PieceO());

        // Initialize Game Engine (3x3 board)
        TicTacToeGame game = new TicTacToeGame(3, Arrays.asList(p1, p2));

        // Start the game
        game.startGame();
    }
}
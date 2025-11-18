package be.kdg.ipj3.domain;

import java.util.Arrays;

public class Game {
    private final CellValue[][] board = new CellValue[3][3];
    private Player currentPlayer;
    private GameStatus status;
    private Player winner;

    public Game() {
        for (int row = 0; row < 3; row++) {
            Arrays.fill(board[row], CellValue.EMPTY);
        }
    }

    public CellValue[][] getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }
}

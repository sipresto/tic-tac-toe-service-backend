package be.kdg.ipj3.domain;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class Game {
    private final UUID id;
    private CellValue[][] board = new CellValue[3][3];
    private final Player playerX;
    private final Player playerO;
    private Player currentPlayer;
    private GameStatus status;
    private final Difficulty difficulty;
    private Player winner;
    private Instant lastActivity;

    public Game(UUID id, CellValue[][] board, Player playerX, Player playerO, Player currentPlayer,
                GameStatus status, Difficulty difficulty, Player winner, Instant lastActivity) {
        this.id = id;
        this.board = board;
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = currentPlayer;
        this.status = status;
        this.difficulty = difficulty;
        this.winner = winner;
        this.lastActivity = lastActivity;
    }

    public Game(Difficulty difficulty, Player playerX, Player playerO) {
        this.id = UUID.randomUUID();
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = playerX;
        this.status = GameStatus.IN_PROGRESS;
        this.difficulty = difficulty;
        for (int row = 0; row < 3; row++) {
            Arrays.fill(board[row], CellValue.EMPTY);
        }
        this.lastActivity = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public CellValue[][] getBoard() {
        return board;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Player getWinner() {
        return winner;
    }

    public Instant getLastActivity() {
        return lastActivity;
    }

    private void switchCurrentPlayer() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

    public void move(UUID playerId, int row, int col) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        if (!playerId.equals(currentPlayer.id().id())) {
            throw new IllegalStateException("Not your turn");
        }

        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            throw new IllegalArgumentException("Move out of bounds");
        }

        if (board[row][col] != CellValue.EMPTY) {
            throw new IllegalArgumentException("Cell is already occupied");
        }

        board[row][col] = valueFor(currentPlayer);

        if (isWinning()) {
            status = GameStatus.FINISHED;
            winner = currentPlayer;
        } else if (isDraw()) {
            status = GameStatus.DRAW;
        } else {
            switchCurrentPlayer();
        }

        this.lastActivity = Instant.now();
    }

    private CellValue valueFor(Player player) {
        return (player == playerX) ? CellValue.X : CellValue.O;
    }

    public boolean isWinning() {
        for (int row = 0; row < 3; row++) {
            if (board[row][0] != CellValue.EMPTY &&
                    board[row][0] == board[row][1] &&
                    board[row][1] == board[row][2]) {
                return true;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col] != CellValue.EMPTY &&
                    board[0][col] == board[1][col] &&
                    board[1][col] == board[2][col]) {
                return true;
            }
        }

        if (board[0][0] != CellValue.EMPTY &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) {
            return true;
        }

        if (board[0][2] != CellValue.EMPTY &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) {
            return true;
        }

        return false;
    }

    public boolean isDraw() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .noneMatch(cell -> cell == CellValue.EMPTY);
    }

    public void resign(Player winnerByResignation) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        this.status = GameStatus.FINISHED;
        this.winner = winnerByResignation;
    }
}

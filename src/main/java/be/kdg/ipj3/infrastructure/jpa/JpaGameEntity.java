package be.kdg.ipj3.infrastructure.jpa;

import be.kdg.ipj3.domain.*;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tictactoe_games")
public class JpaGameEntity {
    @Id
    private UUID id;

    @Version
    private Long version;

    @Column(nullable = false, length = 9)
    private String board;

    @Column(nullable = false)
    private UUID playerXId;

    @Column(nullable = false)
    private boolean playerXHuman;

    @Column(nullable = false)
    private UUID playerOId;

    @Column(nullable = false)
    private boolean playerOHuman;

    @Column(nullable = false)
    private UUID currentPlayerId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    private UUID winnerId;

    @Column(nullable = false)
    private Instant lastActivity;

    public JpaGameEntity() {
    } // for JPA

    public JpaGameEntity(UUID id, String board, UUID playerXId, boolean playerXHuman, UUID playerOId, boolean playerOHuman,
                         UUID currentPlayerId, GameStatus status, Difficulty difficulty, UUID winnerId,  Instant lastActivity) {
        this.id = id;
        this.board = board;
        this.playerXId = playerXId;
        this.playerXHuman = playerXHuman;
        this.playerOId = playerOId;
        this.playerOHuman = playerOHuman;
        this.currentPlayerId = currentPlayerId;
        this.status = status;
        this.difficulty = difficulty;
        this.winnerId = winnerId;
        this.lastActivity = lastActivity;
    }

    public static JpaGameEntity fromDomain(Game game) {
        return new JpaGameEntity(
                game.getId(),
                encodeBoard(game.getBoard()),
                game.getPlayerX().id(),
                game.getPlayerX().isHuman(),
                game.getPlayerO().id(),
                game.getPlayerO().isHuman(),
                game.getCurrentPlayer().id(),
                game.getStatus(),
                game.getDifficulty(),
                game.getWinner() == null ? null : game.getWinner().id(),
                game.getLastActivity());
    }

    public Game toDomain() {
        Player playerX = new Player(this.playerXId, this.playerXHuman);
        Player playerO = new Player(this.playerOId, this.playerOHuman);

        Player currentPlayer = this.playerXId.equals(this.currentPlayerId)
                ? playerX
                : playerO;

        Player winner = this.winnerId == null
                ? null
                : (this.playerXId.equals(this.winnerId) ? playerX : playerO);

        return new Game(this.id, decodeBoard(this.board), playerX, playerO, currentPlayer,
                this.status, this.difficulty, winner,  this.lastActivity);
    }

    private static String encodeBoard(CellValue[][] board) {
        StringBuilder sb = new StringBuilder(9);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                sb.append(board[r][c].name().charAt(0));
            }
        }
        return sb.toString();
    }

    private CellValue[][] decodeBoard(String encoded) {
        CellValue[][] board = new CellValue[3][3];
        int index = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                char ch = encoded.charAt(index++);
                board[r][c] = switch (ch) {
                    case 'X' -> CellValue.X;
                    case 'O' -> CellValue.O;
                    default -> CellValue.EMPTY;
                };
            }
        }
        return board;
    }
}

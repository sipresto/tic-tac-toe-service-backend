package be.kdg.ipj3.infrastructure.messaging;

import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.Player;
import be.kdg.ipj3.infrastructure.messaging.config.RabbitMQTopology;
import be.kdg.ipj3.infrastructure.messaging.dto.AchievementCompletedDto;
import be.kdg.ipj3.infrastructure.messaging.dto.GameResultMessageDto;
import be.kdg.ipj3.infrastructure.messaging.dto.RegisterGameDto;
import be.kdg.ipj3.infrastructure.messaging.dto.TurnNotificationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MessageSendHandler {

    private final RabbitTemplate rabbitTemplate;

    public MessageSendHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRegistration() {
        this.rabbitTemplate.convertAndSend(RabbitMQTopology.GAME_EVENTS_EXCHANGE,
                "game.register.tictactoe",
                RegisterGameDto.create());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        sendRegistration();
    }

    public void sendGameResult(Game game){
        /*this.rabbitTemplate.convertAndSend(RabbitMQTopology.GAME_EVENTS_EXCHANGE,
                "game.result.tictactoe",
                GameResultMessageDto.fromDomain(game));*/
    }

    public void sendAchievementCompleted(Game game) {
        /*Player winner = game.getWinner();

        if (winner != null && winner.isHuman()) {
            String achievementCode;

            if (winner.equals(game.getPlayerX())) {
                achievementCode = "X_CELENT";
            } else {
                achievementCode = "O_MAZING";
            }

            AchievementCompletedDto dto = new AchievementCompletedDto(
                    winner.id().id(),
                    game.getId(),
                    achievementCode,
                    Instant.now()
            );

            this.rabbitTemplate.convertAndSend(
                    RabbitMQTopology.GAME_EVENTS_EXCHANGE,
                    "achievement.completed.tictactoe",
                    dto
            );
        }*/
    }

    public void sendTurnNotification(Game game) {
        /*TurnNotificationDto dto = new TurnNotificationDto(
                game.getCurrentPlayer().id().id(),
                "It's your turn in tic-tac-toe!"
        );

        this.rabbitTemplate.convertAndSend(
                RabbitMQTopology.TURN_NOTIFICATIONS_EXCHANGE,
                "turn.notification.tictactoe",
                dto
        );*/
    }
}

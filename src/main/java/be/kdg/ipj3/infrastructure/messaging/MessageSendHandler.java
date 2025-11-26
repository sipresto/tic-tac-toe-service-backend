package be.kdg.ipj3.infrastructure.messaging;

import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.infrastructure.messaging.config.RabbitMQTopology;
import be.kdg.ipj3.infrastructure.messaging.dto.GameResultMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSendHandler {

    private final RabbitTemplate rabbitTemplate;

    public MessageSendHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendGameResult(Game game){
        this.rabbitTemplate.convertAndSend(RabbitMQTopology.GAME_EVENTS_EXCHANGE,
                "game.result.tictactoe",
                GameResultMessageDto.fromDomain(game));
    }
}

package be.kdg.ipj3.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String GAME_RESULTS_QUEUE = "platform-game-results-queue";
    public static final String GAME_EVENTS_EXCHANGE = "game-events-exchange";

    public static final String TURN_NOTIFICATIONS_QUEUE = "turn-notifications-queue";
    public static final String TURN_NOTIFICATIONS_EXCHANGE = "turn-notifications-exchange";

    @Bean
    TopicExchange gameEventsExchange() {
        return new TopicExchange(GAME_EVENTS_EXCHANGE);
    }

    @Bean
    Queue gameResultsQueue() {
        return QueueBuilder.durable(GAME_RESULTS_QUEUE).build();
    }

    @Bean
    Binding binding(Queue gameResultsQueue, TopicExchange gameEventsExchange) {
        return BindingBuilder.bind(gameResultsQueue)
                .to(gameEventsExchange)
                .with("game.result.*");
    }

    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(TURN_NOTIFICATIONS_EXCHANGE);
    }

    @Bean
    Queue turnNotificationsQueue() {
        return QueueBuilder.durable(TURN_NOTIFICATIONS_QUEUE).build();
    }

    @Bean
    Binding notificationsBinding(Queue turnNotificationsQueue, TopicExchange notificationsExchange) {
        return BindingBuilder.bind(turnNotificationsQueue)
                .to(notificationsExchange)
                .with("turn.notification.*");
    }

}



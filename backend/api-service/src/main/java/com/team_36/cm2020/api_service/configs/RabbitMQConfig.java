package com.team_36.cm2020.api_service.configs;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public Queue creatorNewMeetingQueue() {
        return new Queue("CREATOR_NOTIFICATIONS_NEW_MEETING", true);
    }

    @Bean
    public Queue participantsNewMeetingQueue() {
        return new Queue("PARTICIPANTS_NOTIFICATIONS_NEW MEETING", true);
    }

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            amqpAdmin.declareQueue(creatorNewMeetingQueue());
            amqpAdmin.declareQueue(participantsNewMeetingQueue());
            System.out.println("Queues \"CREATOR_NOTIFICATIONS_NEW_MEETING\" and  \"PARTICIPANTS_NOTIFICATIONS_NEW MEETING\" initialized");
        };
    }
}

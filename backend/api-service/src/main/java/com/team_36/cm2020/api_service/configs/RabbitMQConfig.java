package com.team_36.cm2020.api_service.configs;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {

    private final AmqpAdmin amqpAdmin;

    @Bean
    public TopicExchange meetingExchange() {
        return new TopicExchange("meeting.exchange");
    }

    @Bean
    public TopicExchange voteExchange() {
        return new TopicExchange("vote.exchange");
    }

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange("auth.exchange");
    }
}

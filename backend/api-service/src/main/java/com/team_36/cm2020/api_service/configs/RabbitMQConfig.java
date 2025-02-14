package com.team_36.cm2020.api_service.configs;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {

    private final AmqpAdmin amqpAdmin;

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange meetingExchange() {
        return new DirectExchange("meeting.exchange");
    }

    @Bean
    public DirectExchange voteExchange() {
        return new DirectExchange("vote.exchange");
    }

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange("auth.exchange");
    }
}

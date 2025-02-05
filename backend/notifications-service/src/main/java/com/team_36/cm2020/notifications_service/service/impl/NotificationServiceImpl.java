package com.team_36.cm2020.notifications_service.service.impl;

import com.team_36.cm2020.notifications_service.entities.Notification;
import com.team_36.cm2020.notifications_service.repositories.NotificationRepository;
import com.team_36.cm2020.notifications_service.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void saveNotificationLog(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void sendNotification(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Sent message to " + routingKey + ": " + message);
    }
}


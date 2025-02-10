package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.rmq.NotificationMessage;
import com.team_36.cm2020.api_service.rmq.NotificationType;
import com.team_36.cm2020.api_service.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNotification(String exchange, String routingKey, NotificationMessage message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    @Override
    public void sendNotificationNewMeetingOrganizer(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.NEW_MEETING_ORGANIZER.getExchange(),
                NotificationType.NEW_MEETING_ORGANIZER.getRoutingKey(),
                notificationMessage);
    }
    @Override
    public void sendNotificationNewMeetingParticipants(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.NEW_MEETING_PARTICIPANTS.getExchange(),
                NotificationType.NEW_MEETING_PARTICIPANTS.getRoutingKey(),
                notificationMessage);
    }

    @Override
    public void sendNotificationMeetingDeleted(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.MEETING_DELETED_ORGANIZER.getExchange(),
                NotificationType.MEETING_DELETED_ORGANIZER.getRoutingKey(),
                notificationMessage);
    }

    @Override
    public void sendNotificationMeetingFinalizedOrganizer(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.MEETING_FINALIZED_ORGANIZER.getExchange(),
                NotificationType.MEETING_FINALIZED_ORGANIZER.getRoutingKey(),
                notificationMessage);
    }

    @Override
    public void sendNotificationMeetingFinalizedParticipants(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.MEETING_FINALIZED_PARTICIPANTS.getExchange(),
                NotificationType.MEETING_FINALIZED_PARTICIPANTS.getRoutingKey(),
                notificationMessage);
    }

    @Override
    public void sendNotificationVoteRegisteredOrganizer(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.VOTE_REGISTERED_ORGANIZER.getExchange(),
                NotificationType.VOTE_REGISTERED_ORGANIZER.getRoutingKey(),
                notificationMessage);
    }

    @Override
    public void sendNotificationVoteRegisteredParticipant(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.VOTE_REGISTERED_PARTICIPANT.getExchange(),
                NotificationType.VOTE_REGISTERED_PARTICIPANT.getRoutingKey(),
                notificationMessage);
    }

    @Override
    public void sendNotificationLinkRestoreOrganizer(NotificationMessage notificationMessage) {
        sendNotification(NotificationType.LINK_RESTORE_ORGANIZER.getExchange(),
                NotificationType.LINK_RESTORE_ORGANIZER.getRoutingKey(),
                notificationMessage);
    }
}

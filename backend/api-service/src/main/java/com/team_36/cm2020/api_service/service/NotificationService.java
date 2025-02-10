package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.rmq.NotificationMessage;

public interface NotificationService {

    void sendNotification(String exchange, String routingKey, NotificationMessage message);
    void sendNotificationNewMeetingOrganizer(NotificationMessage notificationMessage);
    void sendNotificationNewMeetingParticipants(NotificationMessage notificationMessage);
    void sendNotificationMeetingDeleted(NotificationMessage notificationMessage);
    void sendNotificationMeetingFinalizedOrganizer(NotificationMessage notificationMessage);
    void sendNotificationMeetingFinalizedParticipants(NotificationMessage notificationMessage);

    void sendNotificationVoteRegisteredOrganizer(NotificationMessage notificationMessage);
    void sendNotificationVoteRegisteredParticipant(NotificationMessage notificationMessage);
    void sendNotificationLinkRestoreOrganizer(NotificationMessage notificationMessage);


}

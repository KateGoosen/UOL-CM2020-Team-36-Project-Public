package com.team_36.cm2020.notifications_service.service;

import com.team_36.cm2020.notifications_service.entities.Notification;

public interface NotificationService {

    void sendEmail(String to, String subject, String text);

    void saveNotificationLog(Notification notification);

    void sendNotification(String exchange, String routingKey, String message);

}


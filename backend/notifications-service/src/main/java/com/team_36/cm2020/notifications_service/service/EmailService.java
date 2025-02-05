package com.team_36.cm2020.notifications_service.service;

public interface EmailService {

    void sendEmail(String to, String subject, String content);
}


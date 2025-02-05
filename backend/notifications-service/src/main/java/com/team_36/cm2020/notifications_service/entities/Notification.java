package com.team_36.cm2020.notifications_service.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.team_36.cm2020.notifications_service.listeners.MessageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications", schema = "audit")
public class Notification {

    public Notification(MessageDto messageDto, boolean success){
        this.meetingId = messageDto.getMeetingId();
        this.meetingTitle = messageDto.getMeetingTitle();
        this.userId = messageDto.getUserId();
        this.userEmail = messageDto.getUserEmail();
        this.dateTimeSent = LocalDateTime.now();
        this.success = success;
    }

    @Id
    @GeneratedValue
    @Column(name = "notification_id", updatable = false, nullable = false)
    private UUID notificationId;

    @Column(name = "meeting_id", updatable = false, nullable = false)
    private UUID meetingId;

    @Column(name = "meeting_title", updatable = false)
    private String meetingTitle;

    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "user_email", updatable = false, nullable = false)
    private String userEmail;

    @Column(name = "date_time_sent", nullable = false)
    private LocalDateTime dateTimeSent;

    @Column(name = "success", updatable = false, nullable = false)
    private Boolean success;

}

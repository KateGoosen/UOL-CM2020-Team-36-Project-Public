package com.team_36.cm2020.api_service.rmq;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NotificationMessage implements Serializable {
    private String userName;
    private String meetingTitle;
    private UUID meetingId;
    private UUID userId;
    private UUID organizerToken;
    private String userEmail;
    private LocalDateTime meetingDateTime;
    private Integer duration;
    private String confirmationCode;

    public NotificationMessage(User user,
            Meeting meeting,
            UserType userType,
            Optional<String> confirmationCode) {
        this.userName = user.getName();
        this.meetingTitle = meeting.getTitle();
        this.meetingId = meeting.getMeetingId();
        this.userId = user.getUserId();
        this.organizerToken = userType.equals(UserType.ORGANIZER) ? meeting.getOrganizerToken() : null;
        this.userEmail = user.getEmail();
        this.meetingDateTime = meeting.getFinalTimeSlot();
        this.duration = meeting.getDuration();
        this.confirmationCode = confirmationCode.orElse(null);
    }

    // NotificationMessage.builder()
    // .userName()
    // .userId()
    // .userEmail()
    // .meetingTitle()
    // .meetingId()
    // .meetingDateTime()
    // .duration()
    // .organizerToken()
    // .confirmationCode()
    // .build()
}

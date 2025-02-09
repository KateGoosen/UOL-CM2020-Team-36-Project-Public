package com.team_36.cm2020.notifications_service.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team_36.cm2020.notifications_service.entities.Notification;
import com.team_36.cm2020.notifications_service.listeners.dto.MessageDto;
import com.team_36.cm2020.notifications_service.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MeetingNotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "meeting_created_organizer")
    public void receiveMeetingCreatedOrganizerMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nA new meeting %s has been successfully created.\n\nPlease, save the link for further meeting management:\nhttps://example.com/%s/%s/%s",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingId(),
                messageDto.getUserId(),
                messageDto.getCreatorToken());
        sendEmailAndWriteLog(messageDto, emailText, "New Meeting Created");
    }

    @RabbitListener(queues = "meeting_created_participants")
    public void receiveMeetingCreatedParticipantsMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nA new meeting %s has been successfully created.\n\nPlease, proceed to vote for a suitable time slot:\nhttps://example.com/%s/%s/vote",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingId(),
                messageDto.getUserId());
        sendEmailAndWriteLog(messageDto, emailText, "New Meeting Created");
    }

    @RabbitListener(queues = "vote_registered_organizer")
    public void receiveVoteRegisteredOrganizerMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nA new vote for the meeting %s has been registered.\n\nYou can view the vote statistics and manage the meeting here:\nhttps://example.com/%s/%s/%s",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingId(),
                messageDto.getUserId(),
                messageDto.getCreatorToken());
        sendEmailAndWriteLog(messageDto, emailText, "Vote Registered");
    }

    @RabbitListener(queues = "vote_registered_participant")
    public void receiveVoteRegisteredParticipantMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nYour vote for the meeting %s has been successfully registered.\n\nYou can view the meeting’s details here:\nhttps://example.com/%s",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingId());
        sendEmailAndWriteLog(messageDto, emailText, "Vote Registered");
    }

    @RabbitListener(queues = "meeting_finalized_organizer")
    public void receiveMeetingFinalizedOrganizerMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nThe final time for the meeting %s has been finalized. The date and time for your meeting is set for: %s.\n\nPlease, save the link for further meeting management:\nhttps://example.com/%s/%s/%s",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingDateTime(),
                messageDto.getMeetingId(),
                messageDto.getUserId(),
                messageDto.getCreatorToken());
        sendEmailAndWriteLog(messageDto, emailText, "Meeting Time Finalized");
    }

    @RabbitListener(queues = "meeting_no_slots_organizer")
    public void receiveMeetingNoSlotsOrganizerMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nUnfortunately, no suitable time slot for the meeting %s has been found. Please, add more suitable time slots for the meeting:\nhttps://example.com/%s/%s/%s",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingId(),
                messageDto.getUserId(),
                messageDto.getCreatorToken());
        sendEmailAndWriteLog(messageDto, emailText, "No Suitable Time Slots");
    }

    @RabbitListener(queues = "meeting_no_slots_participants")
    public void receiveMeetingNoSlotsParticipantsMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nThe organizer for the meeting %s has added more time slots for voting. Please, proceed here:\nhttps://example.com/%s/%s/vote",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingId(),
                messageDto.getUserId());
        sendEmailAndWriteLog(messageDto, emailText, "No Suitable Time Slots");
    }

    @RabbitListener(queues = "meeting_finalized_participants")
    public void receiveMeetingFinalizedParticipantsMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nThe final time for the meeting %s has been finalized. The date and time for the meeting is set for: %s.\n\nYou can view the meeting’s details here:\nhttps://example.com/%s",
                messageDto.getUserName(),
                messageDto.getMeetingTitle(),
                messageDto.getMeetingDateTime(),
                messageDto.getMeetingId());
        sendEmailAndWriteLog(messageDto, emailText, "Meeting Time Finalized");
    }

    @RabbitListener(queues = "meeting_deleted_organizer")
    public void receiveMeetingDeletedOrganizerMessage(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nThe meeting %s has been successfully deleted",
                messageDto.getUserName(),
                messageDto.getMeetingTitle());
        sendEmailAndWriteLog(messageDto, emailText, "Meeting Deleted");
    }

    // Listener for "auth_signup_confirmation_code"
    @RabbitListener(queues = "auth_signup_confirmation_code")
    public void receiveAuthSignupConfirmationCode(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nEnter this code %s for signing up.",
                messageDto.getUserName(),
                messageDto.getConfirmationCode());
        sendEmailAndWriteLog(messageDto, emailText, "Sign Up Confirmation Code");
    }

    @RabbitListener(queues = "auth_signup_success")
    public void receiveAuthSignupSuccess(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nYour account has been successfully created.",
                messageDto.getUserName());
        sendEmailAndWriteLog(messageDto, emailText, "Sign Up Success");
    }

    @RabbitListener(queues = "auth_login_confirmation_code")
    public void receiveAuthLoginConfirmationCode(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nEnter this code %s for logging in.",
                messageDto.getUserName(),
                messageDto.getConfirmationCode());
        sendEmailAndWriteLog(messageDto, emailText, "Login Confirmation Code");
    }

    @RabbitListener(queues = "auth_password_reset")
    public void receiveAuthPasswordReset(String message) {
        MessageDto messageDto = getMessageDto(message);
        String emailText = String.format("Dear %s,\n\nEnter this code %s for password change.",
                messageDto.getUserName(),
                messageDto.getConfirmationCode());
        sendEmailAndWriteLog(messageDto, emailText, "Password Reset");
    }

    private void sendEmailAndWriteLog(MessageDto messageDto, String emailText, String emailSubject) {
        try {
            notificationService.sendEmail(messageDto.getUserEmail(), emailSubject, emailText);
        } catch (Exception e) {
            notificationService.saveNotificationLog(new Notification(messageDto, false));
        }
        notificationService.saveNotificationLog(new Notification(messageDto, true));
    }

    private MessageDto getMessageDto(String message) {
        JsonNode messageJson = null;
        try {
            messageJson = objectMapper.readTree(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new MessageDto(messageJson);
    }
}


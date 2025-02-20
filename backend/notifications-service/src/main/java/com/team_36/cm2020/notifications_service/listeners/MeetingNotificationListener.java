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

import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class MeetingNotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter customDateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy. HH:mm"); //TODO handle time zones!

    @RabbitListener(queues = "common_time_slots_found_organizer")
    public void receiveCommonTimeSlotsFoundOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nVoting for the meeting %s has been finished.\n\nPlease, proceed to choose a final date and time for your meeting:\nhttps://localhost:5173/%s/%s/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId(),
                message.getUserId(),
                message.getOrganizerToken());
        sendEmailAndWriteLog(message, emailText, "Voting finished");
    }

    @RabbitListener(queues = "meeting_created_organizer")
    public void receiveMeetingCreatedOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nA new meeting %s has been successfully created.\n\nPlease, save the link for further meeting management:\nhttps://localhost:5173/edit/%s/%s/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId(),
                message.getUserId(),
                message.getOrganizerToken());
        sendEmailAndWriteLog(message, emailText, "New Meeting Created");
    }

    @RabbitListener(queues = "meeting_created_participants")
    public void receiveMeetingCreatedParticipantsMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nA new meeting %s has been successfully created.\n\nPlease, proceed to vote for a suitable time slot:\nhttps://localhost:5173/%s/%s/vote",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId(),
                message.getUserId());
        sendEmailAndWriteLog(message, emailText, "New Meeting Created");
    }

    @RabbitListener(queues = "vote_registered_organizer")
    public void receiveVoteRegisteredOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nA new vote for the meeting %s has been registered.\n\nYou can view the vote statistics and manage the meeting here:\nhttps://localhost:5173/edit/%s/%s/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId(),
                message.getUserId(),
                message.getOrganizerToken());
        sendEmailAndWriteLog(message, emailText, "Vote Registered");
    }

    @RabbitListener(queues = "vote_registered_participant")
    public void receiveVoteRegisteredParticipantMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nYour vote for the meeting %s has been successfully registered.\n\nYou can view the meeting’s details here:\nhttps://localhost:5173/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId());
        sendEmailAndWriteLog(message, emailText, "Vote Registered");
    }

    @RabbitListener(queues = "meeting_finalized_organizer")
    public void receiveMeetingFinalizedOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nThe final time for the meeting %s has been finalized. The date and time for your meeting is set for: %s.\n\nPlease, save the link for further meeting management:\nhttps://localhost:5173/edit/%s/%s/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingDateTime().format(customDateTimeFormatter),
                message.getMeetingId(),
                message.getUserId(),
                message.getOrganizerToken());
        sendEmailAndWriteLog(message, emailText, "Meeting Time Finalized");
    }

    @RabbitListener(queues = "meeting_no_slots_organizer")
    public void receiveMeetingNoSlotsOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nUnfortunately, no suitable time slot for the meeting %s has been found. Please, add more suitable time slots for the meeting:\nhttps://localhost:5173/%s/%s/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId(),
                message.getUserId(),
                message.getOrganizerToken());
        sendEmailAndWriteLog(message, emailText, "No Suitable Time Slots");
    }

    @RabbitListener(queues = "meeting_no_slots_participants")
    public void receiveMeetingNoSlotsParticipantsMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nThe organizer for the meeting %s has added more time slots for voting. Please, proceed here:\nhttps://localhost:5173/%s/%s/vote",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingId(),
                message.getUserId());
        sendEmailAndWriteLog(message, emailText, "No Suitable Time Slots");
    }

    @RabbitListener(queues = "meeting_finalized_participants")
    public void receiveMeetingFinalizedParticipantsMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nThe final time for the meeting %s has been finalized. The date and time for the meeting is set for: %s.\n\nYou can view the meeting’s details here:\nhttps://localhost:5173/%s",
                message.getUserName(),
                message.getMeetingTitle(),
                message.getMeetingDateTime().format(customDateTimeFormatter),
                message.getMeetingId());
        sendEmailAndWriteLog(message, emailText, "Meeting Time Finalized");
    }

    @RabbitListener(queues = "link_restore_organizer")
    public void receiveLinkRestoreOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nYour link for further meeting management \nhttps://localhost:5173/edit/%s/%s/%s",
                message.getUserName(),
                message.getMeetingId(),
                message.getUserId(),
                message.getOrganizerToken());
        sendEmailAndWriteLog(message, emailText, "Meeting Link Restored");
    }

    @RabbitListener(queues = "meeting_deleted_organizer")
    public void receiveMeetingDeletedOrganizerMessage(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nThe meeting %s has been successfully deleted",
                message.getUserName(),
                message.getMeetingTitle());
        sendEmailAndWriteLog(message, emailText, "Meeting Deleted");
    }

    @RabbitListener(queues = "auth_signup_confirmation_code")
    public void receiveAuthSignupConfirmationCode(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nEnter this code %s for signing up.",
                message.getUserName(),
                message.getConfirmationCode());
        sendEmailAndWriteLog(message, emailText, "Sign Up Confirmation Code");
    }

    @RabbitListener(queues = "auth_signup_success")
    public void receiveAuthSignupSuccess(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nYour account has been successfully created.",
                message.getUserName());
        sendEmailAndWriteLog(message, emailText, "Sign Up Success");
    }

    @RabbitListener(queues = "auth_login_confirmation_code")
    public void receiveAuthLoginConfirmationCode(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nEnter this code %s for logging in.",
                message.getUserName(),
                message.getConfirmationCode());
        sendEmailAndWriteLog(message, emailText, "Login Confirmation Code");
    }

    @RabbitListener(queues = "auth_password_reset")
    public void receiveAuthPasswordReset(MessageDto message) {
        String emailText = String.format("Dear %s,\n\nEnter this code %s for password change.",
                message.getUserName(),
                message.getConfirmationCode());
        sendEmailAndWriteLog(message, emailText, "Password Reset");
    }

    private void sendEmailAndWriteLog(MessageDto messageDto, String emailText, String emailSubject) {
        try {
            notificationService.sendEmail(messageDto.getUserEmail(), emailSubject, emailText);
            notificationService.saveNotificationLog(new Notification(messageDto, true, null));
        } catch (Exception e) {
            notificationService.saveNotificationLog(new Notification(messageDto, false, e.getMessage()));
        }
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


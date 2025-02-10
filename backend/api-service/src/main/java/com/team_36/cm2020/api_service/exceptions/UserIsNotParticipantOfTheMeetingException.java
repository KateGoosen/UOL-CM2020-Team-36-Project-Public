package com.team_36.cm2020.api_service.exceptions;

public class UserIsNotParticipantOfTheMeetingException extends RuntimeException {
    public UserIsNotParticipantOfTheMeetingException(String message) {
        super(message);
    }
}

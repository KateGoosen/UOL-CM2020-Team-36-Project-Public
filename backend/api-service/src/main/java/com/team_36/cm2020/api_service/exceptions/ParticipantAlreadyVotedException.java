package com.team_36.cm2020.api_service.exceptions;

public class ParticipantAlreadyVotedException extends RuntimeException {
    public ParticipantAlreadyVotedException(String message) {
        super(message);
    }
}

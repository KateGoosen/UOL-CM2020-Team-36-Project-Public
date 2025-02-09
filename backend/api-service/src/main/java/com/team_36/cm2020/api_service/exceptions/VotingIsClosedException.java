package com.team_36.cm2020.api_service.exceptions;

public class VotingIsClosedException extends RuntimeException {
    public VotingIsClosedException(String message) {
        super(message);
    }
}

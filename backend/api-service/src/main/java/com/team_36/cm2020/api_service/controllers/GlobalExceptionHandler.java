package com.team_36.cm2020.api_service.controllers;

import com.team_36.cm2020.api_service.exceptions.NoMeetingFoundException;
import com.team_36.cm2020.api_service.exceptions.NoPrivilegeToAccessException;
import com.team_36.cm2020.api_service.exceptions.NoUserFoundException;
import com.team_36.cm2020.api_service.exceptions.UserIsNotParticipantOfTheMeetingException;
import com.team_36.cm2020.api_service.exceptions.VotingIsClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoMeetingFoundException.class)
    public ResponseEntity<String> handleNoMeetingFoundException(NoMeetingFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<String> handleNoUserFoundException(NoUserFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

    @ExceptionHandler(NoPrivilegeToAccessException.class)
    public ResponseEntity<String> handleNoPrivilegeToAccessException(NoPrivilegeToAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    @ExceptionHandler(VotingIsClosedException.class)
    public ResponseEntity<String> handleVotingIsClosedException(VotingIsClosedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserIsNotParticipantOfTheMeetingException.class)
    public ResponseEntity<String> handleUserIsNotParticipantOfTheMeetingException(UserIsNotParticipantOfTheMeetingException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}

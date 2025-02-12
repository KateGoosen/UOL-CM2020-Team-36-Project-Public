package com.team_36.cm2020.api_service.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoMeetingFoundException.class)
    public ResponseEntity<String> handleNoMeetingFoundException(NoMeetingFoundException ex) {
        logger.error("No meeting found: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<String> handleNoUserFoundException(NoUserFoundException ex) {
        logger.error("No user found: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }

    @ExceptionHandler(NoPrivilegeToAccessException.class)
    public ResponseEntity<String> handleNoPrivilegeToAccessException(NoPrivilegeToAccessException ex) {
        logger.error("No privilege to access resource: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(VotingIsClosedException.class)
    public ResponseEntity<String> handleVotingIsClosedException(VotingIsClosedException ex) {
        logger.error("Voting is closed: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserIsNotParticipantOfTheMeetingException.class)
    public ResponseEntity<String> handleUserIsNotParticipantOfTheMeetingException(
            UserIsNotParticipantOfTheMeetingException ex) {
        logger.error("User is not participant of meeting: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}

package com.team_36.cm2020.api_service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NoMeetingFoundException.class)
    public ResponseEntity<String> handleNoMeetingFoundException(NoMeetingFoundException ex) {
        log.error("No meeting found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ParticipantAlreadyVotedException.class)
    public ResponseEntity<String> handleParticipantAlreadyVotedException(ParticipantAlreadyVotedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<String> handleNoUserFoundException(NoUserFoundException ex) {
        log.error("No user found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }

    @ExceptionHandler(NoPrivilegeToAccessException.class)
    public ResponseEntity<String> handleNoPrivilegeToAccessException(NoPrivilegeToAccessException ex) {
        log.error("No privilege to access resource: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(VotingIsClosedException.class)
    public ResponseEntity<String> handleVotingIsClosedException(VotingIsClosedException ex) {
        log.error("Voting is closed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserIsNotParticipantOfTheMeetingException.class)
    public ResponseEntity<String> handleUserIsNotParticipantOfTheMeetingException(
            UserIsNotParticipantOfTheMeetingException ex) {
        log.error("User is not participant of meeting: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}

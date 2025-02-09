package com.team_36.cm2020.api_service.exceptions;

public class NoPrivilegeToAccessException extends RuntimeException {
    public NoPrivilegeToAccessException(String message) {
        super(message);
    }
}

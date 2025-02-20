package com.team_36.cm2020.api_service.exceptions;

public class CommonTimeSlotsNotYetCalculatedException extends RuntimeException {
    public CommonTimeSlotsNotYetCalculatedException() {
        super("Common date-time slots not yet calculated");
    }
}

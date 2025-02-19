package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.entities.Meeting;

public interface TimeSlotService {
    /**
     * Find suitable time slots for a meeting.m
     *
     * @param meeting {@link com.team_36.cm2020.api_service.entities.Meeting}
     */
    void findSuitableTimeSlots(Meeting meeting);

}

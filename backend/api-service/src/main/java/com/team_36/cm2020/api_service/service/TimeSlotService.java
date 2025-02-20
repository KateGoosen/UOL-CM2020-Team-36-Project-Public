package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.output.CommonTimeSlotsResponse;

import java.util.UUID;

public interface TimeSlotService {
    /**
     * Find suitable time slots for a meeting.
     *
     * @param meeting {@link com.team_36.cm2020.api_service.entities.Meeting}
     */
    void findSuitableTimeSlots(Meeting meeting);

    /**
     * Get common time slots for a meeting.
     *
     * @param meetingId      meeting ID
     * @param userEmail      user's e-mail
     * @param organizerToken organizer token
     * @return {@link com.team_36.cm2020.api_service.output.CommonTimeSlotsResponse}
     */
    CommonTimeSlotsResponse getCommonTimeSlots(UUID meetingId, String userEmail, UUID organizerToken);
}

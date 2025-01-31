package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface MeetingServiceInterface {
    /**
     * Creates a new meeting.
     *
     * @param meetingData The meeting details received from the API request.
     * @return organizer token.
     */
    CreateMeetingResponse createMeeting(NewMeeting meetingData);
}

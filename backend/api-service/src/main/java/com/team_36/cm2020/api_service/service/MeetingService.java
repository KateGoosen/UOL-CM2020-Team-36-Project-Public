package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.input.FinalizeMeetingInput;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.input.VoteInput;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.output.GetMeetingForOrganizerResponse;

import java.util.UUID;

public interface MeetingService {
    /**
     * Creates a new meeting.
     *
     * @param meetingData The meeting details received from the API request.
     * @return organizer token.
     */
    CreateMeetingResponse createMeeting(NewMeeting meetingData);

    /**
     * Get information about the meeting (only the organizer can access).
     *
     * @param meetingId      meeting ID
     * @param organizerToken organizer token
     * @return {@link GetMeetingForOrganizerResponse}
     */
    GetMeetingForOrganizerResponse getMeetingForOrganizer(UUID meetingId, UUID organizerToken);

    /**
     * Edit the meeting data (only the organizer can access).
     *
     * @param meetingId      meeting ID
     * @param organizerToken organizer token
     * @param meetingData    {@link NewMeeting}
     */
    void editMeeting(UUID meetingId, UUID organizerToken, NewMeeting meetingData);

    /**
     * Delete the meeting
     *
     * @param meetingId      meeting ID
     * @param organizerToken organizer token
     */
    void deleteMeeting(UUID meetingId, UUID organizerToken);

    /**
     * Finalize the meeting (set final time slot and close the voting)
     *
     * @param meetingId            meeting ID
     * @param organizerToken       organizer token
     * @param finalizeMeetingInput {@link FinalizeMeetingInput}
     */
    void finalizeMeeting(UUID meetingId, UUID organizerToken, FinalizeMeetingInput finalizeMeetingInput);

    /**
     * Vote for available time slots for a meeting
     *
     * @param meetingId meeting ID
     * @param voteInput {@link VoteInput}
     */
    void vote(UUID meetingId, VoteInput voteInput);
}

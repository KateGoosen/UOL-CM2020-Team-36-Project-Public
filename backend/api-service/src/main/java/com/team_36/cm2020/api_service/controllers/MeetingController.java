package com.team_36.cm2020.api_service.controllers;

import com.team_36.cm2020.api_service.input.FinalizeMeetingInput;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.input.VoteInput;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.output.GetMeetingForOrganizerResponse;
import com.team_36.cm2020.api_service.output.MeetingDataForParticipantResponse;
import com.team_36.cm2020.api_service.service.MeetingService;
import com.team_36.cm2020.api_service.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;


    @PostMapping("/new")
    public ResponseEntity<CreateMeetingResponse> createMeeting(@RequestBody NewMeeting meetingData) {
        CreateMeetingResponse response = meetingService.createMeeting(meetingData);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{meeting_id}/{organizer_token}")
    public ResponseEntity<GetMeetingForOrganizerResponse> getMeetingForOrganizer(@PathVariable(name = "meeting_id") UUID meetingId,
                                                                                 @PathVariable(name = "organizer_token") UUID organizerToken) {
        GetMeetingForOrganizerResponse response = meetingService.getMeetingForOrganizer(meetingId, organizerToken);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{meeting_id}/{organizer_token}")
    public ResponseEntity<Void> editMeeting(@PathVariable(name = "meeting_id") UUID meetingId,
                                            @PathVariable(name = "organizer_token") UUID organizerToken,
                                            @RequestBody NewMeeting meetingData) {
        meetingService.editMeeting(meetingId, organizerToken, meetingData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{meeting_id}/{organizer_token}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable(name = "meeting_id") UUID meetingId,
                                              @PathVariable(name = "organizer_token") UUID organizerToken) {
        meetingService.deleteMeeting(meetingId, organizerToken);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{meeting_id}/{organizer_token}/finalize")
    public ResponseEntity<Void> finalizeMeeting(@PathVariable(name = "meeting_id") UUID meetingId,
                                                @PathVariable(name = "organizer_token") UUID organizerToken,
                                                @RequestBody FinalizeMeetingInput finalizeMeetingInput) {
        meetingService.finalizeMeeting(meetingId, organizerToken, finalizeMeetingInput);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{meeting_id}/vote")
    public ResponseEntity<Void> vote(@PathVariable(name = "meeting_id") UUID meetingId,
                                     @RequestBody VoteInput voteInput){
        meetingService.vote(meetingId, voteInput);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{user_email}")
    public ResponseEntity<Set<MeetingDataForParticipantResponse>> getMeetingsByEmail(@PathVariable(name = "user_email") String userEmail){
        Set<MeetingDataForParticipantResponse> response = meetingService.getMeetingsByEmail(userEmail);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/restore_edit_link/{meeting_id}")
    public ResponseEntity<Void> restoreEditLink(@PathVariable(name = "meeting_id") UUID meetingId){
        meetingService.restoreEditLink(meetingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/participant/{meeting_id}/{user_email}")
    public ResponseEntity<MeetingDataForParticipantResponse> viewMeetingDetailsByParticipant(@PathVariable(name = "meeting_id") UUID meetingId,
                                                                @PathVariable(name = "user_email") String userEmail){
        meetingService.viewMeetingDetailsByParticipant(meetingId, userEmail);
        return ResponseEntity.ok().build();
    }
}

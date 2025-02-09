package com.team_36.cm2020.api_service.controllers;

import com.team_36.cm2020.api_service.input.FinalizeMeetingInput;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.input.VoteInput;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.output.GetMeetingForOrganizerResponse;
import com.team_36.cm2020.api_service.service.MeetingService;
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

}

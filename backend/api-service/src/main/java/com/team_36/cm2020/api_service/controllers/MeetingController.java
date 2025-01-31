package com.team_36.cm2020.api_service.controllers;

import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.service.MeetingService;
import com.team_36.cm2020.api_service.service.MeetingServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    @Autowired
    MeetingServiceInterface meetingService;


    @PostMapping("/new")
    public ResponseEntity<CreateMeetingResponse> createMeeting(@RequestBody NewMeeting meetingData) {

        CreateMeetingResponse response = meetingService.createMeeting(meetingData);

        return ResponseEntity.ok().body(response);
    }
}

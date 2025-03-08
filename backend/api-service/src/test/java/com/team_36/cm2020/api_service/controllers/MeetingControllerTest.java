package com.team_36.cm2020.api_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team_36.cm2020.api_service.enums.Priority;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.input.VoteInput;
import com.team_36.cm2020.api_service.output.CommonTimeSlotsResponse;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.output.GetMeetingForOrganizerResponse;
import com.team_36.cm2020.api_service.output.MeetingDataForOrganizerResponse;
import com.team_36.cm2020.api_service.output.MeetingDataForParticipantResponse;
import com.team_36.cm2020.api_service.service.MeetingService;
import com.team_36.cm2020.api_service.service.TimeSlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MeetingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MeetingService meetingService;

    @Mock
    private TimeSlotService timeSlotService;

    @InjectMocks
    private MeetingController meetingController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(meetingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Test: Create Meeting Successfully")
    void testCreateMeeting_Success() throws Exception {
        NewMeeting meetingData = new NewMeeting();
        meetingData.setTitle("Project Kickoff");
        meetingData.setDescription("Initial project discussion");
        meetingData.setDuration(30);

        NewMeeting.TimeSlot timeSlot = new NewMeeting.TimeSlot();
        timeSlot.setDateTimeStart(LocalDateTime.now().plusDays(1));
        timeSlot.setPriority(Priority.HIGH);

        meetingData.setTimeSlots(List.of(timeSlot));

        NewMeeting.Participant participant = new NewMeeting.Participant("Alice Doe", "alice@example.com");
        meetingData.setParticipants(List.of(participant));

        NewMeeting.Organizer organizer = new NewMeeting.Organizer("John Smith", "john@example.com");
        meetingData.setOrganizer(organizer);

        CreateMeetingResponse response = new CreateMeetingResponse(UUID.randomUUID(), UUID.randomUUID());

        when(meetingService.createMeeting(any(NewMeeting.class))).thenReturn(response);

        mockMvc.perform(post("/api/meeting/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetingData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingId").exists())
                .andExpect(jsonPath("$.organizerToken").exists());
    }


    @Test
    @DisplayName("Test: Create Meeting - 400 Bad Request (Missing Required Fields)")
    void testCreateMeeting_BadRequest() throws Exception {
        // Missing required fields in NewMeeting
        String invalidJsonRequest = """
                    {
                        "title": "",
                        "duration": null,
                        "participants": [],
                        "organizer": null
                    }
                """;

        mockMvc.perform(post("/api/meeting/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Get Meeting for Organizer Successfully")
    void testGetMeetingForOrganizer_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();
        GetMeetingForOrganizerResponse response = new GetMeetingForOrganizerResponse();

        when(meetingService.getMeetingForOrganizer(meetingId, organizerToken)).thenReturn(response);

        mockMvc.perform(get("/api/meeting/{meeting_id}/{organizer_token}", meetingId, organizerToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Edit Meeting Successfully")
    void testEditMeeting_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        String jsonRequest = """
                    {
                        "title": "Project Kickoff",
                        "description": "Initial meeting for project discussion",
                        "timeSlots": [
                            {
                                "dateTimeStart": "2025-03-10T10:00:00",
                                "priority": "HIGH"
                            }
                        ],
                        "duration": 30,
                        "participants": [
                            {
                                "name": "John Doe",
                                "email": "john.doe@example.com"
                            }
                        ],
                        "organizer": {
                            "name": "Jane Smith",
                            "email": "jane.smith@example.com"
                        },
                        "votingDeadline": "2025-03-12T12:00:00"
                    }
                """;

        mockMvc.perform(put("/api/meeting/{meeting_id}/{organizer_token}", meetingId, organizerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Edit Meeting - 400 Bad Request (Invalid JSON)")
    void testEditMeeting_BadRequest() throws Exception {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        // Invalid JSON format (missing required fields)
        String invalidJsonRequest = """
        {
            "title": ""
        }
    """;

        mockMvc.perform(put("/api/meeting/{meeting_id}/{organizer_token}", meetingId, organizerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Delete Meeting Successfully")
    void testDeleteMeeting_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        mockMvc.perform(delete("/api/meeting/{meeting_id}/{organizer_token}", meetingId, organizerToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Finalize Meeting Successfully")
    void testFinalizeMeeting_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();
        LocalDateTime finalTimeSlot = LocalDateTime.now();

        String jsonRequest = String.format("{\"finalTimeSlot\": \"%s\"}", finalTimeSlot);

        mockMvc.perform(put("/api/meeting/{meeting_id}/{organizer_token}/finalize", meetingId, organizerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Finalize Meeting - 400 Bad Request (Missing Time Slot)")
    void testFinalizeMeeting_BadRequest() throws Exception {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        // Missing "finalTimeSlot"
        String invalidJsonRequest = "{}";

        mockMvc.perform(put("/api/meeting/{meeting_id}/{organizer_token}/finalize", meetingId, organizerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Vote for Time Slots Successfully")
    void testVote_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();

        VoteInput voteInput = new VoteInput();
        voteInput.setUserEmail("test@example.com");
        voteInput.setHighPriorityTimeSlots(List.of(LocalDateTime.now().plusDays(1)));
        voteInput.setLowPriorityTimeSlots(List.of(LocalDateTime.now().plusDays(2)));

        mockMvc.perform(post("/api/meeting/{meeting_id}/vote", meetingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteInput)))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test: Vote for Time Slots - 400 Bad Request (Missing Votes)")
    void testVote_BadRequest() throws Exception {
        UUID meetingId = UUID.randomUUID();

        // Empty vote input
        String invalidJsonRequest = "{}";

        mockMvc.perform(post("/api/meeting/{meeting_id}/vote", meetingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Get Meetings by Email Successfully")
    void testGetMeetingsByEmail_Success() throws Exception {
        String userEmail = "test@example.com";
        Set<MeetingDataForParticipantResponse> response = new HashSet<>();

        when(meetingService.getMeetingsByEmail(userEmail)).thenReturn(response);

        mockMvc.perform(get("/api/meeting/{user_email}", userEmail))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Get Meetings by Email - 400 Bad Request (Invalid Email)")
    void testGetMeetingsByEmail_BadRequest() throws Exception {
        // Invalid email format
        String invalidEmail = "invalid-email";

        mockMvc.perform(get("/api/meeting/{user_email}", invalidEmail))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Get Organized Meetings by Email Successfully")
    void testGetOrganizedMeetingsByEmail_Success() throws Exception {
        String userEmail = "organizer@example.com";
        List<MeetingDataForOrganizerResponse> response = new ArrayList<>();

        when(meetingService.getOrganizedMeetingsByEmail(userEmail)).thenReturn(response);

        mockMvc.perform(get("/api/meeting/organizer/{user_email}", userEmail))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Restore Edit Link Successfully")
    void testRestoreEditLink_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();

        mockMvc.perform(get("/api/meeting/restore_edit_link/{meeting_id}", meetingId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: View Meeting Details for Participant Successfully")
    void testViewMeetingDetailsByParticipant_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();
        String userEmail = "participant@example.com";
        MeetingDataForParticipantResponse response = new MeetingDataForParticipantResponse();

        when(meetingService.viewMeetingDetailsByParticipant(meetingId, userEmail)).thenReturn(response);

        mockMvc.perform(get("/api/meeting/participant/{meeting_id}/{user_email}", meetingId, userEmail))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Get Common Time Slots Successfully")
    void testGetCommonTimeSlots_Success() throws Exception {
        UUID meetingId = UUID.randomUUID();
        String userEmail = "participant@example.com";
        UUID organizerToken = UUID.randomUUID();
        CommonTimeSlotsResponse response = new CommonTimeSlotsResponse();

        when(timeSlotService.getCommonTimeSlots(meetingId, userEmail, organizerToken)).thenReturn(response);

        mockMvc.perform(get("/api/meeting/common_time_slots/{meeting_id}/{user_email}/{organizer_token}", meetingId, userEmail, organizerToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Health Check")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/meeting/test"))
                .andExpect(status().isOk());
    }
}
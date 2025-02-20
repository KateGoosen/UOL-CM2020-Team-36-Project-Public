package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.MeetingParticipantId;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.entities.Vote;
import com.team_36.cm2020.api_service.enums.Priority;
import com.team_36.cm2020.api_service.exceptions.UserIsNotParticipantOfTheMeetingException;
import com.team_36.cm2020.api_service.exceptions.VotingIsClosedException;
import com.team_36.cm2020.api_service.input.FinalizeMeetingInput;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.input.VoteInput;
import com.team_36.cm2020.api_service.messaging.RabbitMQProducer;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.output.GetMeetingForOrganizerResponse;
import com.team_36.cm2020.api_service.output.MeetingDataForOrganizerResponse;
import com.team_36.cm2020.api_service.output.MeetingDataForParticipantResponse;
import com.team_36.cm2020.api_service.repositories.MeetingParticipantRepository;
import com.team_36.cm2020.api_service.repositories.MeetingRepository;
import com.team_36.cm2020.api_service.repositories.UserRepository;
import com.team_36.cm2020.api_service.repositories.VoteRepository;
import com.team_36.cm2020.api_service.rmq.NotificationMessage;
import com.team_36.cm2020.api_service.service.NotificationService;
import com.team_36.cm2020.api_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingServiceImplTest {

    @Mock
    private RabbitMQProducer rabbitMQProducer;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserService userService;

    @Mock
    private MeetingParticipantRepository meetingParticipantRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    private NewMeeting meetingData;
    private User organizer;
    private User participant;
    private Meeting meeting;

    private final String participantEmail = "participant@example.com";
    private final String participantName = "Test Participant";
    private final String organizerEmail = "organizer@example.com";
    private final String organizerName = "Test Organizer";

    @BeforeEach
    void setUp() {
        organizer = new User();
        organizer.setEmail(organizerEmail);
        organizer.setName(organizerName);
        organizer.setUserId(UUID.randomUUID());

        participant = new User();
        participant.setEmail(participantEmail);
        participant.setName(participantName);
        participant.setUserId(UUID.randomUUID());

        meetingData = new NewMeeting();
        meetingData.setOrganizer(new NewMeeting.Organizer(organizer.getEmail(), organizer.getName()));
        meetingData.setParticipants(List.of(new NewMeeting.Participant(participantName, participantEmail)));

        meeting = new Meeting();
        meeting.setMeetingId(UUID.randomUUID());
        meeting.setOrganizerToken(UUID.randomUUID());
        meeting.setParticipants(List.of(new MeetingParticipant(new MeetingParticipantId(UUID.randomUUID(), UUID.randomUUID()), meeting, participant, false)));
    }

    @Test
    @Transactional
    void testCreateMeeting_Success() {
        // Mock dependencies
        when(userService.getUserOptionalByEmail(anyString())).thenReturn(Optional.of(organizer));
        when(userService.getUserOptionalByEmail(anyString())).thenReturn(Optional.of(participant));
        when(meetingRepository.save(any(Meeting.class))).thenReturn(meeting);

        // Execute
        CreateMeetingResponse response = meetingService.createMeeting(meetingData);

        // Verify interactions
        verify(meetingRepository, times(1)).save(any(Meeting.class));
        verify(notificationService, times(1)).sendNotificationNewMeetingOrganizer(any(NotificationMessage.class));
        verify(notificationService, times(1)).sendNotificationNewMeetingParticipants(any(NotificationMessage.class));

        // Assertions
        assertNotNull(response);
        assertEquals(meeting.getMeetingId(), response.getMeetingId());
        assertEquals(meeting.getOrganizerToken(), response.getOrganizerToken());
    }

    @Test
    void testCreateMeeting_UserNotFound() {
        when(userService.getUserOptionalByEmail(organizer.getEmail())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> meetingService.createMeeting(meetingData));
    }

    @Test
    @Transactional
    void testGetMeetingForOrganizer_Success() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        meeting.setMeetingId(meetingId);
        meeting.setOrganizerToken(organizerToken);
        meeting.setTitle("Test Meeting");
        meeting.setDescription("Meeting Description");
        meeting.setDateTimeCreated(LocalDateTime.now());
        meeting.setDateTimeUpdated(LocalDateTime.now());
        meeting.setOrganizer(organizer);
        meeting.setTimeSlots(new ArrayList<>());

        MeetingParticipant meetingParticipant = new MeetingParticipant(
                new MeetingParticipantId(UUID.randomUUID(), UUID.randomUUID()),
                meeting,
                participant,
                false);
        meeting.setParticipants(List.of(meetingParticipant));

        Vote vote = new Vote();
        vote.setUser(participant);
        vote.setMeeting(meeting);
        vote.setDateTimeStart(LocalDateTime.now());
        vote.setPriority(Priority.HIGH);
        List<Vote> votes = List.of(vote);
        meeting.setVotes(votes);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        GetMeetingForOrganizerResponse response = meetingService.getMeetingForOrganizer(meetingId, organizerToken);

        assertNotNull(response);
        assertEquals("Test Meeting", response.getTitle());
        assertEquals("Meeting Description", response.getDescription());
        assertEquals(1, response.getParticipants().size());
        assertTrue(response.getParticipants().get(0).isIfVoted());

    }

    @Test
    void testGetMeetingForOrganizer_NotFound() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> meetingService.getMeetingForOrganizer(meetingId, organizerToken));
    }

    @Test
    @Transactional
    void testEditMeeting_Success() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();
        meeting.setMeetingId(meetingId);
        meeting.setOrganizerToken(organizerToken);
        meeting.setParticipants(new ArrayList<>());

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(meetingRepository.save(any())).thenReturn(meeting);
        when(userService.saveUser(any())).thenReturn(participant);

        meetingService.editMeeting(meetingId, organizerToken, meetingData);

        verify(meetingRepository, times(1)).save(any(Meeting.class));
    }

    @Test
    void testEditMeeting_NotFound() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> meetingService.editMeeting(meetingId, organizerToken, meetingData));
    }

    @Test
    @Transactional
    void testDeleteMeeting_Success() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();
        meeting.setMeetingId(meetingId);
        meeting.setOrganizerToken(organizerToken);
        meeting.setTitle("Test Meeting");
        meeting.setOrganizer(organizer);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        meetingService.deleteMeeting(meetingId, organizerToken);

        verify(meetingRepository, times(1)).deleteById(meetingId);
        verify(notificationService, times(1)).sendNotificationMeetingDeleted(any(NotificationMessage.class));
    }

    @Test
    void testDeleteMeeting_NotFound() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> meetingService.deleteMeeting(meetingId, organizerToken));
    }

    @Test
    @Transactional
    void testFinalizeMeeting_Success() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();
        LocalDateTime finalTimeSlot = LocalDateTime.now();
        FinalizeMeetingInput finalizeMeetingInput = new FinalizeMeetingInput(finalTimeSlot);

        meeting.setMeetingId(meetingId);
        meeting.setOrganizerToken(organizerToken);
        meeting.setOrganizer(organizer);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        meetingService.finalizeMeeting(meetingId, organizerToken, finalizeMeetingInput);

        verify(meetingRepository, times(1)).save(meeting);
        verify(notificationService, times(1)).sendNotificationMeetingFinalizedOrganizer(any(NotificationMessage.class));
        verify(notificationService, atLeastOnce()).sendNotificationMeetingFinalizedParticipants(any(NotificationMessage.class));
    }

    @Test
    void testFinalizeMeeting_NotFound() {
        UUID meetingId = UUID.randomUUID();
        UUID organizerToken = UUID.randomUUID();
        FinalizeMeetingInput finalizeMeetingInput = new FinalizeMeetingInput(LocalDateTime.now());

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> meetingService.finalizeMeeting(meetingId, organizerToken, finalizeMeetingInput));
    }

    @Test
    @Transactional
    void testVote_Success() {
        UUID meetingId = UUID.randomUUID();
        VoteInput voteInput = new VoteInput(participantEmail, List.of(LocalDateTime.now()), List.of(LocalDateTime.now()));

        meeting.setMeetingId(meetingId);
        meeting.setIsVotingOpened(true);
        meeting.setOrganizer(organizer);
        meeting.getParticipants().forEach(participant -> participant.setIfVoted(false));

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(userService.getUserByEmail(participantEmail)).thenReturn(participant);
        when(this.meetingParticipantRepository.findAllByUserAndAndMeeting(any(), any()))
                .thenReturn(MeetingParticipant.builder().ifVoted(false).build());

        meetingService.vote(meetingId, voteInput);

        verify(voteRepository, times(1)).saveAll(anyList());
        verify(notificationService, times(1)).sendNotificationVoteRegisteredOrganizer(any(NotificationMessage.class));
        verify(notificationService, times(1)).sendNotificationVoteRegisteredParticipant(any(NotificationMessage.class));
    }

    @Test
    void testVote_MeetingNotFound() {
        UUID meetingId = UUID.randomUUID();
        VoteInput voteInput = new VoteInput(participantEmail, List.of(LocalDateTime.now()), List.of(LocalDateTime.now()));

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> meetingService.vote(meetingId, voteInput));
    }

    @Test
    void testVote_VotingClosed() {
        UUID meetingId = UUID.randomUUID();
        VoteInput voteInput = new VoteInput(participantEmail, List.of(LocalDateTime.now()), List.of(LocalDateTime.now()));

        meeting.setMeetingId(meetingId);
        meeting.setIsVotingOpened(false);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        assertThrows(VotingIsClosedException.class, () -> meetingService.vote(meetingId, voteInput));
    }

    @Test
    void testGetMeetingsByEmail_Success() {
        when(userService.getUserByEmail(participantEmail)).thenReturn(participant);
        when(meetingParticipantRepository.findAllByUser(participant)).thenReturn(Collections.emptyList());

        Set<MeetingDataForParticipantResponse> response = meetingService.getMeetingsByEmail(participantEmail);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testRestoreEditLink_Success() {
        UUID meetingId = UUID.randomUUID();
        meeting.setMeetingId(meetingId);
        meeting.setOrganizer(organizer);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        meetingService.restoreEditLink(meetingId);

        verify(notificationService, times(1)).sendNotificationLinkRestoreOrganizer(any(NotificationMessage.class));
    }

    @Test
    void testRestoreEditLink_NotFound() {
        UUID meetingId = UUID.randomUUID();
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> meetingService.restoreEditLink(meetingId));
    }

    @Test
    void testViewMeetingDetailsByParticipant_Success() {
        UUID meetingId = UUID.randomUUID();
        meeting.setMeetingId(meetingId);
        meeting.setParticipants(List.of(new MeetingParticipant(
                new MeetingParticipantId(meetingId, participant.getUserId()),
                meeting, participant, false)));

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(userService.getUserByEmail(participantEmail)).thenReturn(participant);

        MeetingDataForParticipantResponse response = meetingService.viewMeetingDetailsByParticipant(meetingId, participantEmail);

        assertNotNull(response);
        assertEquals(meeting.getTitle(), response.getTitle());
    }

    @Test
    void testViewMeetingDetailsByParticipant_NotParticipant() {
        UUID meetingId = UUID.randomUUID();
        String userEmail = "notparticipant@example.com";
        String userName = "Not Participant Name";
        User user = new User();
        user.setName(userName);
        user.setEmail(userEmail);
        meeting.setMeetingId(meetingId);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(userService.getUserByEmail(userEmail)).thenReturn(user);

        assertThrows(UserIsNotParticipantOfTheMeetingException.class, () ->
                meetingService.viewMeetingDetailsByParticipant(meetingId, userEmail));
    }

    @Test
    void testGetOrganizedMeetingsByEmail() {
        Meeting mockMeeting1 = Meeting.builder()
                .title("Title 1")
                .duration(12414)
                .build();
        Meeting mockMeeting2 = Meeting.builder()
                .title("Title 1")
                .duration(6453)
                .build();

        when(userService.getUserByEmail(organizerEmail)).thenReturn(organizer);
        when(meetingRepository.findAllByOrganizer(organizer)).thenReturn(List.of(mockMeeting1, mockMeeting2));

        List<MeetingDataForOrganizerResponse> responseList = meetingService.getOrganizedMeetingsByEmail(organizerEmail);

        assertNotNull(responseList);
        assertEquals(2, responseList.size());

        MeetingDataForOrganizerResponse response1 = responseList.get(0);
        assertEquals(mockMeeting1.getTitle(), response1.getTitle());
        assertEquals(mockMeeting1.getDuration(), response1.getDuration());

        MeetingDataForOrganizerResponse response2 = responseList.get(1);
        assertEquals(mockMeeting2.getTitle(), response2.getTitle());
        assertEquals(mockMeeting2.getDuration(), response2.getDuration());

        verify(userService).getUserByEmail(organizerEmail);
    }
}
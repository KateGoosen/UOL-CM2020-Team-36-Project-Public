package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.MeetingParticipantId;
import com.team_36.cm2020.api_service.entities.TimeSlot;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.entities.Vote;
import com.team_36.cm2020.api_service.enums.UserType;
import com.team_36.cm2020.api_service.exceptions.NoMeetingFoundException;
import com.team_36.cm2020.api_service.exceptions.NoPrivilegeToAccessException;
import com.team_36.cm2020.api_service.exceptions.NoUserFoundException;
import com.team_36.cm2020.api_service.exceptions.ParticipantAlreadyVotedException;
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
import com.team_36.cm2020.api_service.output.OrganizerResponse;
import com.team_36.cm2020.api_service.output.ParticipantResponse;
import com.team_36.cm2020.api_service.output.TimeSlotResponse;
import com.team_36.cm2020.api_service.repositories.MeetingParticipantRepository;
import com.team_36.cm2020.api_service.repositories.MeetingRepository;
import com.team_36.cm2020.api_service.repositories.TimeSlotRepository;
import com.team_36.cm2020.api_service.repositories.UserRepository;
import com.team_36.cm2020.api_service.repositories.VoteRepository;
import com.team_36.cm2020.api_service.rmq.NotificationMessage;
import com.team_36.cm2020.api_service.service.MeetingService;
import com.team_36.cm2020.api_service.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final RabbitMQProducer rabbitMQProducer;
    private final NotificationService notificationService;

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final VoteRepository voteRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public CreateMeetingResponse createMeeting(NewMeeting meetingData) {
        log.debug("Start creating new meeting");

        User organizer = checkIfExistsAndCreateUser(meetingData.getOrganizer().getEmail(),
                meetingData.getOrganizer().getName());

        List<User> participants = new ArrayList<>();
        for (NewMeeting.Participant participant : meetingData.getParticipants()) {
            User savedParticipant = checkIfExistsAndCreateUser(participant.getEmail(),
                    participant.getName());
            participants.add(savedParticipant);
        }

        log.debug("Hashed meeting participants: " + participants);

        Meeting savedMeeting = saveMeeting(organizer, meetingData, participants, Optional.empty());

        log.debug("Sending notification to meeting organizer to alert them of the new meeting.");
        log.debug("Saved meeting participants: " + savedMeeting.getParticipants());

        this.notificationService.sendNotificationNewMeetingOrganizer(
                new NotificationMessage(organizer, savedMeeting, UserType.ORGANIZER, Optional.empty()));
        for (MeetingParticipant meetingParticipant : savedMeeting.getParticipants()) {
            this.notificationService.sendNotificationNewMeetingParticipants(
                    new NotificationMessage(meetingParticipant.getUser(), savedMeeting,
                            UserType.PARTICIPANT, Optional.empty()));
        }

        log.debug("Finish creating the new meeting.");
        UUID organizerToken = savedMeeting.getOrganizerToken();
        log.debug("Retrieved the organizer token.");
        UUID meetingId = savedMeeting.getMeetingId();
        log.debug("Retrieved the meeting id.");
        CreateMeetingResponse meetingResponse = new CreateMeetingResponse(organizerToken, meetingId);
        log.debug("Created meeting response using retrieved organizer token and meeting id.");
        return meetingResponse;
    }

    @Override
    @Transactional
    public GetMeetingForOrganizerResponse getMeetingForOrganizer(UUID meetingId, UUID organizerToken) {
        Meeting meeting = getMeetingIfExistsById(meetingId);
        checkOrganizerToken(organizerToken, meeting);
        List<Vote> votes = meeting.getVotes();

        Map<UUID, List<Vote>> voteMap = votes.stream()
                .collect(Collectors.groupingBy(vote -> vote.getUser().getUserId()));

        List<ParticipantResponse> participantResponse = meeting.getParticipants().stream().map(participant -> {
                    boolean ifVoted = voteMap.containsKey(participant.getUser().getUserId());
                    return ParticipantResponse.builder()
                            .name(participant.getUser().getName())
                            .email(participant.getUser().getEmail())
                            .ifVoted(voteMap.containsKey(participant.getUser().getUserId()))
                            .timeSlotsLowPriority(ifVoted
                                    ? voteMap.get(participant.getUser().getUserId()).stream()
                                    .filter(vote -> vote.getPriority()
                                            .equals(Vote.Priority.LOW))
                                    .map(Vote::getDateTimeStart)
                                    .collect(Collectors.toSet())
                                    : new HashSet<>())
                            .timeSlotsHighPriority(ifVoted
                                    ? voteMap.get(participant.getUser().getUserId()).stream()
                                    .filter(vote -> vote.getPriority()
                                            .equals(Vote.Priority.HIGH))
                                    .map(Vote::getDateTimeStart)
                                    .collect(Collectors.toSet())
                                    : new HashSet<>())
                            .build();
                })
                .collect(Collectors.toList());

        return GetMeetingForOrganizerResponse.builder()
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .duration(meeting.getDuration())
                .organizer(OrganizerResponse.builder()
                        .name(meeting.getOrganizer().getName())
                        .email(meeting.getOrganizer().getEmail())
                        .build())
                .participants(participantResponse)

                .timeSlots(meeting.getTimeSlots().stream()
                        .map(slot -> TimeSlotResponse.builder()
                                .dateTimeStart(slot.getDateTimeStart())
                                .priority(slot.getPriority())
                                .build())
                        .toList()
                )
                .votingDeadLine(meeting.getVotingDeadline())
                .finalDateTimeSlot(meeting.getFinalTimeSlot())
                .dateTimeCreated(meeting.getDateTimeCreated())
                .build();
    }

    @Override
    @Transactional
    public void editMeeting(UUID meetingId, UUID organizerToken, NewMeeting meetingData) {
        Meeting meeting = getMeetingIfExistsById(meetingId);
        checkOrganizerToken(organizerToken, meeting);

        List<User> participants = new ArrayList<>();
        for (NewMeeting.Participant participant : meetingData.getParticipants()) {
            User savedParticipant = checkIfExistsAndCreateUser(participant.getEmail(),
                    participant.getName());
            participants.add(savedParticipant);
        }

        List<String> existentParticipantsEmails = meeting.getParticipants().stream()
                .map(participant -> participant.getUser().getEmail())
                .toList();
        List<User> newParticipants = participants.stream()
                .filter(participant -> !existentParticipantsEmails.contains(participant.getEmail())).toList();

        this.saveMeeting(meeting.getOrganizer(), meetingData, participants, Optional.of(meeting));

        for (User newParticipant : newParticipants) {
            this.notificationService.sendNotificationNewMeetingParticipants(
                    new NotificationMessage(newParticipant, meeting,
                            UserType.PARTICIPANT, Optional.empty()));
        }
    }

    @Override
    @Transactional
    public void deleteMeeting(UUID meetingId, UUID organizerToken) {
        Meeting meeting = getMeetingIfExistsById(meetingId);
        User organizer = meeting.getOrganizer();
        checkOrganizerToken(organizerToken, meeting);
        this.meetingRepository.deleteById(meetingId);
        this.notificationService.sendNotificationMeetingDeleted(
                NotificationMessage.builder()
                        .userName(organizer.getName())
                        .userId(organizer.getUserId())
                        .userEmail(organizer.getEmail())
                        .meetingTitle(meeting.getTitle())
                        .meetingId(meeting.getMeetingId())
                        .build());
    }

    @Override
    @Transactional
    public void finalizeMeeting(UUID meetingId, UUID organizerToken, FinalizeMeetingInput finalizeMeetingInput) {
        Meeting meeting = getMeetingIfExistsById(meetingId);
        checkOrganizerToken(organizerToken, meeting);

        meeting.setFinalTimeSlot(finalizeMeetingInput.getFinalTimeSlot());
        meeting.setIsVotingOpened(false);

        this.meetingRepository.save(meeting);

        this.notificationService.sendNotificationMeetingFinalizedOrganizer(
                new NotificationMessage(meeting.getOrganizer(), meeting, UserType.ORGANIZER,
                        Optional.empty()));

        for (MeetingParticipant meetingParticipant : meeting.getParticipants()) {
            this.notificationService.sendNotificationMeetingFinalizedParticipants(
                    new NotificationMessage(meetingParticipant.getUser(), meeting,
                            UserType.PARTICIPANT, Optional.empty()));
        }
    }

    @Override
    @Transactional
    public void vote(UUID meetingId, VoteInput voteInput) {
        Meeting meeting = getMeetingIfExistsById(meetingId);

        if (!meeting.getIsVotingOpened()) {
            throw new VotingIsClosedException("Voting is closed!");
        }

        User user = getUserByEmail(voteInput.getUserEmail());
        MeetingParticipant meetingParticipant = this.meetingParticipantRepository.findAllByUserAndAndMeeting(user, meeting);

        if (meetingParticipant.isIfVoted()) {
            throw new ParticipantAlreadyVotedException(String.format("The participant with email: %s has already voted", user.getEmail()));
        }

        List<Vote> votesToSave = new ArrayList<>();
        votesToSave.addAll(createVotes(voteInput.getLowPriorityTimeSlots(),
                Vote.Priority.LOW, meeting, user));
        votesToSave.addAll(createVotes(voteInput.getHighPriorityTimeSlots(),
                Vote.Priority.HIGH, meeting, user));

        this.voteRepository.saveAll(votesToSave);

        meetingParticipant.setIfVoted(true);
        meetingParticipantRepository.save(meetingParticipant);

        this.notificationService.sendNotificationVoteRegisteredOrganizer(
                new NotificationMessage(meeting.getOrganizer(), meeting, UserType.ORGANIZER,
                        Optional.empty()));
        this.notificationService.sendNotificationVoteRegisteredParticipant(
                new NotificationMessage(user, meeting, UserType.PARTICIPANT, Optional.empty()));

    }

    @Override
    public Set<MeetingDataForParticipantResponse> getMeetingsByEmail(String userEmail) {
        User user = getUserByEmail(userEmail);

        Set<MeetingDataForParticipantResponse> response = this.meetingParticipantRepository.findAllByUser(user)
                .stream()
                .map(meetingParticipant -> {
                    Meeting meeting = meetingParticipant.getMeeting();
                    return MeetingDataForParticipantResponse.builder()
                            .title(meeting.getTitle())
                            .description(meeting.getDescription())
                            .duration(meeting.getDuration())
                            .isVotingOpened(meeting.getIsVotingOpened())
                            .finalTimeSlot(meeting.getFinalTimeSlot())
                            .build();
                })
                .collect(Collectors.toSet());

        return response;
    }

    @Override
    public List<MeetingDataForOrganizerResponse> getOrganizedMeetingsByEmail(String userEmail) {
        User user = getUserByEmail(userEmail);

        return meetingRepository.findAllByOrganizer(user).stream()
                .map(meeting -> MeetingDataForOrganizerResponse.builder()
                        .title(meeting.getTitle())
                        .description(meeting.getDescription())
                        .duration(meeting.getDuration())
                        .isVotingOpened(meeting.getIsVotingOpened())
                        .finalTimeSlot(meeting.getFinalTimeSlot())
                        .build())
                .toList();
    }

    @Override
    public void restoreEditLink(UUID meetingId) {
        Meeting meeting = getMeetingIfExistsById(meetingId);

        this.notificationService.sendNotificationLinkRestoreOrganizer(
                new NotificationMessage(meeting.getOrganizer(), meeting, UserType.ORGANIZER,
                        Optional.empty()));
    }

    @Override
    public MeetingDataForParticipantResponse viewMeetingDetailsByParticipant(UUID meetingId, String userEmail) {
        Meeting meeting = getMeetingIfExistsById(meetingId);
        User user = getUserByEmail(userEmail);

        boolean userIsParticipantOfTheMeeting = meeting.getParticipants().stream()
                .map(MeetingParticipant::getUser)
                .anyMatch(participantUser -> participantUser.equals(user));

        if (!userIsParticipantOfTheMeeting) {
            throw new UserIsNotParticipantOfTheMeetingException(String.format(
                    "The user with email: %s is not the participant of the meeting with title: %s",
                    userEmail, meeting.getTitle()));
        }

        return MeetingDataForParticipantResponse.builder()
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .duration(meeting.getDuration())
                .isVotingOpened(meeting.getIsVotingOpened())
                .finalTimeSlot(meeting.getFinalTimeSlot())
                .build();
    }

    private Set<Vote> createVotes(List<LocalDateTime> timeSlots,
                                  Vote.Priority priority,
                                  Meeting meeting,
                                  User user) {
        Set<Vote> votes = new HashSet<>();
        for (LocalDateTime timeSlot : timeSlots) {
            Vote vote = Vote.builder()
                    .meeting(meeting)
                    .priority(priority)
                    .dateTimeStart(timeSlot)
                    .user(user)
                    .build();
            votes.add(vote);
        }
        return votes;
    }

    private User getUserByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NoUserFoundException(String.format("No user found with email: %s", email));
        }
        return userOptional.get();
    }

    private Meeting getMeetingIfExistsById(UUID meetingId) {
        Optional<Meeting> meetingOptional = this.meetingRepository.findById(meetingId);
        if (meetingOptional.isEmpty()) {
            throw new NoMeetingFoundException(
                    String.format("No meeting with id: %s has been found", meetingId.toString()));
        }
        return meetingOptional.get();
    }

    private void checkOrganizerToken(UUID organizerToken, Meeting meeting) {
        boolean organizerTokenMatches = meeting.getOrganizerToken().equals(organizerToken);
        if (!organizerTokenMatches) {
            throw new NoPrivilegeToAccessException(
                    String.format("No privilege to access full meeting data id: %s ",
                            meeting.getMeetingId().toString()));
        }
    }

    private Meeting saveMeeting(User organizer,
                                NewMeeting meetingData,
                                List<User> participants,
                                Optional<Meeting> existingMeetingOptional) {
        Meeting newMeeting = Meeting.builder().build();
        if (existingMeetingOptional.isPresent()) {
            newMeeting = existingMeetingOptional.get();
        }
        newMeeting.setTitle(meetingData.getTitle());
        newMeeting.setDescription(meetingData.getDescription());
        newMeeting.setDateTimeUpdated(LocalDateTime.now());
        newMeeting.setDateTimeToDelete(LocalDateTime.now().plusMonths(3));
        newMeeting.setDuration(meetingData.getDuration());
        newMeeting.setVotingDeadline(meetingData.getVotingDeadline());

        if (existingMeetingOptional.isEmpty()) {
            newMeeting.setOrganizer(organizer);
            newMeeting.setDateTimeCreated(LocalDateTime.now());
            newMeeting.setOrganizerToken(UUID.randomUUID());
            newMeeting.setIsVotingOpened(true);
        }

        // Save participants
        List<MeetingParticipant> participantsToSave = new ArrayList<>();
        for (User participant : participants) {
            MeetingParticipant participantToSave = MeetingParticipant.builder()
                    .id(MeetingParticipantId.builder()
                            .meetingId(newMeeting.getMeetingId())
                            .userId(participant.getUserId())
                            .build())
                    .meeting(newMeeting)
                    .user(participant)
                    .build();
            participantsToSave.add(participantToSave);
        }

        if (Objects.nonNull(newMeeting.getParticipants())) {
            newMeeting.getParticipants().clear();
            newMeeting.getParticipants().addAll(participantsToSave);
        } else {
            newMeeting.setParticipants(participantsToSave);
        }


        // Save time slots
        List<TimeSlot> slotsToAdd = new ArrayList<>();
        for (NewMeeting.TimeSlot dateOption : meetingData.getTimeSlots()) {
            slotsToAdd.add(
                    com.team_36.cm2020.api_service.entities.TimeSlot.builder()
                            .dateTimeStart(dateOption.getDateTimeStart())
                            .meeting(newMeeting)
                            .priority(dateOption.getPriority())
                            .build()
            );
        }
        if (Objects.nonNull(newMeeting.getTimeSlots())) {
            newMeeting.getTimeSlots().clear();
            newMeeting.getTimeSlots().addAll(slotsToAdd);
        } else {
            newMeeting.setTimeSlots(slotsToAdd);
        }

        // Save the meeting to the database.
        Meeting savedMeeting = this.meetingRepository.save(newMeeting);
        meetingParticipantRepository.saveAll(participantsToSave);

        return savedMeeting;
    }

    private User checkIfExistsAndCreateUser(String email, String name) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            log.debug("User with email: {} does not exist, creating new user", email);
            User newCreator = User.builder()
                    .email(email)
                    .name(name)
                    .isRegistered(false)
                    .build();
            return this.userRepository.save(newCreator);
        }
        return user.get();
    }

    private User checkIfExistsAndCreateTimeSlot(String email, String name) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            log.debug("User with email: {} does not exist, creating new user", email);
            User newCreator = User.builder()
                    .email(email)
                    .name(name)
                    .isRegistered(false)
                    .build();
            return this.userRepository.save(newCreator);
        }
        return user.get();
    }
}

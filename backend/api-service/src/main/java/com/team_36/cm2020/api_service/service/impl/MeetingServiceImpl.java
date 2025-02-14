package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.MeetingParticipantId;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.entities.Vote;
import com.team_36.cm2020.api_service.enums.UserType;
import com.team_36.cm2020.api_service.exceptions.NoMeetingFoundException;
import com.team_36.cm2020.api_service.exceptions.NoPrivilegeToAccessException;
import com.team_36.cm2020.api_service.exceptions.NoUserFoundException;
import com.team_36.cm2020.api_service.exceptions.UserIsNotParticipantOfTheMeetingException;
import com.team_36.cm2020.api_service.exceptions.VotingIsClosedException;
import com.team_36.cm2020.api_service.input.FinalizeMeetingInput;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.input.VoteInput;
import com.team_36.cm2020.api_service.messaging.RabbitMQProducer;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.output.GetMeetingForOrganizerResponse;
import com.team_36.cm2020.api_service.output.MeetingDataForParticipantResponse;
import com.team_36.cm2020.api_service.output.ParticipantResponse;
import com.team_36.cm2020.api_service.repositories.MeetingParticipantRepository;
import com.team_36.cm2020.api_service.repositories.MeetingRepository;
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
                                    .map(Vote::getTimeStart)
                                    .collect(Collectors.toSet())
                                    : new HashSet<>())
                            .timeSlotsHighPriority(ifVoted
                                    ? voteMap.get(participant.getUser().getUserId()).stream()
                                    .filter(vote -> vote.getPriority()
                                            .equals(Vote.Priority.HIGH))
                                    .map(Vote::getTimeStart)
                                    .collect(Collectors.toSet())
                                    : new HashSet<>())
                            .build();
                })
                .collect(Collectors.toList());

        return GetMeetingForOrganizerResponse.builder()
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .dateTimeCreated(meeting.getDateTimeCreated())
                .dateTimeUpdated(meeting.getDateTimeUpdated())
                .duration(meeting.getDuration())
                .participants(participantResponse)
                .build();
    }

    @Override
    @Transactional
    public void editMeeting(UUID meetingId, UUID organizerToken, NewMeeting meetingData) {
        Meeting meeting = getMeetingIfExistsById(meetingId);
        checkOrganizerToken(organizerToken, meeting);

        // Compare users and remove if needed
        Set<String> existentUsersEmails = meeting.getParticipants().stream()
                .map(participant -> participant.getUser().getEmail())
                .collect(Collectors.toSet());

        // save new users to the 'users' table
        List<User> newUsersToSave = meetingData.getParticipants().stream()
                .filter(participant -> !existentUsersEmails.contains(participant.getEmail()))
                .map(participant -> User.builder()
                        .name(participant.getName())
                        .email(participant.getEmail())
                        .isRegistered(false)
                        .build())
                .collect(Collectors.toList());
        List<User> savedNewUsers = this.userRepository.saveAll(newUsersToSave);
        Set<String> saveNewUsersEmails = savedNewUsers.stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

//        Set<User> newUsersToAddToMeeting = meeting.getParticipants().stream()
//                .map(MeetingParticipant::getUser)
//                .filter(user -> saveNewUsersEmails.contains(user.getEmail()))
//                .collect(Collectors.toSet());
//        newUsersToAddToMeeting.addAll(savedNewUsers);

        this.saveMeeting(meeting.getOrganizer(), meetingData, savedNewUsers, Optional.of(meeting));
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

        // TODO What if a user chooses no time slots?

        if (!meeting.getIsVotingOpened()) {
            throw new VotingIsClosedException("Voting is closed!");
        }
        User user = getUserByEmail(voteInput.getUserEmail());

        Set<Vote> votesToSave = new HashSet<>();
        votesToSave.addAll(createVotes(voteInput.getLowPriorityTimeSlots(),
                Vote.Priority.LOW, meeting, user));
        votesToSave.addAll(createVotes(voteInput.getHighPriorityTimeSlots(),
                Vote.Priority.HIGH, meeting, user));

        this.voteRepository.saveAll(votesToSave);

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

    private Set<Vote> createVotes(Set<LocalDateTime> timeSlots,
                                  Vote.Priority priority,
                                  Meeting meeting,
                                  User user) {
        Set<Vote> votes = new HashSet<>();
        for (LocalDateTime timeSlot : timeSlots) {
            Vote vote = Vote.builder()
                    .meeting(meeting)
                    .priority(priority)
                    .timeStart(timeSlot)
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

        if (existingMeetingOptional.isEmpty()) {
            newMeeting.setOrganizer(organizer);
            newMeeting.setDateTimeCreated(LocalDateTime.now());
            newMeeting.setOrganizerToken(UUID.randomUUID());
            newMeeting.setIsVotingOpened(true);
            newMeeting.setVotingDeadline(meetingData.getVotingDeadline());
        }

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

        // Save the meeting participants.
        List<MeetingParticipant> finalParticipantsList = newMeeting.getParticipants();
        finalParticipantsList.addAll(participantsToSave);

        newMeeting.setParticipants(finalParticipantsList);

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
}

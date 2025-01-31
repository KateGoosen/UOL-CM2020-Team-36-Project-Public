package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.MeetingParticipantId;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.input.NewMeeting;
import com.team_36.cm2020.api_service.messaging.RabbitMQProducer;
import com.team_36.cm2020.api_service.output.CreateMeetingResponse;
import com.team_36.cm2020.api_service.repositories.MeetingParticipantRepository;
import com.team_36.cm2020.api_service.repositories.MeetingRepository;
import com.team_36.cm2020.api_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MeetingService implements MeetingServiceInterface {

    @Autowired
    RabbitMQProducer rabbitMQProducer;

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingParticipantRepository meetingParticipantRepository;


    @Override
    @Transactional
    public CreateMeetingResponse createMeeting(NewMeeting meetingData) {
        log.debug("Start creating new meeting");

        User organizer = checkIfExistsAndCreateUser(meetingData.getOrganizer().getEmail(), meetingData.getOrganizer().getName());

        List<User> participants = new ArrayList<>();
        for (NewMeeting.Participant participant : meetingData.getParticipants()) {
            User savedParticipant = checkIfExistsAndCreateUser(participant.getEmail(), participant.getName());
            participants.add(savedParticipant);
        }

        Meeting savedMeeting = saveMeeting(organizer, meetingData, participants);

        log.debug("Finish creating new meeting");
        return new CreateMeetingResponse(savedMeeting.getOrganizerToken(), savedMeeting.getMeetingId());
    }



    private Meeting saveMeeting(User organizer, NewMeeting meetingData, List<User> participants) {


        Meeting newMeeting = Meeting.builder()
                .organizer(organizer)
                .title(meetingData.getTitle())
                .description(meetingData.getDescription())
                .dateTimeCreated(LocalDateTime.now())
                .dateTimeUpdated(LocalDateTime.now())
                .dateTimeToDelete(LocalDateTime.now().plusMonths(3))
                .organizerToken(UUID.randomUUID())
                .build();

        Meeting savedMeeting = this.meetingRepository.save(newMeeting);

        List<MeetingParticipant> participantsToSave = new ArrayList<>();

        for(User participant : participants){
            MeetingParticipant participantToSave = MeetingParticipant.builder()
                    .id(MeetingParticipantId.builder()
                            .meetingId(savedMeeting.getMeetingId())
                            .userId(participant.getUserId())
                            .build())
                    .meeting(newMeeting)
                    .user(participant)
                    .build();
            participantsToSave.add(participantToSave);
        }
        meetingParticipantRepository.saveAll(participantsToSave);

        return this.meetingRepository.save(newMeeting);
    }

    private User checkIfExistsAndCreateUser(String email, String name) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            log.debug("User with email: {} does not exist, creating new user", email);
            User newCreator = User.builder()
                    .email(email)
                    .name(name)
                    .isRegistered(false)
                    .lastTimeActive(LocalDateTime.now())
                    .dateTimeToDelete(LocalDateTime.now().plusYears(1))
                    .build();
            return this.userRepository.save(newCreator);
        }
        return user.get();
    }
}

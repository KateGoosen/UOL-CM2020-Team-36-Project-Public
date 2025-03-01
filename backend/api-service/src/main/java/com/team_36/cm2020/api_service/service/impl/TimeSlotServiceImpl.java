package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.entities.CommonTimeSlot;
import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.TimeSlot;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.entities.Vote;
import com.team_36.cm2020.api_service.enums.Priority;
import com.team_36.cm2020.api_service.enums.UserType;
import com.team_36.cm2020.api_service.exceptions.CommonTimeSlotsNotYetCalculatedException;
import com.team_36.cm2020.api_service.exceptions.NoUserFoundException;
import com.team_36.cm2020.api_service.output.CommonTimeSlotData;
import com.team_36.cm2020.api_service.output.CommonTimeSlotsResponse;
import com.team_36.cm2020.api_service.repositories.CommonTimeSlotRepository;
import com.team_36.cm2020.api_service.repositories.VoteRepository;
import com.team_36.cm2020.api_service.rmq.NotificationMessage;
import com.team_36.cm2020.api_service.service.MeetingService;
import com.team_36.cm2020.api_service.service.NotificationService;
import com.team_36.cm2020.api_service.service.TimeSlotService;
import com.team_36.cm2020.api_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
public class TimeSlotServiceImpl implements TimeSlotService {

    private final VoteRepository voteRepository;
    private final CommonTimeSlotRepository commonTimeSlotRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final MeetingService meetingService;

    @Override
    @Transactional
    public void findSuitableTimeSlots(Meeting meeting) {
        List<List<LocalDateTime>> allTimeSlots = new ArrayList<>();
        Map<LocalDateTime, Map<Priority, Integer>> participantTimeSlotsWithPrioritiesCount = new HashMap<>();
        Map<LocalDateTime, Priority> organizerTimeSlotsWithPriorities = meeting.getTimeSlots().stream()
                .collect(Collectors.toMap(TimeSlot::getDateTimeStart, TimeSlot::getPriority));

        allTimeSlots.add(meeting.getTimeSlots().stream().map(TimeSlot::getDateTimeStart).toList());
        for (MeetingParticipant participant : meeting.getParticipants()) {
            User user = participant.getUser();
            List<Vote> votes = this.voteRepository.findAllByMeetingAndUser(meeting, user);
            allTimeSlots.add(votes.stream().map(Vote::getDateTimeStart).toList());

            for (Vote vote : votes) {
                Priority priority = vote.getPriority();
                LocalDateTime dateTimeStart = vote.getDateTimeStart();
                participantTimeSlotsWithPrioritiesCount
                        .computeIfAbsent(dateTimeStart, k -> new HashMap<>())
                        .compute(priority, (k, currentCount) -> (currentCount == null ? 1 : currentCount + 1));

            }
        }

        List<LocalDateTime> commonTimeSlots = findCommonLocalDateTimes(allTimeSlots);

        List<CommonTimeSlot> commonTimeSlotList = commonTimeSlots.stream()
                .map(slot -> CommonTimeSlot.builder()
                        .dateTimeStart(slot)
                        .meeting(meeting)
                        .organizerPriority(organizerTimeSlotsWithPriorities.get(slot))
                        .lowPriorityVotesCount(Objects.isNull(participantTimeSlotsWithPrioritiesCount.get(slot))
                                ?  0
                                : Objects.isNull(participantTimeSlotsWithPrioritiesCount.get(slot).get(Priority.LOW)) ? 0 : participantTimeSlotsWithPrioritiesCount.get(slot).get(Priority.LOW))
                        .highPriorityVotesCount(Objects.isNull(participantTimeSlotsWithPrioritiesCount.get(slot))
                                ?  0
                                : Objects.isNull(participantTimeSlotsWithPrioritiesCount.get(slot).get(Priority.HIGH)) ? 0 : participantTimeSlotsWithPrioritiesCount.get(slot).get(Priority.HIGH))
                        .build())
                .toList();
        commonTimeSlotRepository.saveAll(commonTimeSlotList);
        meeting.setCommonTimeSlotsCalculated(true);
        this.meetingService.saveMeeting(meeting);

        notificationService.sendNotificationCommonTimeSlotsFoundOrganizer(new NotificationMessage(
                meeting.getOrganizer(),
                meeting,
                UserType.ORGANIZER,
                Optional.empty()
        ));

        //TODO FINISH
//        .sorted(Comparator.comparingInt(CommonTimeSlotData::getHighPriorityVotesCount).reversed() // High priority votes DESC
//                .thenComparingInt(CommonTimeSlotData::getLowPriorityVotesCount) // Low priority votes ASC
//                .thenComparing((data) -> data.getOrganizerPriority().ordinal(), Comparator.reverseOrder()))

    }

    @Override
    @Transactional
    public CommonTimeSlotsResponse getCommonTimeSlots(UUID meetingId, String userEmail, UUID organizerToken) {


        userService.checkUserByEmail(userEmail);
        Meeting meeting = meetingService.getMeetingIfExistsById(meetingId);
        if(!meeting.isCommonTimeSlotsCalculated()){
            throw new CommonTimeSlotsNotYetCalculatedException();
        }
        meetingService.checkOrganizerToken(organizerToken, meeting);

        List<CommonTimeSlot> commonTimeSlots = this.commonTimeSlotRepository.findAllByMeeting(meeting);
        List<CommonTimeSlotData> commonTimeSlotDataList = commonTimeSlots.stream()
                .map(data -> CommonTimeSlotData.builder()
                        .dateTimeStart(data.getDateTimeStart())
                        .lowPriorityVotesCount(data.getLowPriorityVotesCount())
                        .highPriorityVotesCount(data.getHighPriorityVotesCount())
                        .organizerPriority(data.getOrganizerPriority())
                        .duration(meeting.getDuration())
                        .build())
                .sorted(Comparator.comparingInt(CommonTimeSlotData::getHighPriorityVotesCount).reversed()
                        .thenComparingInt(CommonTimeSlotData::getLowPriorityVotesCount)
                        .thenComparing((data) -> data.getOrganizerPriority().ordinal(), Comparator.reverseOrder()))
                .toList();

        return CommonTimeSlotsResponse.builder().commonTimeSlotDataList(commonTimeSlotDataList).build();
    }

//    private void checkUserByEmail(String email) {
//        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
//        if (userOptional.isEmpty()) {
//            throw new NoUserFoundException(String.format("No user found with email: %s", email));
//        }
//    }

    private static List<LocalDateTime> findCommonLocalDateTimes(List<List<LocalDateTime>> lists) {
        if (lists.isEmpty()) return Collections.emptyList();

        Set<LocalDateTime> commonSet = new HashSet<>(lists.getFirst());

        for (List<LocalDateTime> list : lists) {
            commonSet.retainAll(new HashSet<>(list));
        }

        return new ArrayList<>(commonSet);
    }
}

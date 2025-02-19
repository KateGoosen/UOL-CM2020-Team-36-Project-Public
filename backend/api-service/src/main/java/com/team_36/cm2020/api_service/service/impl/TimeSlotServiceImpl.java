package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.TimeSlot;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.entities.Vote;
import com.team_36.cm2020.api_service.enums.Priority;
import com.team_36.cm2020.api_service.output.CommonTimeSlotData;
import com.team_36.cm2020.api_service.output.CommonTimeSlotsResponse;
import com.team_36.cm2020.api_service.repositories.VoteRepository;
import com.team_36.cm2020.api_service.service.TimeSlotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {

    private final VoteRepository voteRepository;

    @Override
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

        List<CommonTimeSlotData> commonTimeSlotDataList = commonTimeSlots.stream()
                .map(slot -> CommonTimeSlotData.builder()
                        .dateTimeStart(slot)
                        .duration(meeting.getDuration())
                        .organizerPriority(organizerTimeSlotsWithPriorities.get(slot))
                        .lowPriorityVotesCount(participantTimeSlotsWithPrioritiesCount.get(slot).get(Priority.LOW))
                        .highPriorityVotesCount(participantTimeSlotsWithPrioritiesCount.get(slot).get(Priority.HIGH))
                        .build())
                .toList();
        CommonTimeSlotsResponse commonTimeSlotsResponse = CommonTimeSlotsResponse.builder()
                .commonTimeSlotDataList(commonTimeSlotDataList)
                .build();

        //TODO FINISH

    }

    private static List<LocalDateTime> findCommonLocalDateTimes(List<List<LocalDateTime>> lists) {
        if (lists.isEmpty()) return Collections.emptyList();

        Set<LocalDateTime> commonSet = new HashSet<>(lists.getFirst());

        for (List<LocalDateTime> list : lists) {
            commonSet.retainAll(new HashSet<>(list));
        }

        return new ArrayList<>(commonSet);
    }
}

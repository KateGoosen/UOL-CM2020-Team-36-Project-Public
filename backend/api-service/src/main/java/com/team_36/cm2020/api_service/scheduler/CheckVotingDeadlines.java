package com.team_36.cm2020.api_service.scheduler;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.service.MeetingService;
import com.team_36.cm2020.api_service.service.TimeSlotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CheckVotingDeadlines {

    private final TimeSlotService timeSlotService;
    private final MeetingService meetingService;

    @Scheduled(fixedRate = 60000)
    public void runEveryMinute() {
        log.debug("Check voting deadlines executed at: {}", LocalTime.now());

        List<Meeting> meetings = meetingService.findMeetingsWithExpiredVotingDeadline();
        for (Meeting meeting : meetings) {
            log.debug("start finding common time slots for meeting with id: {}", meeting.getMeetingId());
            timeSlotService.findSuitableTimeSlots(meeting);
        }
    }
}

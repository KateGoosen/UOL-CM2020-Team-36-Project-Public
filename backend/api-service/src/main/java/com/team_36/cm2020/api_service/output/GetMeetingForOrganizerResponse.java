package com.team_36.cm2020.api_service.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
public class GetMeetingForOrganizerResponse {

    private String title;
    private String description;
    private Integer duration;
    private OrganizerResponse organizer;
    private List<ParticipantResponse> participants;
    private List<TimeSlotResponse> timeSlots;
    private LocalDateTime votingDeadLine;
    private LocalDateTime finalDateTimeSlot;
    private LocalDateTime dateTimeCreated;


}


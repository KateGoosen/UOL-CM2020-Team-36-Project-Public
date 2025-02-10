package com.team_36.cm2020.api_service.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class MeetingDataForParticipantResponse {
    private String title;
    private String description;
    private Integer duration;
    private Boolean isVotingOpened;
    private LocalDateTime finalTimeSlot;
}


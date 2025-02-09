package com.team_36.cm2020.api_service.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class GetMeetingForOrganizerResponse {

    private String title;
    private String description;
    private LocalDateTime dateTimeCreated;
    private LocalDateTime dateTimeUpdated;
    private Set<ParticipantResponse> participants;
}


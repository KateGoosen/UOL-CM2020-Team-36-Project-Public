package com.team_36.cm2020.api_service.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
public class ParticipantResponse {
    private String name;
    private String email;
    private boolean ifVoted;
    private Set<LocalDateTime> timeSlotsHighPriority;
    private Set<LocalDateTime> timeSlotsLowPriority;
}


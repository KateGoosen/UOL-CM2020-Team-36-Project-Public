package com.team_36.cm2020.api_service.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class MeetingParticipantId implements Serializable {
    private UUID meetingId;
    private UUID userId;
}

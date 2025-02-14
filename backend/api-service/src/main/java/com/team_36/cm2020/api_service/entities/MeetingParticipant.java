package com.team_36.cm2020.api_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "meeting_participants", schema = "scheduler")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MeetingParticipant implements Serializable {

    @EmbeddedId
    private MeetingParticipantId id;

    @ManyToOne
    @MapsId("meetingId")
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "if_voted")
    private boolean ifVoted;
}
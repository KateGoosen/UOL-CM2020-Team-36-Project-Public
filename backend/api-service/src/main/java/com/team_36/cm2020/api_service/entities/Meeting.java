package com.team_36.cm2020.api_service.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Table(name = "meetings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Meeting {
    @Id
    @GeneratedValue
    private UUID meetingId;

    @ManyToOne
    @JoinColumn(name = "organizer", nullable = false)
    private User organizer;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime dateTimeCreated;

    @Column(nullable = false)
    private LocalDateTime dateTimeUpdated;

    @Column(nullable = false)
    private LocalDateTime dateTimeToDelete;

    @Column(nullable = false)
    private UUID organizerToken;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingParticipant> participants;
}


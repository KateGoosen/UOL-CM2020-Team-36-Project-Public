package com.team_36.cm2020.api_service.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
import lombok.Setter;

@Entity
@Builder
@Table(name = "meetings", schema = "scheduler")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column(nullable = false)
    private Integer duration;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingParticipant> participants;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots;

    @Column(nullable = false)
    private Boolean isVotingOpened;

    @Column
    private LocalDateTime finalTimeSlot;

    @Column
    private LocalDateTime votingDeadline;

    @Column(name = "common_time_slots_calculated", nullable = false)
    private boolean commonTimeSlotsCalculated = false;

}

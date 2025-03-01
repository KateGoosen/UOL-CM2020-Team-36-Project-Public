package com.team_36.cm2020.api_service.entities;

import com.team_36.cm2020.api_service.enums.Priority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "common_time_slots", schema = "scheduler")
public class CommonTimeSlot {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Enumerated(EnumType.STRING)
    @Column(name = "organizer_priority", nullable = false)
    private Priority organizerPriority;

    @Column(name = "high_priority_votes_count", nullable = false)
    private Integer highPriorityVotesCount = 0;

    @Column(name = "low_priority_votes_count", nullable = false)
    private Integer lowPriorityVotesCount = 0;
}


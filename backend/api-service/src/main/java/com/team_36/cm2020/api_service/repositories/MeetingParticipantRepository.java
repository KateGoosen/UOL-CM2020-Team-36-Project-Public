package com.team_36.cm2020.api_service.repositories;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, UUID> {
}


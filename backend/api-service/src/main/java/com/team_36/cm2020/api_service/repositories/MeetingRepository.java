package com.team_36.cm2020.api_service.repositories;
import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    List<Meeting> findAllByOrganizer(User organizer);
    List<Meeting> findAllByVotingDeadlineLessThan(LocalDateTime dateTime);
}


package com.team_36.cm2020.api_service.repositories;
import com.team_36.cm2020.api_service.entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
}


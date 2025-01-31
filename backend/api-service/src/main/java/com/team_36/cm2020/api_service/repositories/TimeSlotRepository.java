package com.team_36.cm2020.api_service.repositories;
import com.team_36.cm2020.api_service.entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
}

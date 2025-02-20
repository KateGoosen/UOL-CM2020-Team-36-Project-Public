package com.team_36.cm2020.api_service.repositories;

import com.team_36.cm2020.api_service.entities.CommonTimeSlot;
import com.team_36.cm2020.api_service.entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommonTimeSlotRepository extends JpaRepository<CommonTimeSlot, UUID> {
    List<CommonTimeSlot> findAllByMeeting(Meeting meeting);
}

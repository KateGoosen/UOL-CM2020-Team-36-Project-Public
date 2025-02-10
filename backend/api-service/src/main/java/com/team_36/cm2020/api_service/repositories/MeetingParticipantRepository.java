package com.team_36.cm2020.api_service.repositories;

import com.team_36.cm2020.api_service.entities.MeetingParticipant;
import com.team_36.cm2020.api_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, UUID> {
    void deleteAllByMeeting_MeetingIdAndUser_Email(UUID meetingId, Set<String> userEmails);

    List<MeetingParticipant> findAllByUser(User user);
}


package com.team_36.cm2020.api_service.repositories;
import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    List<Meeting> findAllByOrganizer(User organizer);
    List<Meeting> findAllByVotingDeadlineLessThanAndFinalTimeSlotIsNullAndCommonTimeSlotsCalculatedIsFalse(LocalDateTime dateTime);
    @Query(value = """
        SELECT CASE
            WHEN COUNT(DISTINCT mp.user_id) = COUNT(DISTINCT v.user_id)
            THEN TRUE ELSE FALSE END
        FROM scheduler.meeting_participants mp
        LEFT JOIN scheduler.votes v 
        ON mp.meeting_id = v.meeting_id AND mp.user_id = v.user_id
        WHERE mp.meeting_id = :meetingId
        """, nativeQuery = true)
    boolean haveAllParticipantsVoted(@Param("meetingId") UUID meetingId);
    List<Meeting> findAllByIfEveryoneVotedIsTrue();
}


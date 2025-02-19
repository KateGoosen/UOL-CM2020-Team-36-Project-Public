package com.team_36.cm2020.api_service.repositories;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    void deleteAllByUser_UserIdIn(List<UUID> userIds);
    List<Vote> findAllByMeetingAndUser(Meeting meeting, User user);


}
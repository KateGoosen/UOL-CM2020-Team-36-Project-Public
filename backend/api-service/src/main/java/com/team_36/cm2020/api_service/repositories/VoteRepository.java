package com.team_36.cm2020.api_service.repositories;

import com.team_36.cm2020.api_service.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
}
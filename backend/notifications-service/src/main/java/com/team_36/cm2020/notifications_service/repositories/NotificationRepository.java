package com.team_36.cm2020.notifications_service.repositories;

import com.team_36.cm2020.notifications_service.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}

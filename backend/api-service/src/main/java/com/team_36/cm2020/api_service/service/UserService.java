package com.team_36.cm2020.api_service.service;

import com.team_36.cm2020.api_service.entities.Meeting;
import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.output.CommonTimeSlotsResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void checkUserByEmail(String email);
    User getUserByEmail(String email);
    User saveUser(User user);
    Optional<User> getUserOptionalByEmail(String email);
}

package com.team_36.cm2020.api_service.service.impl;

import com.team_36.cm2020.api_service.entities.User;
import com.team_36.cm2020.api_service.exceptions.NoUserFoundException;
import com.team_36.cm2020.api_service.repositories.UserRepository;
import com.team_36.cm2020.api_service.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class userServiceImpl implements UserService {
    private final UserRepository userRepository;

    public void checkUserByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NoUserFoundException(String.format("No user found with email: %s", email));
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NoUserFoundException(String.format("No user found with email: %s", email));
        }
        return userOptional.get();
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> getUserOptionalByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}

package com.team_36.cm2020.api_service.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Getter
public class VoteInput {

    @NotBlank(message = "User email should not be empty")
    private String userEmail;

    private Set<LocalDateTime> highPriorityTimeSlots;
    private Set<LocalDateTime> lowPriorityTimeSlots;
}


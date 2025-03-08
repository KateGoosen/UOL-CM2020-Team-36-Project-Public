package com.team_36.cm2020.api_service.input;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoteInput {

    @NotBlank(message = "User email should not be empty")
    private String userEmail;

    @NonNull
    private List<LocalDateTime> highPriorityTimeSlots = new ArrayList<>();
    @NonNull
    private List<LocalDateTime> lowPriorityTimeSlots = new ArrayList<>();
}


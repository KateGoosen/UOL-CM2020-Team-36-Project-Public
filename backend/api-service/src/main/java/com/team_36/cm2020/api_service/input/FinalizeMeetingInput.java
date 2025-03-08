package com.team_36.cm2020.api_service.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class FinalizeMeetingInput {
    @NotNull
    private LocalDateTime finalTimeSlot;
}

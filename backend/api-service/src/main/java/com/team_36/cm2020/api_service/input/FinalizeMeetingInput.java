package com.team_36.cm2020.api_service.input;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class FinalizeMeetingInput {
    private LocalDateTime finalTimeSlot;
}

package com.team_36.cm2020.api_service.output;

import com.team_36.cm2020.api_service.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
public class TimeSlotResponse {
    private LocalDateTime dateTimeStart;
    private Priority priority;
}


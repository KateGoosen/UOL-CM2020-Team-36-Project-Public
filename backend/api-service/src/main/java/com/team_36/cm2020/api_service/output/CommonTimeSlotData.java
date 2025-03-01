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
public class CommonTimeSlotData {
    private LocalDateTime dateTimeStart;
    private Integer duration;
    private Priority organizerPriority;
    private Integer highPriorityVotesCount;
    private Integer lowPriorityVotesCount;
}


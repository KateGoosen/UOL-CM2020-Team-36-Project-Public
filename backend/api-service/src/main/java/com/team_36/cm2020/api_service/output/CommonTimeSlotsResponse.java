package com.team_36.cm2020.api_service.output;

import com.team_36.cm2020.api_service.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonTimeSlotsResponse {
    private List<CommonTimeSlotData> commonTimeSlotDataList;
}


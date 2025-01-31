package com.team_36.cm2020.api_service.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateMeetingResponse {

    private UUID organizerToken;
    private UUID meetingId;

}


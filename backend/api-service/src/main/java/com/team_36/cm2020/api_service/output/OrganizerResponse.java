package com.team_36.cm2020.api_service.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@AllArgsConstructor
public class OrganizerResponse {
    private String name;
    private String email;
}


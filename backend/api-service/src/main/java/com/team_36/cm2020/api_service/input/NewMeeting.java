package com.team_36.cm2020.api_service.input;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class NewMeeting {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Date options are required")
    @Size(min = 1, message = "At least one date option must be provided")
    private List<DateOption> dateOptions;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @NotNull(message = "Participants data is required")
    private List<Participant> participants;

    @NotNull(message = "Organizer data is required")
    private Organizer organizer;

    @NotBlank(message = "Timezone is required")
    private String timezone;

    @Data
    public static class DateOption {
        @NotBlank(message = "Start time is required")
        private String dateTimeStart; // ISO format

        @NotNull(message = "Priority is required")
        @Pattern(regexp = "HIGH|LOW", message = "Priority must be either HIGH or LOW")
        private String priority;
    }

    // Inner class for participants
    @Data
    @Getter
    public static class Participant {
        @NotBlank(message = "Participant name is required")
        private String name;

        @NotBlank(message = "Participant email is required")
        @Email(message = "Invalid email format")
        private String email;
    }

    // Inner class for organizer
    @Data
    public static class Organizer {
        @NotBlank(message = "Organizer name is required")
        private String name;

        @NotBlank(message = "Organizer email is required")
        @Email(message = "Invalid email format")
        private String email;
    }
}


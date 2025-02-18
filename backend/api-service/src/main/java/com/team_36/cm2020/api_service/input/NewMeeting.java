package com.team_36.cm2020.api_service.input;

import com.team_36.cm2020.api_service.enums.Priority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class NewMeeting {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Time slots are required")
    @Size(min = 1, message = "At least one date option must be provided")
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @NotNull(message = "Participants data is required")
    private List<Participant> participants = new ArrayList<>();;

    @NotNull(message = "Organizer data is required")
    private Organizer organizer;

    private LocalDateTime votingDeadline;

    @Data
    public static class TimeSlot {
        @NotBlank(message = "Start time is required")
        private LocalDateTime dateTimeStart; // ISO format

        @NotNull(message = "Priority is required")
        @Pattern(regexp = "HIGH|LOW", message = "Priority must be either HIGH or LOW")
        private Priority priority;
    }

    @Data
    @Getter
    @AllArgsConstructor
    public static class Participant {
        @NotBlank(message = "Participant name is required")
        private String name;

        @NotBlank(message = "Participant email is required")
        @Email(message = "Invalid email format")
        private String email;
    }

    @Data
    @Getter
    @AllArgsConstructor
    public static class Organizer {
        @NotBlank(message = "Organizer name is required")
        private String name;

        @NotBlank(message = "Organizer email is required")
        @Email(message = "Invalid email format")
        private String email;
    }
}


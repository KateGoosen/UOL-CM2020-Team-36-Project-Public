package com.team_36.cm2020.notifications_service.listeners.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class MessageDto implements Serializable {
    private String userName;
    private String meetingTitle;
    private UUID meetingId;
    private UUID userId;
    private UUID organizerToken;
    private String userEmail;
    private LocalDateTime meetingDateTime;
    private Integer duration;
    private String confirmationCode;

    public MessageDto(JsonNode messageJson) {
        this.userName = getStringFieldIfNotNull(messageJson, "userName");
        this.meetingTitle = getStringFieldIfNotNull(messageJson, "meetingTitle");
        this.meetingId = getUUIDFieldIfNotNull(messageJson, "meetingId");
        this.userId = getUUIDFieldIfNotNull(messageJson, "userId");
        this.organizerToken = getUUIDFieldIfNotNull(messageJson, "creatorToken");
        this.userEmail = getStringFieldIfNotNull(messageJson, "userEmail");
        this.meetingDateTime = getLocalDateTimeFieldIfNotNull(messageJson, "meetingDateTime");
        this.confirmationCode = getStringFieldIfNotNull(messageJson, "confirmationCode");
        this.duration = getIntegerFieldIfNotNull(messageJson, "duration");
    }

    private String getStringFieldIfNotNull(JsonNode messageJson, String fieldName) {
        JsonNode jsonNode = messageJson.get(fieldName);
        return jsonNode != null ? jsonNode.asText() : null;
    }

    private Integer getIntegerFieldIfNotNull(JsonNode messageJson, String fieldName) {
        JsonNode jsonNode = messageJson.get(fieldName);
        return jsonNode != null ? jsonNode.asInt() : null;
    }

    private UUID getUUIDFieldIfNotNull(JsonNode messageJson, String fieldName) {
        JsonNode jsonNode = messageJson.get(fieldName);
        return jsonNode != null ? UUID.fromString(jsonNode.asText()) : null;
    }

    private LocalDateTime getLocalDateTimeFieldIfNotNull(JsonNode messageJson, String fieldName) {
        JsonNode jsonNode = messageJson.get(fieldName);
        return (jsonNode != null)
                ? LocalDateTime.parse(jsonNode.asText(), DateTimeFormatter.ISO_DATE_TIME)
                : null;
    }
}

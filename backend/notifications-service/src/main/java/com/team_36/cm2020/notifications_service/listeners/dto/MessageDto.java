package com.team_36.cm2020.notifications_service.listeners.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageDto {
    private final String userName;
    private final String meetingTitle;
    private final UUID meetingId;
    private final UUID userId;
    private final UUID organizerToken;
    private final String userEmail;
    private final String meetingDateTime;
    private final Integer duration;
    private final String confirmationCode;

    public MessageDto(JsonNode messageJson) {
        this.userName = getStringFieldIfNotNull(messageJson, "userName");
        this.meetingTitle = getStringFieldIfNotNull(messageJson, "meetingTitle");
        this.meetingId = getUUIDFieldIfNotNull(messageJson, "meetingId");
        this.userId = getUUIDFieldIfNotNull(messageJson, "userId");
        this.organizerToken = getUUIDFieldIfNotNull(messageJson, "creatorToken");
        this.userEmail = getStringFieldIfNotNull(messageJson, "userEmail");
        this.meetingDateTime = getStringFieldIfNotNull(messageJson, "meetingDateTime");
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
}

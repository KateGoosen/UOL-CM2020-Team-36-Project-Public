package com.team_36.cm2020.notifications_service.listeners;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageDto {
    private final String userName;
    private final String meetingTitle;
    private final UUID meetingId;
    private final UUID userId;
    private final String creatorToken;
    private final String userEmail;
    private final String meetingDateTime;
    private final String confirmationCode;

    public MessageDto(JsonNode messageJson) {
        this.userName = getStringFieldIfNotNull(messageJson, "userName");
        this.meetingTitle = getStringFieldIfNotNull(messageJson, "meetingTitle");
        this.meetingId = getUUIDFieldIfNotNull(messageJson, "meetingId");
        this.userId = getUUIDFieldIfNotNull(messageJson, "userId");
        this.creatorToken = getStringFieldIfNotNull(messageJson, "creatorToken");
        this.userEmail = getStringFieldIfNotNull(messageJson, "userEmail");
        this.meetingDateTime = getStringFieldIfNotNull(messageJson, "meetingDateTime");
        this.confirmationCode = getStringFieldIfNotNull(messageJson, "confirmationCode");
    }

    private String getStringFieldIfNotNull(JsonNode messageJson, String fieldName) {
        JsonNode jsonNode = messageJson.get(fieldName);
        return jsonNode != null ? jsonNode.asText() : null;
    }

    private UUID getUUIDFieldIfNotNull(JsonNode messageJson, String fieldName) {
        JsonNode jsonNode = messageJson.get(fieldName);
        return jsonNode != null ? UUID.fromString(jsonNode.asText()) : null;
    }
}

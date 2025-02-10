package com.team_36.cm2020.api_service.rmq;

public enum NotificationType {
    NEW_MEETING_ORGANIZER("meeting.exchange", "meeting.created.organizer"),
    NEW_MEETING_PARTICIPANTS("meeting.exchange", "meeting.created.participants"),
    VOTE_REGISTERED_ORGANIZER("vote.exchange", "vote.registered.organizer"),
    VOTE_REGISTERED_PARTICIPANT("vote.exchange", "vote.registered.participant"),
    MEETING_FINALIZED_ORGANIZER("meeting.exchange", "meeting.finalized.organizer"),
    MEETING_FINALIZED_PARTICIPANTS("meeting.exchange", "meeting.finalized.participants"),
    MEETING_NO_SLOTS_ORGANIZER("meeting.exchange", "meeting.no_slots.organizer"),
    MEETING_NO_SLOTS_PARTICIPANTS("meeting.exchange", "meeting.no_slots.participants"),
    MEETING_DELETED_ORGANIZER("meeting.exchange", "meeting.deleted.organizer"),
    AUTH_SIGNUP_CONFIRMATION("auth.exchange", "auth.signup.confirmation_code"),
    AUTH_SIGNUP_SUCCESS("auth.exchange", "auth.signup.success"),
    AUTH_LOGIN_CONFIRMATION("auth.exchange", "auth.login.confirmation_code"),
    AUTH_PASSWORD_RESET("auth.exchange", "auth.password.reset"),
    LINK_RESTORE_ORGANIZER("meeting.exchange", "link.restore.organizer");


    private final String exchange;
    private final String routingKey;

    NotificationType(String exchange, String routingKey) {
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}


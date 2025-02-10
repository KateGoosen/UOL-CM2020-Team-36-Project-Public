-- Users Table
CREATE TABLE scheduler.users
(
    user_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email               VARCHAR(255) NOT NULL UNIQUE,
    name                VARCHAR(255) NOT NULL,
    password            VARCHAR(255),
    last_time_active    TIMESTAMP    NOT NULL,
    date_time_to_delete TIMESTAMP    NOT NULL,
    is_registered       BOOLEAN      NOT NULL
);

-- Meetings Table
CREATE TABLE scheduler.meetings
(
    meeting_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organizer           UUID         NOT NULL REFERENCES scheduler.users (user_id),
    title               VARCHAR(255) NOT NULL,
    description         TEXT,
    duration            INT          NOT NULL,
    date_time_created   TIMESTAMP    NOT NULL,
    date_time_updated   TIMESTAMP    NOT NULL,
    date_time_to_delete TIMESTAMP    NOT NULL,
    organizer_token     UUID         NOT NULL,
    is_voting_opened    BOOLEAN      NOT NULL,
    final_time_slot     TIMESTAMP,
    voting_deadline     TIMESTAMP
);

-- Votes Table
CREATE TABLE scheduler.votes
(
    vote_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id UUID        NOT NULL REFERENCES scheduler.meetings (meeting_id),
    time_start TIMESTAMP   NOT NULL,
    time_end   TIMESTAMP   NOT NULL,
    priority   VARCHAR(10) NOT NULL CHECK (priority IN ('HIGH', 'LOW')),
    user_id    UUID        NOT NULL REFERENCES scheduler.users (user_id)
);

-- Time Slots Table
CREATE TABLE scheduler.time_slots
(
    time_slot_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id   UUID        NOT NULL REFERENCES scheduler.meetings (meeting_id),
    time_start   TIMESTAMP   NOT NULL,
    time_end     TIMESTAMP   NOT NULL,
    priority     VARCHAR(10) NOT NULL CHECK (priority IN ('HIGH', 'LOW')),
    organizer_id UUID        NOT NULL REFERENCES scheduler.users (user_id)
);
-- Meeting Participants (Junction Table)
CREATE TABLE scheduler.meeting_participants
(
    meeting_id UUID NOT NULL REFERENCES scheduler.meetings (meeting_id) ON DELETE CASCADE,
    user_id    UUID NOT NULL REFERENCES scheduler.users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (meeting_id, user_id)
);

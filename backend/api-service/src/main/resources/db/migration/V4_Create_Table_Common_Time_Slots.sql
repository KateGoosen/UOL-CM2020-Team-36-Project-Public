CREATE TABLE scheduler.common_time_slots
(
    id                        UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    meeting_id                INT         NOT NULL,
    date_time_start           TIMESTAMP   NOT NULL,
    organizer_priority        VARCHAR(10) NOT NULL CHECK (organizer_priority IN ('LOW', 'HIGH')),
    high_priority_votes_count INT         NOT NULL DEFAULT 0,
    low_priority_votes_count  INT         NOT NULL DEFAULT 0,
    CONSTRAINT fk_meeting FOREIGN KEY (meeting_id) REFERENCES meetings (meeting_id) ON DELETE CASCADE
);
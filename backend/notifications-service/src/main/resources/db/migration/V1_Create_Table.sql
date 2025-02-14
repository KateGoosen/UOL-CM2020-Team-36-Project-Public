-- Create the 'notifications' table in the 'scheduler' schema
CREATE TABLE notifications
(
    notification_id UUID PRIMARY KEY,
    meeting_id      UUID         NOT NULL,
    meeting_title   VARCHAR(255),
    user_id         UUID         NOT NULL,
    user_email      VARCHAR(255) NOT NULL,
    date_time_sent  TIMESTAMP    NOT NULL,
    success         boolean      NOT NULL,
    error           VARCHAR

);

-- Ensure indexes are created for efficient query execution
CREATE INDEX idx_notifications_meeting_id ON notifications (meeting_id);
CREATE INDEX idx_notifications_user_id ON notifications (user_id);

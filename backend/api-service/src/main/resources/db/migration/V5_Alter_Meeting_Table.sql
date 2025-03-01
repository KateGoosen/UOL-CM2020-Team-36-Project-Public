ALTER TABLE scheduler.meetings
    ADD COLUMN if_everyone_voted BOOLEAN DEFAULT FALSE;
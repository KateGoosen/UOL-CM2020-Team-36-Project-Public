export interface Organizer {
  name: string;
  email: string;
}

export interface TimeSlot {
  dateTimeStart: string;
  priority:  "HIGH" | "LOW";
}

export interface Meeting {
  title: string;
  description: string;
  timeSlots: TimeSlot[];
  duration: number;
  organizer: Organizer;
  votingDeadline: string | null;
  participants: Participant[];
}
export interface Participant {
  name: string;
  email: string;
}

export interface SelectedSlot {
  day: string;
  hour: number;
  period: string;
}

export interface MarkedSlot {
  day: string;
  hour: number;
  period: string;
  availabilityType: "HIGH" | "LOW";
}

export type TimeSlots = {
  highPriorityTimeSlots: string[];
  lowPriorityTimeSlots: string[]; 
};
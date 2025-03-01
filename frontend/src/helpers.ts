import { format, parseISO } from "date-fns";
import { MarkedSlot, TimeSlot, TimeSlots } from "./types";

  export const convertToDateTimeStart = (availabilityList: MarkedSlot[]) => {
    return availabilityList.map((slot) => {
      let hour = slot.hour;
      if (slot.period.toLowerCase() === "pm" && hour !== 12) {
        hour += 12;
      } else if (slot.period.toLowerCase() === "am" && hour === 12) {
        hour = 0;
      }

      const dateTimeStart = `${slot.day}T${String(hour).padStart(
        2,
        "0"
      )}:00:00.000Z`;

      return {
        dateTimeStart,
        priority: slot.availabilityType,
      };
    });
  };

  export const convertToSeconds = (durationHours: number, durationMinutes: number) => {
    return durationHours * 3600 + durationMinutes * 60;
  };

  export const convertToHoursAndMinutes = (durationInSeconds: number) => {
  const hours = Math.floor(durationInSeconds / 3600);
  const minutes = Math.floor((durationInSeconds % 3600) / 60);
  
  return { hours, minutes };
}

export const convertFromDateTimeStart = (timeSlots: TimeSlot[]) => {
 return timeSlots.map((slot) => {
    const date = parseISO(slot.dateTimeStart);

    let hour = date.getUTCHours();
    let period = "am";

    if (hour >= 12) {
      period = "pm";
      if (hour > 12) {
        hour -= 12;
      }
    } else if (hour === 0) {
      hour = 12;
    }

    return {
      day: format(date, "yyyy-MM-dd"), 
      hour,
      period,
      availabilityType: slot.priority,
    };
  });
};


const isValidSlot = (slot: MarkedSlot): boolean => {
  const isDayValid = /^\d{4}-\d{2}-\d{2}$/.test(slot.day);
  const isHourValid = slot.hour >= 0 && slot.hour <= 12;
  const isPeriodValid = ["am", "pm"].includes(slot.period.toLowerCase());
  const isAvailabilityTypeValid = ["HIGH", "LOW"].includes(slot.availabilityType);

  return isDayValid && isHourValid && isPeriodValid && isAvailabilityTypeValid;
};

export const convertSlotsToVote = (slots: MarkedSlot[]): TimeSlots => {
   const highPriorityTimeSlots: string[] = [];
  const lowPriorityTimeSlots: string[] = [];

  slots.forEach((slot) => {
    if (!isValidSlot(slot)) {
      console.warn("Invalid slot:", slot);
      return;
    }
    const date = new Date(slot.day);
    let hour = slot.hour;
    const period = slot.period.toLowerCase();

    if (period === "pm" && hour < 12) {
      hour += 12; 
    } else if (period === "am" && hour === 12) {
      hour = 0; 
    }

    date.setHours(hour, 0, 0, 0); 

    if (slot.availabilityType === "HIGH") {
      highPriorityTimeSlots.push(date.toISOString());
    } else if (slot.availabilityType === "LOW") {
      lowPriorityTimeSlots.push(date.toISOString());
    }
  });

  return {
    highPriorityTimeSlots,
    lowPriorityTimeSlots,
  };

};
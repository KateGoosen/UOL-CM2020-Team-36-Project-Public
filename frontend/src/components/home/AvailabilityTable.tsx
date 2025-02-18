import React, { FC, useEffect, useState } from "react";
import {
  MdArrowBackIos as MdArrowBack,
  MdArrowForwardIos as MdArrowForward,
} from "react-icons/md";
import {
  format,
  addDays,
  startOfToday,
  differenceInMinutes,
  addWeeks,
} from "date-fns";
import { MarkedSlot, SelectedSlot } from "@/pages/ScheduleMeeting";

interface AvailabilityTableProps {
  selectedSlots: SelectedSlot[];
  markedSlots: MarkedSlot[];
  setSelectedSlots: (slots: SelectedSlot[]) => void;
}

const AvailabilityTable: FC<AvailabilityTableProps> = ({
  selectedSlots,
  setSelectedSlots,
  markedSlots,
}) => {
  const today = startOfToday();
  const [linePosition, setLinePosition] = useState(0);
  const [weekOffset, setWeekOffset] = useState(0);

  const getColorForSlot = (isSelected: boolean, markedSlot?: MarkedSlot) => {
    if (isSelected) {
      return "bg-primary";
    } else if (markedSlot) {
      return markedSlot.availabilityType === "available"
        ? "bg-secondary"
        : "bg-mainYellow";
    } else {
      return "bg-white";
    }
  };

  const startOfCurrentWeek = addWeeks(today, weekOffset);

  const weekDays = Array.from({ length: 7 }).map((_, index) => {
    const date = addDays(startOfCurrentWeek, index);
    return {
      day: format(date, "E"),
      date: format(date, "d"),
    };
  });

  const hours = Array.from({ length: 24 }).map((_, index) => {
    const hour = index % 12 === 0 ? 12 : index % 12;
    const period = index < 12 ? "AM" : "PM";
    return `${hour} ${period}`;
  });

  useEffect(() => {
    const updateLinePosition = () => {
      const now = new Date();

      const minutesSinceStartOfDay = now.getHours() * 60 + now.getMinutes();

      const position = (minutesSinceStartOfDay / 60) * 48;
      setLinePosition(position);
    };

    updateLinePosition();
    const interval = setInterval(updateLinePosition, 60000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className='flex flex-col w-[60%] relative'>
      {/* HEADER */}
      <div className='flex flex-row justify-between items-center p-4 border border-black rounded-t-md'>
        <div>
          <p>{format(startOfCurrentWeek, "MMMM yyyy")}</p>
        </div>
        <div className='flex flex-row items-center gap-4'>
          <MdArrowBack
            className='cursor-pointer'
            onClick={() => setWeekOffset((prev) => prev - 1)}
          />
          <MdArrowForward
            className='cursor-pointer'
            onClick={() => setWeekOffset((prev) => prev + 1)}
          />
        </div>
      </div>
      {/* WEEK DAYS */}
      <div className='flex flex-row'>
        <div className='flex w-24 border-l border-gray-300 ' />
        {weekDays.map((day, index) => (
          <div
            key={index}
            className='flex flex-1 flex-row gap-2 items-center justify-center border border-gray-300 p-2 bg-gray-100'
          >
            <p className='font-semibold'>{day.day}</p>
            <p>{day.date}</p>
          </div>
        ))}
      </div>
      {/* HOURS */}
      <div className='relative max-h-[500px] overflow-scroll'>
        {hours.map((hour, rowIndex) => (
          <div key={rowIndex} className='flex flex-row items-center'>
            {/* Hour rectangle */}
            <div className='flex w-24 h-12 items-center justify-center bg-gray-200 border border-gray-300'>
              <p className='font-medium'>{hour}</p>
            </div>

            {/* 7 Empty slot rectangles */}
            {weekDays.map((_day, colIndex) => {
              const period = rowIndex < 12 ? "am" : "pm";
              const slot = {
                day: format(
                  addDays(startOfCurrentWeek, colIndex),
                  "yyyy-MM-dd"
                ),
                hour: rowIndex % 12 === 0 ? 12 : rowIndex % 12,
                period: period,
              };

              const isSelected = selectedSlots.some(
                (s) =>
                  s.day === slot.day &&
                  s.hour === slot.hour &&
                  s.period === slot.period
              );

              const isMarked = markedSlots.find(
                (m) =>
                  m.day === slot.day &&
                  m.hour === slot.hour &&
                  m.period === slot.period
              );

              return (
                <div
                  key={colIndex}
                  className={`flex flex-1 h-12 border border-gray-400 cursor-pointer ${getColorForSlot(
                    isSelected,
                    isMarked
                  )} hover:bg-gray-100`}
                  onClick={() => {
                    if (isSelected) {
                      // Remove the slot if it's already selected
                      setSelectedSlots(
                        selectedSlots.filter(
                          (s) =>
                            !(
                              s.day === slot.day &&
                              s.hour === slot.hour &&
                              s.period === slot.period
                            )
                        )
                      );
                    } else {
                      // Add the slot if it's not selected
                      setSelectedSlots([...selectedSlots, slot]);
                    }
                  }}
                />
              );
            })}
          </div>
        ))}
        {/* Current Time Red Line, only show on the current week*/}
        {weekOffset === 0 && (
          <div
            className='absolute left-0 right-0 h-[2px] bg-red-500'
            style={{ top: `${linePosition}px` }}
          />
        )}
      </div>
    </div>
  );
};

export default AvailabilityTable;

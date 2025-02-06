import React from "react";
import {
  MdArrowBackIos as MdArrowBack,
  MdArrowForwardIos as MdArrowForward,
} from "react-icons/md";
import { format, addDays, startOfToday } from "date-fns";

const AvailabilityTable = () => {
  const today = startOfToday();
  const weekDays = Array.from({ length: 7 }).map((_, index) => {
    const date = addDays(today, index);
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

  return (
    <div className='flex flex-col mt-8 w-[70%]'>
      {/* HEADER */}
      <div className='flex flex-row justify-between items-center p-4 border border-black rounded-t-md'>
        <div>
          <p>December 2024</p>
        </div>
        <div className='flex flex-row items-center gap-4'>
          <MdArrowBack className='cursor-pointer' />
          <MdArrowForward className='cursor-pointer' />
        </div>
      </div>
      {/* WEEK DAYS */}
      <div className='flex flex-row'>
        <div className='flex w-24' />{" "}
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
      {hours.map((hour, rowIndex) => (
        <div key={rowIndex} className='flex flex-row items-center'>
          {/* Hour rectangle */}
          <div className='flex w-24 h-12 items-center justify-center bg-gray-200 border border-gray-300'>
            <p className='font-medium'>{hour}</p>
          </div>

          {/* 7 Empty slot rectangles */}
          {weekDays.map((_, colIndex) => (
            <div
              key={colIndex}
              className='flex flex-1 h-12 border border-gray-300 bg-white cursor-pointer hover:bg-gray-100'
            />
          ))}
        </div>
      ))}
    </div>
  );
};

export default AvailabilityTable;

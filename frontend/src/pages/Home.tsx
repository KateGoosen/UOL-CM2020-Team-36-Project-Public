import React from "react";
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/shadcdn/ui/card";
import { Button } from "@/components/shadcdn/ui/button";
import { useNavigate } from "react-router";
import Header from "@/components/Header";
import MeetingImage from "@/assets/meeting.png";

const Home = () => {
  const navigate = useNavigate();

  const handleScheduleMeeting = () => {
    navigate("/schedule-meeting");
  };

  return (
    <div>
      <Header />
      <div className='pt-[50px] pb-[100px] px-[150px] flex flex-col'>
        <h2 className='self-center text-2xl font-semibold'>
          Effortless meeting scheduling
        </h2>
        <p className='font-medium text-xl mt-[60px]'>
          Schedule meetings seamlessly, find the best time for <br /> everyone,
          and stay organized.
        </p>
        <div className='flex flex-row mt-8 h-[400px] gap-8 justify-between'>
          {/* Card */}
          <Card className='p-8 rounded-2xl border-black flex-1 max-w-[50%] h-full'>
            <CardDescription className='h-full'>
              <ul className='h-full flex flex-col justify-evenly font-medium text-lg'>
                <li>
                  Smart Scheduling: Automatically finds the best time slots for
                  all participants.
                </li>
                <li>
                  Voting System: Participants vote on available time slots.
                </li>
                <li>
                  Organizer Controls: Manage, edit, or cancel meetings with
                  ease.
                </li>
                <li>Timezone Support: Works across different time zones.</li>
                <li>
                  Reminders & Notifications: Get notified when participants vote
                  or changes occur.
                </li>
              </ul>
            </CardDescription>
          </Card>

          {/* Image */}
          <div className='flex'>
            <img
              src={MeetingImage}
              alt='Meeting Illustration'
              className='rounded-2xl h-full w-auto object-cover'
            />
          </div>
        </div>
        <Button
          className='w-[350px] bg-[#b4c1ff] mt-8 self-center hover:bg-blue-500 cursor-pointer'
          onClick={handleScheduleMeeting}
        >
          Schedule meeting
        </Button>

        {/* Steps Section */}
        <div className='flex flex-row items-center justify-center mt-12 gap-6'>
          {[
            {
              title: "Create a Meeting",
              description: "Set up a meeting with possible time slots.",
            },
            {
              title: "Invite Participants",
              description: "Share a unique link for voting.",
            },
            {
              title: "Vote on Time Slots",
              description: "Participants select their availability.",
            },
            {
              title: "Confirm the Meeting",
              description: "The app picks the best slot based on votes.",
            },
          ].map((step, index) => (
            <React.Fragment key={step.title}>
              <Card className='rounded-full w-[200px] h-[200px] flex items-start justify-center text-center p-4'>
                <CardHeader>
                  <CardTitle className='text-lg'>{step.title}</CardTitle>
                  <CardDescription className='text-sm'>
                    {step.description}
                  </CardDescription>
                </CardHeader>
              </Card>
              {index < 3 && (
                <span className='text-2xl font-bold text-gray-500'>→</span>
              )}
            </React.Fragment>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Home;

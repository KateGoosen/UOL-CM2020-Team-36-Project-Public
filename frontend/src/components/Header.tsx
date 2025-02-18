import { useNavigate } from "react-router";
import { Card, CardHeader, CardTitle } from "./shadcdn/ui/card";
import { Button } from "./shadcdn/ui/button";

const Header = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  const handleScheduleMeeting = () => {
    navigate("/schedule-meeting");
  };

  const handleShowScheduledMeetings = () => {
    navigate("/view-scheduled-meetings");
  };

  return (
    <div className='bg-primary h-[80px] w-full pl-[50px] pr-[100px] flex flex-row items-center justify-between'>
      <p
        onClick={handleGoHome}
        className='text-black font-semibold text-2xl cursor-pointer'
      >
        Syncify
      </p>
      <div className='flex flex-row gap-[50px] items-center '>
        <Button
          className='w-[350px] bg-[#b4c1ff] hover:bg-white cursor-pointer'
          onClick={handleScheduleMeeting}
        >
          Schedule meeting
        </Button>
        <Button
          className='w-[350px] bg-[#b4c1ff] hover:bg-white cursor-pointer'
          onClick={handleShowScheduledMeetings}
        >
          View Scheduled Meetings
        </Button>
      </div>
    </div>
  );
};

export default Header;

import Header from "@/components/Header";
import { Button } from "@/components/shadcdn/ui/button";
import { useEffect, useState } from "react";
import { MdArrowBack } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";
import { MeetingData } from "./FinalizeMeeting";
import api from "@/api/axios";
import { format } from "date-fns";

const MeetingFinalizeSuccess = () => {
  const navigate = useNavigate();
  const { meetingId, organizerToken } = useParams();
  const [meetingData, setMeetingData] = useState<MeetingData | null>(null);

  const getAndSetMeetingData = async () => {
    try {
      const response = await api.get(
        `api/meeting/${meetingId}/${organizerToken}`
      );
      if (Object.keys(response.data).length) {
        setMeetingData(response.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (meetingId && organizerToken) {
      getAndSetMeetingData();
    }
  }, [meetingId, organizerToken]);

  const handleGoBack = () => {
    navigate("/");
  };

  return (
    <div className='flex flex-col pb-[100px] items-center'>
      <Header />
      <div className='flex w-full flex-row justify-start items-center mt-[50px] px-8'>
        <MdArrowBack
          className='h-12 w-12 cursor-pointer'
          onClick={handleGoBack}
        />
      </div>
      <p className='text-2xl font-medium mt-8'>
        Meeting date and time has been finalized!
      </p>
      <p className='text-lg font-medium  mt-[50px]'>
        All participants will receive notifications.
      </p>

      <div className='w-[500px] flex flex-col gap-8 mt-[80px]'>
        <div className='flex flex-row items-center justify-between'>
          <p className='font-semibold text-lg'>Meeting title</p>
          <p>{meetingData?.title}</p>
        </div>
        <div className='flex flex-row items-center justify-between'>
          <p className='font-semibold text-lg'>Meeting date and time</p>
          {meetingData?.finalDateTimeSlot ? (
            <p>
              {format(
                new Date(meetingData.finalDateTimeSlot),
                "dd MMMM yyyy   HH:mm"
              )}
            </p>
          ) : null}
        </div>
      </div>

      <Button
        className='bg-primary w-[250px] mt-[80px] self-center'
        onClick={() => navigate("/")}
      >
        Return to Home Screen
      </Button>
    </div>
  );
};

export default MeetingFinalizeSuccess;

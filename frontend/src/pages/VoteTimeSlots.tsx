import Header from "@/components/Header";
import { Input } from "@/components/shadcdn/ui/input";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { MdArrowBack } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";
import { debounce } from "lodash";
import api from "@/api/axios";
import { convertSlotsToVote, convertToHoursAndMinutes } from "@/helpers";
import AvailabilityTable from "@/components/AvailabilityTable";
import {
  Card,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/shadcdn/ui/card";
import { MarkedSlot, SelectedSlot } from "@/types";
import { Button } from "@/components/shadcdn/ui/button";

interface ParticipantMeeting {
  title: string;
  description: string;
  organizerName: string;
  organizerEmail: string;
  duration: number;
  finalTimeSlot?: string;
  isVotingOpened: boolean;
}

const VoteTimeSlots = () => {
  const { meetingId } = useParams();
  const navigate = useNavigate();
  const [participantMeetingData, setParticipantMeetingData] =
    useState<ParticipantMeeting | null>(null);
  const [participantEmail, setParticipantEmail] = useState("");
  const [availabilitySelection, setAvailabilitySelection] = useState<
    "HIGH" | "LOW"
  >("HIGH");
  const [selectedSlots, setSelectedSlots] = useState<SelectedSlot[]>([]);
  const [markedSlots, setMarkedSlots] = useState<MarkedSlot[]>([]);
  const showForm = participantEmail.length && participantMeetingData;

  const durationString = useMemo(() => {
    if (participantMeetingData?.duration) {
      const { hours, minutes } = convertToHoursAndMinutes(
        participantMeetingData?.duration
      );
      return `${hours} hs. ${minutes} min.`;
    }

    return "";
  }, [participantMeetingData?.duration]);

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleMarkSelectedSlots = () => {
    setMarkedSlots([
      ...markedSlots,
      ...selectedSlots.map((sl) => {
        return { ...sl, availabilityType: availabilitySelection };
      }),
    ]);
    setSelectedSlots([]);
  };

  const handleUnmarkSelectedSlots = () => {
    setMarkedSlots(
      markedSlots.filter(
        (ms) =>
          !selectedSlots.some(
            (sl) =>
              sl.day === ms.day &&
              sl.hour === ms.hour &&
              sl.period === ms.period
          )
      )
    );
    setSelectedSlots([]);
  };

  const getMeetingForParticipant = useCallback(
    debounce(async (email) => {
      try {
        const response = await api.get(
          `api/meeting/participant/${meetingId}/${email}`
        );
        if (Object.keys(response.data).length) {
          setParticipantMeetingData(response.data);
        }
        console.log("API Response:", response.data);
      } catch (error) {
        alert("The email doesn't correspond to a participant");
      }
    }, 1000),
    []
  );

  const handleSetVote = async () => {
    try {
      if (markedSlots.length) {
        const { highPriorityTimeSlots, lowPriorityTimeSlots } =
          convertSlotsToVote(markedSlots);
        console.log({ highPriorityTimeSlots, lowPriorityTimeSlots });
        await api.post(`api/meeting/${meetingId}/vote`, {
          userEmail: participantEmail,
          highPriorityTimeSlots,
          lowPriorityTimeSlots,
        });
        navigate(`/meeting/${meetingId}/success`);
      } else {
        alert("You need to mark slots first");
      }
    } catch (error) {
      console.error("unable to vote", error);
      alert("The participant already voted!");
    }
  };

  useEffect(() => {
    if (participantEmail.length) {
      getMeetingForParticipant(participantEmail);
    }
  }, [participantEmail]);

  const LabelWithValue = ({
    label,
    value,
  }: {
    label: string;
    value: string;
  }) => {
    return (
      <div className='flex flex-row justify-start items-center gap-8'>
        <p className='font-semibold text-lg'>{label}:</p>
        <p className='font-normal text-gray-700 text-md'>{value}</p>
      </div>
    );
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
      <div className='flex flex-col mt-8 w-[50%] items-start'>
        <p className='text-lg font-medium  my-4'>Participant email</p>
        <Input
          className='w-[400px]'
          placeholder='Participant email'
          value={participantEmail}
          onChange={(event) => setParticipantEmail(event.target.value)}
        />
        {showForm ? (
          <>
            <div className='mt-[50px] flex flex-col items-center gap-4 w-full'>
              <p className='font-semibold text-3xl'>
                {participantMeetingData?.title}
              </p>
              <p className='font-medium text-xl'>
                {participantMeetingData?.description}
              </p>
            </div>
            <div className='mt-8 flex flex-col items-start gap-4 w-full'>
              <LabelWithValue label='Duration' value={durationString} />
              <LabelWithValue
                label='Organizer name'
                value={participantMeetingData?.organizerName ?? ""}
              />
              <LabelWithValue
                label='Organizer email'
                value={participantMeetingData?.organizerEmail ?? ""}
              />
            </div>
            <p className='mt-[50px] font-semibold text-lg'>
              Set your vote for the meeting time:
            </p>
          </>
        ) : null}
      </div>
      {showForm ? (
        <div className='flex w-full flex-row justify-between mt-[50px] pb-[50px] px-[100px]'>
          <AvailabilityTable
            selectedSlots={selectedSlots}
            setSelectedSlots={setSelectedSlots}
            markedSlots={markedSlots}
          />
          <Card className='bg-white border border-black h-[350px] ml-[50px]  right-[50px]'>
            <CardHeader>
              <CardTitle className='text-xl'>Set Availability</CardTitle>
              <CardDescription>
                <div className='flex flex-col gap-2 mt-8'>
                  <p>Green: Preferred meeting times.</p>
                  <p>Yellow: Less desirable times but manageable if needed.</p>
                  <p>Not marked: Times they are absolutely unavailable.</p>
                </div>

                <div className='flex flex-row gap-8 mt-4'>
                  <div
                    className='h-12 w-12 border-black bg-secondary cursor-pointer'
                    style={{
                      borderWidth: availabilitySelection === "HIGH" ? 2 : 1,
                    }}
                    onClick={() => setAvailabilitySelection("HIGH")}
                  />
                  <div
                    className='h-12 w-12 border-2 border-black bg-mainYellow cursor-pointer'
                    style={{
                      borderWidth: availabilitySelection === "LOW" ? 2 : 1,
                    }}
                    onClick={() => setAvailabilitySelection("LOW")}
                  />
                </div>
              </CardDescription>
              <CardFooter
                style={{
                  marginTop: 50,
                  gap: 24,
                }}
              >
                <Button
                  className='bg-primary flex-1 self-center'
                  onClick={handleMarkSelectedSlots}
                >
                  Mark selection
                </Button>
                <Button
                  className='bg-red-400 flex-1 self-center hover:bg-red-600'
                  onClick={handleUnmarkSelectedSlots}
                >
                  Unmark selection
                </Button>
              </CardFooter>
            </CardHeader>
          </Card>
        </div>
      ) : null}
      {showForm ? (
        <Button
          className='bg-primary w-[250px] mt-[50px] self-center'
          onClick={handleSetVote}
        >
          Vote
        </Button>
      ) : null}
    </div>
  );
};

export default VoteTimeSlots;

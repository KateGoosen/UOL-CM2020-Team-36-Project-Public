import api from "@/api/axios";
import Header from "@/components/Header";
import { Button } from "@/components/shadcdn/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/shadcdn/ui/table";
import { format } from "date-fns";
import { useEffect, useState } from "react";
import { MdArrowBack } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";

const dummyVotes = [
  { date: "2025-12-12T14:00:00Z", priority: "High", high: 5, low: 3 },
  { date: "2025-11-25T09:00:00Z", priority: "Medium", high: 2, low: 1 },
  { date: "2025-10-05T18:00:00Z", priority: "Low", high: 4, low: 2 },
];

export interface MeetingData {
  title: string;
  votes: { date: string; priority: string; high: number; low: number }[];
  finalDateTimeSlot?: string;
}

const FinalizeMeeting = () => {
  const { meetingId, organizerToken } = useParams();
  const navigate = useNavigate();
  const [meetingData, setMeetingData] = useState<MeetingData | null>(null);
  const [selectedRow, setSelectedRow] = useState<number | null>(null);

  const getAndSetMeetingData = async () => {
    try {
      const response = await api.get(
        `api/meeting/${meetingId}/${organizerToken}`
      );
      if (Object.keys(response.data).length) {
        setMeetingData({ ...response.data, votes: dummyVotes });
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
    navigate(-1);
  };

  const handleSubmit = async () => {
    if (selectedRow !== null) {
      const selectedVote = meetingData?.votes[selectedRow];
      try {
        await api.put(`api/meeting/${meetingId}/${organizerToken}/finalize`, {
          finalTimeSlot: selectedVote?.date,
        });
        navigate(`/meeting/${meetingId}/${organizerToken}/finalize`);
      } catch (error) {
        console.log(error);
      }
    }
  };

  return (
    <div className='flex flex-col items-center w-full'>
      <Header />
      <div className='flex w-full flex-row justify-start items-center mt-[50px] px-8'>
        <MdArrowBack
          className='h-12 w-12 cursor-pointer'
          onClick={handleGoBack}
        />
      </div>
      {!meetingData?.finalDateTimeSlot ? (
        <div className='flex flex-col mt-8 w-[60%] items-center'>
          <p className='font-semibold text-2xl'>
            Finalize the meeting "{meetingData?.title}"
          </p>
          <p className='mt-[50px] text-start'>
            There are time slots which are suitable for all participants, choose
            one to finalize the date and time of the meeting.
          </p>

          <Table className='mt-8'>
            <TableHeader>
              <TableRow>
                <TableHead>Date</TableHead>
                <TableHead>Priority</TableHead>
                <TableHead>High Priority Votes</TableHead>
                <TableHead>Low Priority Votes</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {meetingData?.votes?.map((vote, index) => (
                <TableRow
                  key={index}
                  className={`cursor-pointer ${
                    selectedRow === index ? "bg-primary" : ""
                  }`}
                  onClick={() => setSelectedRow(index)}
                >
                  <TableCell>
                    {format(new Date(vote.date), "dd MMMM yyyy   HH:mm")}
                  </TableCell>
                  <TableCell>{vote.priority}</TableCell>
                  <TableCell>{vote.high}</TableCell>
                  <TableCell>{vote.low}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>

          <Button
            className='bg-primary w-[250px] mt-[50px] self-center'
            onClick={handleSubmit}
          >
            Submit final date and time
          </Button>
        </div>
      ) : (
        <p className='text-2xl font-semibold mt-8'>
          Meeting voting has finalized!
        </p>
      )}
    </div>
  );
};

export default FinalizeMeeting;

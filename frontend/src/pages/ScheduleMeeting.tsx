import Header from "@/components/Header";
import AvailabilityTable from "@/components/home/AvailabilityTable";
import { Button } from "@/components/shadcdn/ui/button";
import {
  Card,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/shadcdn/ui/card";
import { Input } from "@/components/shadcdn/ui/input";
import { useEffect, useState } from "react";
import { MdArrowBack, MdDelete } from "react-icons/md";
import { useNavigate } from "react-router";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import api from "@/api/axios";

export interface SelectedSlot {
  day: string;
  hour: number;
  period: string;
}

export interface MarkedSlot {
  day: string;
  hour: number;
  period: string;
  availabilityType: "available" | "mostly-available";
}

interface Participant {
  name: string;
  email: string;
}

const InputWithLabel = ({
  onChange,
  label,
  type,
  value,
}: {
  label: string;
  onChange: React.ChangeEventHandler<HTMLInputElement> | undefined;
  type?: React.HTMLInputTypeAttribute | undefined;
  value?: string | number | readonly string[] | undefined;
}) => {
  return (
    <div className='flex flex-row items-center gap-4 w-[250px]'>
      <Input value={value} type={type} onChange={onChange} />
      <p className='font-semibold'>{label}</p>
    </div>
  );
};

const ScheduleMeeting = () => {
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [organizerName, setOrganizerName] = useState("");
  const [organizerEmail, setOrganizerEmail] = useState("");
  const [participants, setParticipants] = useState<Participant[]>([
    { name: "", email: "" },
  ]);
  const todayStr = new Date().toISOString().split("T")[0];
  const [durationMinutes, setDurationMinutes] = useState(0);
  const [durationHours, setDurationHours] = useState(0);
  const [dateFrom, setDateFrom] = useState<string>(todayStr);
  const [dateTo, setDateTo] = useState<string>(todayStr);
  const [availabilitySelection, setAvailabilitySelection] = useState<
    "available" | "mostly-available"
  >("available");
  const [selectedSlots, setSelectedSlots] = useState<SelectedSlot[]>([]);
  const [markedSlots, setMarkedSlots] = useState<MarkedSlot[]>([]);
  const [votingDeadline, setVotingDeadline] = useState<Date | null>(new Date());

  const handleChangeVotingDateTime = (date: Date | null) => {
    if (date) {
      setVotingDeadline(date);
    } else {
      setVotingDeadline(new Date());
    }
  };

  useEffect(() => {
    console.log("participants", participants);
  }, [participants]);

  const handleAddParticipant = () => {
    setParticipants([...participants, { name: "", email: "" }]);
  };

  const handleChangeParticipantField = (
    index: number,
    value: string,
    field: string
  ) => {
    setParticipants(
      participants.map((p, pIndex) => {
        if (index === pIndex) {
          return { ...p, [field]: value };
        }
        return p;
      })
    );
  };

  const handleDeleteParticipant = (participantIndex: number) => {
    setParticipants(
      participants.filter((_, index) => index !== participantIndex)
    );
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

  const handleGoBack = () => {
    navigate("/");
  };

  const handleConfirmSchedule = async () => {
    try {
      const response = await api.post("api/meeting/new", {
        title: "Project Kickoff Meeting",
        description:
          "Initial meeting to discuss project scope, responsibilities, and timeline.",
        timeSlots: [
          {
            dateTimeStart: "2025-02-19T10:00:00.000Z",
            priority: "HIGH",
          },
        ],
        duration: 3600,
        participants: [
          {
            name: "Alice Johnson",
            email: "alice.johnson@example.com",
          },
          {
            name: "Bob Smith",
            email: "bob.smith@example.com",
          },
        ],
        organizer: {
          name: "John Doe",
          email: "john.doe@example.com",
        },
        votingDeadline: "2025-02-18T23:59:59.000Z",
      });

      console.log("Meeting created successfully:", response.data);
    } catch (error) {
      console.error(
        "Error creating meeting:",
        error.response?.data || error.message
      );
    }
  };

  return (
    <div className='flex flex-col pb-[100px]'>
      <Header />
      <div className='flex flex-row justify-between items-center mt-[50px] px-8'>
        <MdArrowBack
          className='h-12 w-12 cursor-pointer'
          onClick={handleGoBack}
        />
        <p className='text-2xl font-semibold text-center'>Schedule Meeting</p>
        <div />
      </div>
      <div className='flex flex-col mt-[50px] border-0 items-start self-center w-[600px] py-4'>
        <p className='text-lg font-medium my-4'>Title</p>
        <Input
          value={title}
          placeholder='Meeting title'
          onChange={(event) => setTitle(event.target.value)}
        />
        <p className='text-lg font-medium  my-4'>Description</p>
        <Input
          placeholder='Meeting description'
          value={title}
          onChange={(event) => setDescription(event.target.value)}
        />
        <p className='text-lg font-medium mt-4'> Duration</p>
        <div className='flex flex-row mt-4 gap-8'>
          <InputWithLabel
            label='hs.'
            onChange={(event) => setDurationHours(Number(event.target.value))}
            value={durationHours}
          />
          <InputWithLabel
            label='min.'
            onChange={(event) => setDurationMinutes(Number(event.target.value))}
            value={durationMinutes}
          />
        </div>
        <p className='text-lg font-medium mt-8'> Date Range</p>
        <div className='flex flex-row mt-4 gap-8'>
          <InputWithLabel
            type='date'
            label='from'
            onChange={(event) => setDateFrom(event.target.value)}
            value={dateFrom}
          />
          <InputWithLabel
            type='date'
            label='to'
            onChange={(event) => setDateTo(event.target.value)}
            value={dateTo}
          />
        </div>
        <p className='text-lg font-medium  my-4'>Organizer name</p>
        <Input
          placeholder='Organizer name'
          value={organizerName}
          onChange={(event) => setOrganizerName(event.target.value)}
        />
        <p className='text-lg font-medium  my-4'>Organizer email</p>
        <Input
          placeholder='Organizer email'
          value={organizerEmail}
          onChange={(event) => setOrganizerEmail(event.target.value)}
        />
        <p className='text-lg font-medium  my-4'>Participants</p>
        {participants.map((p, index) => (
          <div
            key={index}
            className='flex w-full flex-row gap-4 items-center mb-4'
          >
            <Input
              className='flex-1'
              placeholder='Participant name'
              value={p.name}
              onChange={(event) =>
                handleChangeParticipantField(index, event.target.value, "name")
              }
            />
            <Input
              className='flex-1'
              type='email'
              placeholder='Participant email'
              value={p.email}
              onChange={(event) =>
                handleChangeParticipantField(index, event.target.value, "email")
              }
            />
            <MdDelete
              fill='red'
              size={24}
              cursor={"pointer"}
              onClick={
                participants.length > 1
                  ? () => handleDeleteParticipant(index)
                  : undefined
              }
            />
          </div>
        ))}
        <Button
          className='bg-primary w-[45%] mr-[40px] self-end'
          onClick={handleAddParticipant}
        >
          Add participant
        </Button>
        <p className='text-lg font-medium  my-4'>Add your times</p>
      </div>

      <div className='flex flex-row justify-between pb-[50px] px-[100px]'>
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
                    borderWidth: availabilitySelection === "available" ? 2 : 1,
                  }}
                  onClick={() => setAvailabilitySelection("available")}
                />
                <div
                  className='h-12 w-12 border-2 border-black bg-mainYellow cursor-pointer'
                  style={{
                    borderWidth:
                      availabilitySelection === "mostly-available" ? 2 : 1,
                  }}
                  onClick={() => setAvailabilitySelection("mostly-available")}
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
      <div className='flex flex-col border-0 self-center w-[600px]'>
        <p className='text-lg font-medium'>Voting deadline</p>
        <DatePicker
          selected={votingDeadline}
          onChange={handleChangeVotingDateTime}
          showTimeSelect
          timeFormat='HH:mm'
          timeIntervals={15}
          timeCaption='Time'
          dateFormat='MMMM d, yyyy h:mm aa'
          className='w-full p-2 border rounded-md mt-4 '
          wrapperClassName='w-full'
          placeholderText='Select a date and time'
        />
        <p className='font-light text-gray-400 mt-8'>
          If no voting deadline is provided, the final time slot for the meeting
          will be determined once all participants have voted.
        </p>
      </div>

      <Button
        className='bg-primary w-[250px] mt-[50px] self-center'
        onClick={handleConfirmSchedule}
      >
        Confirm Schedule
      </Button>
    </div>
  );
};

export default ScheduleMeeting;

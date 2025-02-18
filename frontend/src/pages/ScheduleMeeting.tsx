import Header from "@/components/Header";
import { Button } from "@/components/shadcdn/ui/button";
import { Card, CardContent, CardFooter } from "@/components/shadcdn/ui/card";
import { Input } from "@/components/shadcdn/ui/input";
import { useState } from "react";
import { MdArrowBack } from "react-icons/md";
import { useNavigate } from "react-router";

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

  const handleConfirmSchedule = () => {
    console.log({ durationHours, durationMinutes, dateFrom, dateTo });
  };

  return (
    <div className='flex flex-col'>
      <Header />
      <div className='flex flex-row justify-between items-center mt-[50px] px-8'>
        <MdArrowBack
          className='h-12 w-12 cursor-pointer'
          onClick={handleGoBack}
        />
        <p className='text-3xl font-semibold text-center'>Schedule Meeting</p>
        <div />
      </div>
      <Card className='flex flex-col mt-[100px] items-center self-center min-w-[800px] py-4'>
        <CardContent className='p-8'>
          <p className='text-lg font-medium'> Duration</p>
          <div className='flex flex-row mt-4 gap-8'>
            <InputWithLabel
              label='hs.'
              onChange={(event) => setDurationHours(Number(event.target.value))}
              value={durationHours}
            />
            <InputWithLabel
              label='min.'
              onChange={(event) =>
                setDurationMinutes(Number(event.target.value))
              }
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
        </CardContent>
        <CardFooter className='flex flex-1 justify-center mt-8'>
          <Button
            className='bg-primary w-[250px] self-center'
            onClick={handleConfirmSchedule}
          >
            Confirm Schedule
          </Button>
        </CardFooter>
      </Card>
      {/* <p className='text-black font-light mt-[72px] text-2xl mb-8'>
          My availability
        </p>
        <div className='flex flex-row justify-between pb-[100px]'>
          <AvailabilityTable
            selectedSlots={selectedSlots}
            setSelectedSlots={setSelectedSlots}
            markedSlots={markedSlots}
          />
          <Card className='bg-white border border-black h-[350px] ml-[50px] fixed right-[50px]'>
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
                      borderWidth:
                        availabilitySelection === "available" ? 2 : 1,
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
        </div> */}
    </div>
  );
};

export default ScheduleMeeting;

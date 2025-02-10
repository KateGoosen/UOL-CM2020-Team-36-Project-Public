import Header from "@/components/Header";
import { Button } from "@/components/shadcdn/ui/button";
import { Card, CardContent, CardFooter } from "@/components/shadcdn/ui/card";
import { Input } from "@/components/shadcdn/ui/input";
import { useState } from "react";
import { MdArrowBack } from "react-icons/md";
import { useNavigate } from "react-router";

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
    </div>
  );
};

export default ScheduleMeeting;

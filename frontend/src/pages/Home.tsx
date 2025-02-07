import React, { useState } from "react";
import {
  Card,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/shadcdn/ui/card";
import AvailabilityTable from "@/components/home/AvailabilityTable";
import { Button } from "@/components/shadcdn/ui/button";

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

const Home = () => {
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

  return (
    <div>
      <div className='bg-primary h-[80px] w-full pl-[50px] flex flex-row items-center'>
        <p className='text-black font-semibold text-2xl'>Syncify</p>
      </div>
      <div className='pt-[100px] px-[50px] flex flex-col'>
        <div className='flex flex-row gap-[50px]'>
          <Card className='w-[350px] bg-primary pb-5 cursor-pointer'>
            <CardHeader>
              <CardTitle className='text-xl'>Schedule meeting</CardTitle>
              <CardDescription>
                Create a meeting task by specifying the duration and date range
                for scheduling.
              </CardDescription>
            </CardHeader>
          </Card>
          <Card className='w-[350px] bg-primary pb-5 cursor-pointer'>
            <CardHeader>
              <CardTitle className='text-xl'>View Scheduled Meetings</CardTitle>
              <CardDescription>
                Access a list of all scheduled meetings with their details.
              </CardDescription>
            </CardHeader>
          </Card>
        </div>
        <p className='text-black font-light mt-[72px] text-2xl mb-8'>
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
              <CardFooter style={{ marginTop: 50 }}>
                <Button
                  className='bg-primary w-full self-center'
                  onClick={handleMarkSelectedSlots}
                >
                  Mark Selection
                </Button>
              </CardFooter>
            </CardHeader>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Home;

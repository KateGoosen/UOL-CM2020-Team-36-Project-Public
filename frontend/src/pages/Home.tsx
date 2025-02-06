import React, { useState } from "react";
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/shadcdn/ui/card";
import AvailabilityTable from "@/components/home/AvailabilityTable";

const Home = () => {
  const [availabilitySelection, setAvailabilitySelection] =
    useState("available");

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
        <p className='text-black font-light mt-[72px] text-2xl'>
          My availability
        </p>
        <div className='flex flex-row justify-around pb-[100px]'>
          <AvailabilityTable />
          <Card className='bg-white border border-black h-[400px] ml-[50px]'>
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
            </CardHeader>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Home;

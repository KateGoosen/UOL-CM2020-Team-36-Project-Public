import React from "react";
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/shadcdn/ui/card";
import AvailabilityTable from "@/components/home/AvailabilityTable";

const Home = () => {
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
        <AvailabilityTable />
      </div>
    </div>
  );
};

export default Home;

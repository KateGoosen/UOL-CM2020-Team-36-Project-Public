import Header from "@/components/Header";
import api from "@/api/axios";
import { useEffect } from "react";

const ViewScheduledMeetings = () => {
  const handleGetMeetings = async () => {
    await api.get(`api/meeting/john.doe@example.com`);
  };

  useEffect(() => {
    handleGetMeetings();
  }, []);

  return (
    <div>
      <Header />
    </div>
  );
};

export default ViewScheduledMeetings;

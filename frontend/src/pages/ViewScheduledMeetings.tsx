import Header from "@/components/Header";
import api from "@/api/axios";
// import { useEffect } from "react";
import { MdArrowBack } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/shadcdn/ui/button";
import { useState } from "react";
import { ToastContainer, toast } from "react-toastify";

interface Meeting {
  title: string;
  description: string;
  duration: number;
  isVotingOpened: boolean;
  finalTimeSlot: string;
}

const ViewScheduledMeetings = () => {
  const navigate = useNavigate();
  const [meetings, setMeetings] = useState<Meeting[]>([]);

  const isValidEmail = (email: string) => {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
  };

  const handleGetMeetings = async (e: React.FormEvent) => {
    e.preventDefault();
    const formData = new FormData(e.target as HTMLFormElement);
    const email = formData.get("email") as string;
    try {
      if (!isValidEmail(email)) {
        alert("Please enter a valid email address.");
        return;
      }

      api.get(`api/meeting/${email}`).then((res) => {
        setMeetings(res.data);
      });
    } catch (error) {
      console.error(error);
      alert("Failed to get meetings");
    }
  };

  const handleGoBack = () => {
    navigate("/");
  };

  const handleLinkCopy = (e: React.MouseEvent) => {
    e.preventDefault();
    const link = e.currentTarget as HTMLAnchorElement;
    navigator.clipboard.writeText(link.href);
    toast.success("Link copied to clipboard", {
      position: "top-right",
      autoClose: 1000,
      hideProgressBar: true,
      closeOnClick: false,
      pauseOnHover: false,
      draggable: false,
      progress: undefined,
      theme: "colored",
    });
  };

  const formatDuration = (minutes: number) => {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    return `${hours > 0 ? `${hours} hour${hours > 1 ? "s" : ""} ` : ""}${
      remainingMinutes > 0
        ? `${remainingMinutes} minute${remainingMinutes > 1 ? "s" : ""}`
        : ""
    }`.trim();
  };

  return (
    <div className="flex flex-col pb-[100px]">
      <Header />
      <div className="flex flex-row justify-between items-center mt-[50px] px-8">
        <MdArrowBack
          className="h-12 w-12 cursor-pointer"
          onClick={handleGoBack}
        />
        <p className="text-2xl font-semibold text-center">My meetings</p>
        <div />
      </div>
      <div className="flex flex-row justify-center items-center mt-[50px] px-8">
        <form className="w-[75%]" onSubmit={handleGetMeetings}>
          <div className="flex justify-between items-center gap-10">
            <p>Your e-mail</p>
            <input
              type="email"
              name="email"
              className="flex h-10 w-64 rounded-md border border-input bg-background px-3 py-2 text-base ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-foreground placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
            />
            <Button>View my meetings</Button>
          </div>
        </form>
      </div>
      <div className="flex flex-row justify-center items-center mt-[50px] px-8">
        {meetings.length === 0 ? (
          <p className="text-center mt-4">No meetings found</p>
        ) : (
          <table className="table-fixed bg-primary w-[860px] border-2 border-gray-500">
            <thead>
              <tr className="text-white">
                <th className="border border-gray-500">Title</th>
                <th className="border border-gray-500">Final Date and Time</th>
                <th className="border border-gray-500">Duration</th>
                <th className="border border-gray-500">Share Link</th>
              </tr>
            </thead>
            <tbody>
              {meetings.map((meeting, index) => (
                <tr key={index}>
                  <td className="border border-gray-500 p-1">
                    {meeting.title}
                  </td>
                  <td className="border border-gray-500 p-1">
                    {new Date(meeting.finalTimeSlot).toLocaleString()}
                  </td>
                  <td className="border border-gray-500 p-1">
                    {formatDuration(meeting.duration)}
                  </td>
                  <td className="border border-gray-500 p-1">
                    {/* TODO: Change to actual shareable link */}
                    <a href="https://google.com" onClick={handleLinkCopy}>
                      Click here to copy link
                    </a>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
      <ToastContainer
        position="top-right"
        autoClose={1000}
        limit={1}
        hideProgressBar
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss={false}
        draggable={false}
        pauseOnHover={false}
        theme="colored"
      />
    </div>
  );
};

export default ViewScheduledMeetings;

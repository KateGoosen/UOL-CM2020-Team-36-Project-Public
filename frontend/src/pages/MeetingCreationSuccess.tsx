import Header from "@/components/Header";
import { FaCopy } from "react-icons/fa";
import { useParams } from "react-router-dom";

const MeetingCreationSuccess = () => {
  const { meetingId, organizerToken } = useParams();
  const organizerLink = `http://localhost:5173/meeting/${meetingId}/${organizerToken}`;
  const participantLink = `http://localhost:5173/meeting/${meetingId}`;
  const isEditting = window.location.href.includes("edit");

  const copyToClipboard = (text: string) => {
    navigator.clipboard
      .writeText(text)
      .then(() => {
        alert("Copied to clipboard!");
      })
      .catch((err) => {
        console.error("Failed to copy text: ", err);
        alert("Failed to copy text.");
      });
  };

  return (
    <div className='flex flex-col pb-[100px]'>
      <Header />
      <div className='flex flex-col justify-center mt-[50px] px-8'>
        <p className='text-2xl font-semibold text-center'>
          Your meeting has been {isEditting ? "edited" : "scheduled"}!
        </p>
        <p className='mt-[100px] text-lg font-semibold text-center'>
          All the participants will be notified via provided e-mails.
        </p>

        <div className='flex flex-col w-full pl-[20%] pr-[10%] items-start'>
          {/* Organizer Link */}
          <div className='mt-[50px] flex flex-row w-full justify-between items-center'>
            <p className='text-lg w-[300px]'>
              Your link for managing the meeting. <br />
              Do not share it with non-organizers:
            </p>
            <p className='text-blue-700 max-w-[30%] overflow-ellipsis'>
              {organizerLink}
            </p>
            <FaCopy
              className='h-8 w-8 cursor-pointer'
              color='#c3e9cb'
              onClick={() => copyToClipboard(organizerLink)}
            />
          </div>

          {/* Participant Link */}
          <div className='mt-[50px] flex flex-row w-full justify-between items-center'>
            <p className='text-lg w-[300px]'>Link for participants:</p>
            <p className='text-blue-700 max-w-[30%] overflow-ellipsis'>
              {participantLink}
            </p>
            <FaCopy
              className='h-8 w-8 cursor-pointer'
              color='#c3e9cb'
              onClick={() => copyToClipboard(participantLink)}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MeetingCreationSuccess;

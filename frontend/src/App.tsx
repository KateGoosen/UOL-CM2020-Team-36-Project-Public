import Home from "./pages/Home";
import { BrowserRouter, Routes, Route } from "react-router";
import ScheduleMeeting from "@/pages/ScheduleMeeting";
import ViewScheduledMeetings from "@/pages/ViewScheduledMeetings";
import EditMeeting from "./pages/EditMeeting";
import MeetingCreationSuccess from "./pages/MeetingCreationSuccess";
import VoteTimeSlots from "./pages/VoteTimeSlots";
import VoteMeetingSuccess from "./pages/VoteMeetingSuccess";
import FinalizeMeeting from "./pages/FinalizeMeeting";
import MeetingFinalizeSuccess from "./pages/MeetingFinalizeSuccess";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/schedule-meeting' element={<ScheduleMeeting />} />
        <Route
          path='/view-scheduled-meetings'
          element={<ViewScheduledMeetings />}
        />
        <Route
          path='/edit/:meetingId/:userId/:organizerToken'
          element={<EditMeeting />}
        />
        <Route
          path='/schedule-meeting/success/:meetingId/:organizerToken'
          element={<MeetingCreationSuccess />}
        />
        <Route
          path='/schedule-meeting/edit/success/:meetingId/:organizerToken'
          element={<MeetingCreationSuccess />}
        />
        <Route path='/meeting/:meetingId' element={<VoteTimeSlots />} />
        <Route
          path='/meeting/:meetingId/success'
          element={<VoteMeetingSuccess />}
        />
        <Route
          path='/meeting/:meetingId/:organizerToken'
          element={<FinalizeMeeting />}
        />
        <Route
          path='/meeting/:meetingId/:organizerToken/finalize'
          element={<MeetingFinalizeSuccess />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

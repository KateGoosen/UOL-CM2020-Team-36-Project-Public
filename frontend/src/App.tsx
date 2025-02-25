import Home from "./pages/Home";
import { BrowserRouter, Routes, Route } from "react-router";
import ScheduleMeeting from "@/pages/ScheduleMeeting";
import ViewScheduledMeetings from "@/pages/ViewScheduledMeetings";
import EditMeeting from "./pages/EditMeeting";

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
      </Routes>
    </BrowserRouter>
  );
}

export default App;

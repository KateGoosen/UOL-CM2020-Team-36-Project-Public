import Home from "./pages/Home";
import { BrowserRouter, Routes, Route } from "react-router";
import ScheduleMeeting from "@/pages/ScheduleMeeting";
import ViewScheduledMeetings from "@/pages/ViewScheduledMeetings";

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
      </Routes>
    </BrowserRouter>
  );
}

export default App;

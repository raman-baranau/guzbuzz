import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import { NavbarSimple } from "./components/NavbarSimple";
import { JoinComponent } from "./components/JoinComponent";
import BuzzBoard from "components/BuzzBoard";


const App = () => {
  const [auth, setAuth] = useState({
    roomId: null,
    playerName: null,
    token: null
  });
  return (
    <div className="bg-slate-200 h-full min-h-screen">
      <NavbarSimple />
      <div>
        <Router>
          <Routes>
            <Route path="/:id" element={<BuzzBoard auth={auth} setAuth={setAuth} />} />
            <Route path="/" element={<JoinComponent setAuth={setAuth} />} />
          </Routes>
        </Router>
      </div>
    </div>
  );
};

export default App;

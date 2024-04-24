import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import { NavbarSimple } from "./components/NavbarSimple";
import { JoinComponent } from "./components/JoinComponent";

const App = () => {
  return (
    <div>
      <NavbarSimple />
      <Router>
        <Routes>
          <Route path="/:id"/>
          <Route path="/" element={<JoinComponent/>} />
        </Routes>
      </Router>
    </div>
  );
};

export default App;

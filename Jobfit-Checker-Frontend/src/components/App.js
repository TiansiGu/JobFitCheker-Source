import React, { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import AppContent from './AppContents';
import RegistrationSuccessful from './RegistrationSuccessful';

const App = () => {
  const [user, setUser] = useState(null);

  return (
    <Routes>
      <Route path="/*" element={<AppContent user={user} setUser={setUser} />} />

      <Route path="/login" element={<Login setUser={setUser} />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/registrationSuccessful"
        element={<RegistrationSuccessful />}
      />
    </Routes>
  );
};

export default App;

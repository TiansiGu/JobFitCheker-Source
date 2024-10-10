// App.js or wherever you define your main routing logic
import React, { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from './Login'; // Adjust the path as necessary
import Register from './Register'; // Adjust the path as necessary
import AppContent from './AppContents'; // Your main or home component
import RegistrationSuccessful from './RegistrationSuccessful';

const App = () => {
  const [user, setUser] = useState(null); // Initialize user state

  return (
    <Routes>
      <Route path="/*" element={<AppContent user={user} setUser={setUser} />} />

      <Route path="/login" element={<Login setUser={setUser} />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/registrationSuccessful"
        element={<RegistrationSuccessful />}
      />
      {/* Add other routes as needed */}
    </Routes>
  );
};

export default App;

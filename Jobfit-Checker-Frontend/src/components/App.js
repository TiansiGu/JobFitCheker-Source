// App.js or wherever you define your main routing logic
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login'; // Adjust the path as necessary
import Register from './Register'; // Adjust the path as necessary
import AppContent from './AppContents'; // Your main or home component
import RegistrationSuccessful from './RegistrationSuccessful';

const App = ({ user, setUser }) => {
  return (
    <Routes>
      <Route path="/" element={<AppContent />} />
      <Route path="/login" element={<Login />} />
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

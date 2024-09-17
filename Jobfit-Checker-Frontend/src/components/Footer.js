import React, { useEffect, useState } from 'react';

const Footer = () => {
  const [currentDate, setCurrentDate] = useState('');

  useEffect(() => {
    // Set the current date when the component mounts
    const today = new Date().toLocaleDateString();
    setCurrentDate(today);
  }, []); // Empty dependency array to run only on mount

  return (
    <footer id="footer">
      <p>&copy; 2024 Job Fit Checker. All rights reserved.</p>
      <p>
        Current Date: <span id="current-time">{currentDate}</span>
      </p>
    </footer>
  );
};

export default Footer;
